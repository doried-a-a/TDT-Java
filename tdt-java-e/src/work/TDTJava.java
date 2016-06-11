/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package work;

import nlp.Stemmer;
import nlp.StopWordsRemover;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
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

	public static int DRAW_CHART_NONE = 0;
    public static int DRAW_CHART_COST = 1;
    public static int DRAW_CHART_DET = 2;
    public static String docVectorsOutputFile = "/home/doried/Downloads/TDT/alaa/ZPclustering/tdtDocVecs.txt";
    public static String docSimilarityMatrixOutputPath = "/home/doried/Downloads/TDT/alaa/ZPclustering/tdtDocSimMatrix.txt";
    public static String topicStorieLabelsOutputPath = "/home/doried/Downloads/TDT/alaa/ZPclustering/tdtTopicStoryLabels.txt";
    public static int ExportType = 1; // 0 for exporting docVectors , 1 for similarity matrix
    public static String filesPath = "/home/doried/Downloads/TDT/alaa/ZPclustering/files/";
    
    
    public static void main(String[] args) throws Exception{
    	//testTracking(DRAW_CHART_DET);
    
    	exportDataForClustering(docVectorsOutputFile,topicStorieLabelsOutputPath);
    }
    
    
    
    
    
    public static void exportDataForClustering(String outputFile,String topicStorieLabelsOutputPath) throws Exception{
    	
    	//those are text-processed
    	String pathToAllStories = "/home/doried/tdt/test/modified/allStories/"; 
    	
    	// Building IDF and documents' vectors
   
    	IdfExternalProvider idfProvider = new IdfExternalProvider();
    	
        List<File> allStories = DirectoryProcessor.getListOfFiles(pathToAllStories);
        List<TfidfVectorSpaceDocumentRepresentation> docVectors = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
        
        System.out.println("Building IDF and document vectors...");        
        for(File file:allStories){
        	// with stop-words removed
        	StoryFile f = DirectoryProcessor.readStoryFile(file,false,5);
            String fileContent = f.getStoryContent();
            TfidfVectorSpaceDocumentRepresentation doc = new TfidfVectorSpaceDocumentRepresentation(fileContent,idfProvider,f.getStoryDate());
            idfProvider.indexDocument(doc);
            docVectors.add(doc);
        }
        System.out.println("IDF and documents' vectors built.\nNow filtering light words and removing empty docs..");
        
        int filterLimit = 350;
        List<TfidfVectorSpaceDocumentRepresentation> newDocs = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
        List<String> topics = new ArrayList<String>();
        HashMap<String, ArrayList<String>> topic_story = new HashMap<String,ArrayList<String>>();
        
        int fileNumber=1; 
        
        for(int currentStory=0;currentStory<allStories.size();currentStory++){
        	StoryFile f = DirectoryProcessor.readStoryFile(allStories.get(currentStory) ,false,5);
        	String topic = DirectoryProcessor.extractTopicTag(allStories.get(currentStory).getName());
        	TfidfVectorSpaceDocumentRepresentation doc = docVectors.get(currentStory);
        	
        	doc.filterLightWords(filterLimit, false);
    		if(doc.getWordsOfDocument().size()==0)
    			continue;
    		
    		newDocs.add(doc);
    		topics.add(topic);
    		
    		String filePath=filesPath+fileNumber + "_" + topic+".txt";
            PrintWriter wr = new PrintWriter(new File(filePath));
            wr.print(f.getStoryContent() + "\n\n---------\n" + 
            		allStories.get(currentStory).getAbsolutePath() + "\n"
            		+ f.getStoryTitle() + "\n" + f.getStoryTag() + "\n" + f.getStoryUrl()
            	);
            
            wr.close();
    		
            
        	ArrayList<String> topicStories=null;
        	topicStories = topic_story.getOrDefault(topic,null);
        	if (topicStories==null){
        		topicStories = new ArrayList<String>();
        		topic_story.put(topic,topicStories);
        	}
        	topicStories.add(fileNumber + "");
            
    		fileNumber++;
        }
        docVectors = newDocs;
        
        PrintWriter out = new PrintWriter(new File(topicStorieLabelsOutputPath));
        for(String topic:topic_story.keySet()){
        	ArrayList<String> topicStories = topic_story.get(topic);
        	String line="";
        	for(String story:topicStories)
        		line += story + " ";
        	if(line.length()>0)
        		line = line.substring(0,line.length()-1);
        	out.println(line);
        }	       	       
        out.close();

        int numOfDocs = idfProvider.getTotalDocumentsCount();
        int numOfTerms = idfProvider.getTotalTermsCount();
        
        System.out.println("Total docs : " + numOfDocs + ", after filtering ==> " + docVectors.size());
        
        
        if (ExportType==0){
	       
	        System.out.println("Total docs count : " + numOfDocs + "\nTerms count : " + numOfTerms);
	        
	        Set<Word> terms = idfProvider.getTermsSet();
	        Set<Word> termsToTake = new HashSet<Word>();
	        
	        PriorityQueue<WeightedTerm> weightedTerms = new PriorityQueue<WeightedTerm>();
	        int skipHeavy = (int) (0.08*numOfTerms);
	        int skipLight = (int) (0.4*numOfTerms);
	        int take = numOfTerms-skipHeavy-skipLight;
	        System.out.println("Skipping " + skipHeavy + " then taking " + take + " then skipping " + skipLight);
	        for(Word term:terms){
	        	weightedTerms.add(new WeightedTerm(term,-idfProvider.getIdf(term)));
	        }
	        for(int i=0;i<skipHeavy;i++){
	        	WeightedTerm w = weightedTerms.poll();
	        	//System.out.println("Skipping " + w.getWord() + " " + (-w.getWeight()));
	        }
	        for(int i=0;i<take;i++){
	        	WeightedTerm w = weightedTerms.poll();
	        	//System.out.println(w.getWord() + " " + (-w.getWeight()));
	        	termsToTake.add(w.getWord());
	        }
	        
	        System.out.println("Iterating over documents' vectors to construct and save weighted doc vectors to the output file..");
	        double tmpValue = 0;
	        
	        out = new PrintWriter(new File(outputFile));
	        
	        for(TfidfVectorSpaceDocumentRepresentation vec:docVectors){
	        	String line = "";
	        	for(Word term : termsToTake){
	        		tmpValue = vec.getValue(term);
	        		line += tmpValue + " ";
	        	}
	        	if(line.length()>0)
	        		line = line.substring(0,line.length()-1);
	        	out.println(line);
	        }
	        
	        out.close();
        }
        
        else if(ExportType==1) {
        	System.out.println("Exporting similarity matrix..");
        	out = new PrintWriter(new File(docSimilarityMatrixOutputPath));
        	for(TfidfVectorSpaceDocumentRepresentation doc1 : docVectors){
        		String line = "";
        		for (TfidfVectorSpaceDocumentRepresentation doc2 : docVectors ){
        			double similarity = doc1.getSimilarity(doc2);
        			line += similarity + " ";
        		}
        		if(line.length()>0)
        			line = line.substring(0,line.length()-1);
        		out.println(line);
        	}
        	out.close();	
        }
        
        System.out.println("Done!");
    }
    
    
    
    
    public static void testTracking(int drawChart) throws Exception{
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
        
        //String [] topics = {"3","5","11","12","13","14","15","21","23","25","31","37","39"};
        String [] topics = {"25","31","3"};
        double step = 0.01;
        
        
        for(threshold=0.01;threshold<1;threshold+=(threshold<0.1? step*0.5:step)){
        	double overallFA =0, overallMisses=0; double overallCost = 0;
            
        	//double pTopicAvg=0;
        	for(String topicName : topics)
            {
            	String topicPath = "/home/doried/tdt/test/topics/" + topicName + "/";
	        	Result result = trackATopic(threshold, numOfTrainingDocs, topicPath, idfProvider,allStories,2,1);
	        	overallFA += result.getFalseAlarmRate();
	        	overallMisses += result.getMissRate();
	        	overallCost += result.getCost();
	        	//pTopicAvg += result.getPriorYesRate();
            }
        	//pTopicAvg /= topics.length;
        	//System.out.println("p(yes)=" + pTopicAvg);
        	thresholds.add(threshold);
        	costs.add(overallCost/topics.length);
        	fas.add(overallFA/topics.length*100.0);
        	misses.add(overallMisses/topics.length*100.0);
        }
        
        if(drawChart == DRAW_CHART_COST){
	        Drawer drawer = new Drawer("Costs","threshold","cost-value");
	        drawer.draw(thresholds, costs,"cost");
	        drawer.draw(thresholds, fas,"false alarm");
	        drawer.draw(thresholds, misses,"miss");
        }
        else if(drawChart == DRAW_CHART_DET){
	        Drawer drawer = new Drawer("DET","FA prob.","miss prob.");
	        drawer.draw(fas, misses,"tradeoff");
        }
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