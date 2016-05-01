/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package work;

import nlp.Stemmer;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jfree.util.ArrayUtilities;

import files.DirectoryProcessor;
import files.StoryFile;
import files.TopicFiles;
import nlp.TextProcessor;
import tdt.*;
import tfidf.*;
import vectorspace.*;
import gui.*;

public class TDTJava {

    public static void main(String[] args) throws Exception{
    	testTracking();
    }
    
    
    
    
    public static void testTracking() throws Exception{
    	//those are text-processed
    	String pathToAllStories = "/home/doried/tdt/test/all/";
    	int numOfTrainingDocs = 4; 
    	
    	// Bulding idf
    	IdfExternalProvider idfProvider = new IdfExternalProvider();
        List<File> allStories = DirectoryProcessor.getListOfFiles(pathToAllStories);

        for(File file:allStories){ 
        	// with stop-words removed
        	StoryFile f = DirectoryProcessor.readStoryFile(file,false);
            String fileContent = f.getStoryContent();
            TfidfVectorSpaceDocumentRepresentation doc = new TfidfVectorSpaceDocumentRepresentation(fileContent,idfProvider,f.getStoryDate());
            idfProvider.indexDocument(doc);
        }
        
        double threshold = 0.125;

        System.out.println("Number of all stories: " + allStories.size() + "\n");
        
        ArrayList<Double> thresholds = new ArrayList<Double>();
        ArrayList<Double> costs = new ArrayList<Double>();
        ArrayList<Double> fas = new ArrayList<Double>();
        ArrayList<Double> misses = new ArrayList<Double>();
        
        String [] topics = {"5","21","23","25","31","37","39"};
        
        for(threshold=0.05;threshold<0.8;threshold+=0.015){
        	double overallFA =0, overallMisses=0; double overallCost = 0;
            
        	for(String topicName : topics)
            {
            	String topicPath = "/home/doried/tdt/test/topics/" + topicName + "/";
	        	Result result = trackATopic(threshold, numOfTrainingDocs, topicPath, idfProvider,allStories,2,1);
	        	overallFA += result.getFalseAlarmRate();
	        	overallMisses += result.getMissRate();
	        	overallCost += result.getCost();
            }
        	thresholds.add(threshold);
        	costs.add(overallCost/topics.length);
        	fas.add(overallFA/topics.length);
        	misses.add(overallMisses/topics.length);
        }
        
        
        Drawer drawer = new Drawer("Costs","threshold","cost-value");
        drawer.draw(thresholds, costs,"cost");
        drawer.draw(thresholds, fas,"false alarm");
        drawer.draw(thresholds, misses,"miss");
        
    }
    
    
    public static Result trackATopic(double threshold, int numOfTrainingDocs,
    		String topicPath , IdfExternalProvider idfProvider,List<File> allStories,double C_miss,double C_false_alarm) throws Exception{
    	
    	double thresholdAdaptIncrement =0.1*threshold; //0.037;
    	int numOfWordsToKeep=350;
    		
        //initiating training stories
        List<TfidfVectorSpaceDocumentRepresentation> storiesSeen = new ArrayList();
        
        //Reading the topic files (and text-processing them)
        TopicFiles topicFiles = DirectoryProcessor.readTopicFiles(new File(topicPath),false);
        
        Topic topic = new Topic(idfProvider, topicFiles.getAverageDate());
        topic.setTopicTag(topicFiles.getTag());
        
        int cc=0;
        for(int i=0;i<Math.min(numOfTrainingDocs,topicFiles.getStories().size());i++){
        	StoryFile f = topicFiles.getStories().get(i);	
            Date date = f.getStoryDate();
            TfidfVectorSpaceDocumentRepresentation storyTfidf = new TfidfVectorSpaceDocumentRepresentation(f.getStoryContent(), idfProvider, date);
            //storyTfidf.filterLightWords(numOfWordsToKeep,false);
            topic.addStoryToTopic(f.getStoryTag(), storyTfidf);
            topic.adaptStoryIntoTopic(storyTfidf);
            
            storiesSeen.add(storyTfidf);
        }
        
        topic.updateTopicNormalizationFactor(storiesSeen);

        // Tracking the topic
        int num_of_misses  = 0;
        int num_of_false_alarms = 0;
        int num_of_correct_yes=0;
        int num_of_correct_no =0;
        
        double sumYesSim=0; int cntYes=0;
        
        List<Double> similarities = new ArrayList();
        
        DecimalFormat df = new DecimalFormat("00.000");
        
        for(File file:allStories)
        {
        	StoryFile f = DirectoryProcessor.readStoryFile(file,false);
            TfidfVectorSpaceDocumentRepresentation doc = new TfidfVectorSpaceDocumentRepresentation(
            		f.getStoryContent(), idfProvider, f.getStoryDate());
            doc.filterLightWords(numOfWordsToKeep,false);
            TfidfVectorSpaceDocumentRepresentation original = new TfidfVectorSpaceDocumentRepresentation(
            		f.getStoryContent(), idfProvider, f.getStoryDate());
            
            storiesSeen.add(doc);
            
            double sim = topic.getSimilarity(doc) / topic.getTopicNormalizationFactor();
            
            boolean decision = sim>threshold;
            
            boolean sameTopic = DirectoryProcessor.extractTopicTag(f.getStoryTag()).equals(topic.getTopicTag());
            
            if( sim > threshold+thresholdAdaptIncrement)
            {
	            	topic.addStoryToTopic(file.getName(), original);
	            	topic.adaptStoryIntoTopic(original);
	            	//topic.updateTopicNormalizationFactor(storiesSeen);
            }
            
            if(sameTopic)
            {
            	sumYesSim+=sim;
            	cntYes++;
            	similarities.add(sim);
            }
            
            if(sameTopic && decision==false)
            	num_of_misses ++;
            if (!sameTopic && decision==true)
            	num_of_false_alarms++;
            
            if(sameTopic==decision)
            	if(decision)
            		num_of_correct_yes++;
            	else
            		num_of_correct_no++;	
        }
        
        Result result = new Result(num_of_misses, num_of_false_alarms, cntYes, allStories.size());
        
        // this is a normalized cost. Cost>1 means our system is biased twords yes or no
        result.setCost( 
        		C_miss*result.getMissRate()*result.getPriorYesRate() + C_false_alarm*result.getFalseAlarmRate()*result.getPriorNoRate()  
        	///	Math.min(C_miss*result.getPriorYesRate(), C_false_alarm*result.getPriorNoRate())
        		);
       
        
        System.out.println(threshold + " :[" + df.format(result.getCost()) + "]\n" +   "FA:" + result.getNumberOfFalseAlarms() + " ("+df.format(result.getFalseAlarmRate()*100.0)+")"
        		+ "\nMisses:" + result.getNumberOfMisses() + " (" + df.format(result.getMissRate()*100.0) + ")"   
        		+ "\n----------------------\n");
        
        return result;
    }
    
}