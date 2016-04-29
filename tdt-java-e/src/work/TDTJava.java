/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package work;

import nlp.Stemmer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import files.DirectoryProcessor;
import files.StoryFile;
import files.TopicFiles;
import nlp.TextProcessor;
import tdt.*;
import tfidf.*;
import vectorspace.*;


public class TDTJava {

    public static void main(String[] args) throws Exception{
    	trackATopic();
    }
    
    
    
    public static void trackATopic() throws Exception{
    	
    	//those are text-processed
    	String pathToAllStories = "/home/doried/tdt/test/all/";
    	// those are not text-processed
    	String topicPath = "/home/doried/tdt/data/test2/text/25/";
    	
    	int numOfTrainingDocs = 3; 
    	double threshold = 0.1295;//0.1295; //0.437
    	double thresholdAdaptIncrement =0.1*threshold; //0.037;
    	int numOfWordsToKeep=350;
    	
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

        //initiating training stories
        List<TfidfVectorSpaceDocumentRepresentation> storiesSeen = new ArrayList();
        
        //Reading the topic files (and text-processing them)
        TopicFiles topicFiles = DirectoryProcessor.readTopicFiles(new File(topicPath),true);
        
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
            
            
            System.out.println(topic.getTopicTag() + "=" + DirectoryProcessor.extractTopicTag(f.getStoryTag()) + ":" + sameTopic + ", ourDec=" + decision + ", sim=" + sim);	
        }
        
        System.out.println("# Stories:" + allStories.size() + "\nFA:" + num_of_false_alarms + " (" +  ((double)num_of_false_alarms/(allStories.size()-cntYes))+")"+
        		"\nMisses:" + num_of_misses + " (" + ((double)num_of_misses/(cntYes)) + ")"+   "\nCorrect-Yes:" + num_of_correct_yes + "\nCorrect-No:"+num_of_correct_no);
        double variance=0;
        double avg = sumYesSim/cntYes;
        for(double si:similarities){
        	variance += (si-avg)*(si-avg);
        }
        variance/=cntYes;
        double sd=Math.sqrt(variance);
        System.out.println(sumYesSim/cntYes + " " + cntYes + " sd=" + sd + " , avg-3sd=" + (avg-3*sd));
    }
    
    public static void work() throws Exception{
// Files in this dir are textually preprocessed
//        
//        String pathToAllStories = "/home/doried/tdt/test/all/";
//        List<File> storiesFiles = DirectoryProcessor.getListOfFiles(pathToAllStories);
//        
//        TopicCollection coll = new TopicCollection();
//        coll.enableAutomaticDFUpdating(false);
//
//        for(File file:storiesFiles){
//        	String [] f = DirectoryProcessor.readStoryFile(file);
//            String fileContent = f[3];
//            String date_str = f[2];
//            coll.updateTfidfWithStory(fileContent);
//        }
//        
//        
//        for(File file:storiesFiles){
//        	String [] f = DirectoryProcessor.readStoryFile(file);
//            String fileContent = f[3];
//            System.err.println(f[2]);
//            String date_str = f[2];
//            if (f[2].contains("."))
//            	date_str = f[2].substring(0,f[2].indexOf('.'));
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = simpleDateFormat.parse(date_str);
//            coll.addStory(fileContent,file.getName(),date);
//        }
//        
//        int num_of_true  = 0;
//        int num_of_false = 0;
//        for (int i=0;i<coll.topics.size();i++){
//            Topic t = coll.topics.get(i);
//            String tag = t.topicTag; 
//            String tag_part1 = tag.substring(0,tag.indexOf("-"));
//            
//            System.out.println("Topic " + i + "(" + tag_part1 + ") has stories:");
//            for (int j=0;j<t.storyTitles.size();j++){
//            	String stag_part1 = t.storyTitles.get(j).substring(0,tag.indexOf("-"));
//            
//            	System.out.println(t.storyTitles.get(j) + " - " + stag_part1);
//            	
//            	if (tag_part1.equals(stag_part1))
//            		num_of_true++;
//            	else
//            		num_of_false++;
//            }
//            System.out.println("-----------------");
//        }
//        
////        int numClustersOk = 0 , numClustersFalse = 0;
////        for (int i=0;i<coll.topics.size();i++){
////            Topic t = coll.topics.get(i);
////            for (int j=0;j<coll.topics.size();j++){
////            	if (i==j)
////            		continue;
////            	Topic q = coll.topics.get(j);
////            	if(t.topic.getSimilarity(q.topic)>0.535)
////            		if (t.topicTag.substring(0,t.topicTag.indexOf('-')).equals(q.topicTag.substring(0,t.topicTag.indexOf('-'))))
////            			numClustersOk++;
////            		else
////            			numClustersFalse++;
////            		//System.out.println(t.topicTag + " = " + q.topicTag + " ==> " + t.topic.getSimilarity(q.topic) );
////            }
////          
////        }
////        System.out.println("Stories that could be moved correctly " + numClustersOk + "\n" + 
////        "Stories that could be mved incorrectly " + numClustersFalse);
////        
//        int numOfSmallTopics = 0;
//        double sumMovedCorrectly=0;
//        int numMovedCorrectly = 0;
//        int numMovedInorrectly = 0;
//        double sumMovedIncorrectly=0;
//        for (int i=0;i<coll.topics.size();i++){
//            Topic t = coll.topics.get(i);
//            if (t.stories.size() > 2 )
//            	continue;
//            
//            numOfSmallTopics++;
//           // System.out.println("Topic " + t.topicTag + " has " + t.stories.size() + " stories only..");
//            
//            for (int j=0;j<t.stories.size();j++){
//            	
//            	double maxSim = 0; int maxInd = -1;
//            	
//            	
//            	for (int k=0;k<coll.topics.size();k++){
//                 	if (i==k)
//                 		continue;
//                 	
//                 	Topic q = coll.topics.get(k);
//                 	if(t.topicRepresentation.getSimilarity(q.topicRepresentation)> maxSim){
//                 		maxSim = t.topicRepresentation.getSimilarity(q.topicRepresentation);
//                 		maxInd = k;
//                 	}
//                 }
//            	System.out.println ("Story " + t.storyTitles.get(j) + " to " + coll.topics.get(maxInd).topicTag + " for " + maxSim);
//            	if (t.storyTitles.get(j).substring(0,t.storyTitles.get(j).indexOf('-')).equals(coll.topics.get(maxInd).topicTag.substring(0,coll.topics.get(maxInd).topicTag.indexOf('-')))){
//            		numMovedCorrectly++;
//            		sumMovedCorrectly +=maxSim;
//            	}
//            	else{
//            		numMovedInorrectly++;
//            		sumMovedIncorrectly+=maxSim;
//            	}
////        			numClustersOk++;
//            	
//            }
//        }
//        System.out.println("Moved correctly = " + numMovedCorrectly + " avg= " + (sumMovedCorrectly/numMovedCorrectly) +
//        		"\nMoved incorrectly = " + numMovedInorrectly + " avg= " + (sumMovedIncorrectly/numMovedInorrectly));
//        System.out.println("There was " + numOfSmallTopics + " fake topics.");         
//        
//        System.out.println("# topics = " + coll.topics.size()); 
//        System.out.println(num_of_true + " true\n"+num_of_false + " false.");
//        
//        System.out.println("AvgSim = " + coll.getAvgSimilarity());

    }
    
}