/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package work;

import nlp.Stemmer;
import nlp.StopWordsRemover;

import java.io.Externalizable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.util.ArrayUtilities;

import clusters.DynamicSimilarityMatix;
import clusters.DynamicWeakGaussianAffinityMatrix;
import clusters.LogCosineSimilarityToDistanceConverter;
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
    public static int DRAW_CHART_BOTH = 3;
    public static String docVectorsOutputFile = "/home/doried/tdt/TDT/alaa/ZP-ALAAA/tdtDocVecs.txt";
    public static String docSimilarityMatrixOutputPath = "/home/doried/tdt/TDT/alaa/ZP-ALAAA/tdtDocSimMatrix.txt";
    public static String topicStorieLabelsOutputPath = "/home/doried/tdt/TDT/alaa/ZP-ALAAA/tdtTopicStoryLabels.txt";
    public static String pathToDETFile = "/home/doried/tdt/TDT/alaa/ZP-ALAAA/det.txt";
    public static String pathToAffinity = "/home/doried/tdt/TDT/alaa/ZP-ALAAA/tdtAffinity.txt";
    public static int ExportType = 1; // 0 for exporting docVectors , 1 for similarity matrix
    public static String filesPath = "/home/doried/tdt/TDT/alaa/ZP-ALAAA/files/";
    public static double BOOST_TITLE = 3.0;
    public static double detectionThreshold = 0.001;
    public static double trackingThreshold = 0.02;
    public static double trackingAdaptThreshould = 0.25;
    //public static String pathToAllStories = "/home/doried/tdt/test/modified/best-det-2/all/";
    public static String pathToAllStories = "/home/doried/tdt/test/modified/allStories/";
    public static int bufferSize=20;
    public static double [] thresholds = {	0.0000000000000000001,0.00000000000000001,0.000000000000001,0.00000000000001,0.0000000000001,0.000000000001,0.00000000001,0.0000000001,0.000000001,
    										0.00000001,0.0000001,0.000001,0.0001,0.001,0.002,0.004,0.008,0.012 ,0.016, 0.024, 0.032, 0.04, 0.05, 0.064, 0.08, 0.096,0.1,0.11,0.13,0.15,0.175, 0.2,
    										0.225,0.25,0.275,0.3,0.35,0.4,0.45,0.5,0.55,0.6,0.65,0.7,0.8,0.9,0.999999999999999,1};
    
    public static boolean DEBUG=false;
    public static boolean USE_AFFINITY = false;
    
    public static void main(String[] args) throws Exception{
    	testTracking(DRAW_CHART_BOTH);    		
    }
     
    public static void testTracking(int drawChart) throws Exception{

    	int numOfTrainingDocs = 4; 
    	int filterLimit=400;
        
        ArrayList<Double> [] thresholds = new ArrayList[2];
        ArrayList<Double> [] costs = new ArrayList[2];
        ArrayList<Double> [] fas = new ArrayList[2];
        ArrayList<Double> [] misses = new ArrayList[2];
        
        
        IdfExternalProvider idfProvider = new IdfExternalProvider();
        List<TfidfVectorSpaceDocumentRepresentation> docVectors = getDateSortedAndFilteredStoryVectorsAndBuildIdf(false,
        		idfProvider, filterLimit);
        
        if(DEBUG) System.out.println("Documents preparing is done..");

        String [] topics = {"3","5","10","12","15","20","21","22","23","25","27","31","37","39","40"};
        //String [] topics = {"3","5","10","12","15","20","21"};
        //String [] topics = {"22","23","25","27","31","37","39","40"};
        int TESTS=2; boolean [] use_affinity = {true,false};
        // We might perform two tests, with scaling and without
        for(int test=0;test<TESTS;test++)
        {
        	USE_AFFINITY = use_affinity[test];
        	
        	thresholds[test] = new ArrayList<Double>();
        	costs[test] = new ArrayList<Double>();
        	fas[test] = new ArrayList<Double>();
        	misses[test] = new ArrayList<Double>();
        	
	        int total_runs = topics.length*TDTJava.thresholds.length;
	        int currentRun=0;
	        //double step = 0.01; //for(threshold=0.00; threshold<0.5; threshold+=(threshold<0.15? step*0.4:step)){
	        
	        for(double threshold : TDTJava.thresholds){
	        	System.err.println("\nStarted threshold " + threshold  + "\n============================================\n");
	        	trackingThreshold = threshold;
	        	double overallFA =0, overallMisses=0; double overallCost = 0;
	        	int cntTakenTopic=0;
	        	for(String topicName : topics)
	            {
	            	Result result = testTopicTrackingDynamically(docVectors,idfProvider, topicName, numOfTrainingDocs, false, 2, 1); 
		        	if(result==null) // result == null when there is still no stories on this topic (don't forget, short stories are removed)
		        		continue;
		        	cntTakenTopic++;
		        	overallFA += result.getFalseAlarmRate();
		        	overallMisses += result.getMissRate();
		        	overallCost += result.getCost();
		        	
		        	System.out.println("Th=" + threshold + ",Topic=" + topicName + " - " + "Miss: " + result.numberOfMisses + "(" + result.getMissRate() + ")"
		        			+" - FA: " + result.numberOfFalseAlarms + "(" + result.getFalseAlarmRate() + ")" + " - COST: " + result.getCost()
		        			+ " Done: " + ((double)(++currentRun)/total_runs*100) + "%");
	            }
	        	thresholds[test].add(threshold);
	        	costs[test].add(overallCost/cntTakenTopic);
	        	fas[test].add(overallFA/cntTakenTopic*100.0);
	        	misses[test].add(overallMisses/cntTakenTopic*100.0);
	        }
	        
        }
        
        PrintWriter pr = new PrintWriter(pathToDETFile);
        for(int test=0;test<TESTS;test++){
        	String str_fas = "" , str_misses = "";
        	for(int i=0;i<fas[test].size();i++){
	        	str_fas += fas[test].get(i) + " ";
	        	str_misses += misses[test].get(i) + " ";
	        }
        	pr.println(str_fas.trim());
	        pr.println(str_misses.trim());
        }
        pr.close();
        
        if(drawChart == DRAW_CHART_COST || drawChart==DRAW_CHART_BOTH){
	        Drawer drawer = new Drawer("Costs","threshold","cost-value");
	        
	        drawer.draw(thresholds[0], costs[0],"cost-normalized");
	        drawer.draw(thresholds[0], fas[0],"false alarm-nolmalized");
	        drawer.draw(thresholds[0], misses[0],"miss-nolmalized");
	        
	        if(TESTS>1){
		        drawer.draw(thresholds[1], costs[1],"cost-no-normalization");
		        drawer.draw(thresholds[1], fas[1],"false alarm-no-normalization");
		        drawer.draw(thresholds[1], misses[1],"miss-no-normalization");
	        }
	        
        }
        if(drawChart == DRAW_CHART_DET || drawChart==DRAW_CHART_BOTH){
	        Drawer drawer = new Drawer("DET","FA prob.","miss prob.");
	        drawer.draw(fas[0], misses[0],"tradeoff-normalized");
	        if(TESTS>1)
	        	drawer.draw(fas[1], misses[1],"tradeoff-no-normalization");
        }
        
       
    }

    
    public static void exportDataForClustering(String outputFile,String topicStorieLabelsOutputPath) throws Exception{
    	
    	//those are text-processed
    	//String pathToAllStories = "/home/doried/tdt/test/modified/allStories/"; 
    	//String pathToAllStories = "/home/doried/tdt/test/modified/4-topics/all/"; 
    	String pathToAllStories = "/home/doried/tdt/test/modified/best/all/";
    	// Building IDF and documents' vectors
   
    	IdfExternalProvider idfProvider = new IdfExternalProvider();
    	
        List<File> allStories = DirectoryProcessor.getListOfFiles(pathToAllStories);
        List<TfidfVectorSpaceDocumentRepresentation> docVectors = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
        
        System.out.println("Building IDF and document vectors...");        
        for(File file:allStories){
        	// with stop-words removed
        	StoryFile f = DirectoryProcessor.readStoryFile(file,false,0);
            String fileContent = f.getStoryContent();
            TfidfVectorSpaceDocumentRepresentation doc = new BoostableTfidfDocument(
            		fileContent,idfProvider,f.getStoryDate(),
            		TextProcessor.processText(f.getStoryTitle()),BOOST_TITLE
            		);
            idfProvider.indexDocument(doc);
            docVectors.add(doc);
        }
        System.out.println("IDF and documents' vectors built.\nNow filtering light words and removing empty docs..");
        
        int filterLimit = 300; //350
        List<TfidfVectorSpaceDocumentRepresentation> newDocs = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
        List<String> topics = new ArrayList<String>();
        HashMap<String, ArrayList<String>> topic_story = new HashMap<String,ArrayList<String>>();
        
        int fileNumber=1; 
        
        for(int currentStory=0;currentStory<allStories.size();currentStory++){
        	StoryFile f = DirectoryProcessor.readStoryFile(allStories.get(currentStory) ,false,0);
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
	        int skipHeavy = (int) (0.00*numOfTerms);
	        int skipLight = (int) (0.2*numOfTerms);
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
    
    
    public static void testFirstStoryDetectionDynamically() throws Exception{
    	
    	int filterLimit = 400; boolean PRINT_DECESIONS=false;
    	
        IdfExternalProvider idfProvider = new IdfExternalProvider();
        List<TfidfVectorSpaceDocumentRepresentation> docVectors = getDateSortedAndFilteredStoryVectorsAndBuildIdf(false, idfProvider, filterLimit);
        System.out.println("Documents preparing is done..");

        //Now we'll simulate the story arriving procedure. First, we sorted stories by date and processed them.
        //Now, we will get the stories one by one, buffer them until we have for ex 20 stories, then we process them
        // together, then we wait for new stories to fill the buffer..
        
        // Cumulative vectors, vectors for stories arrived so far
        List<TfidfVectorSpaceDocumentRepresentation> c_docVectors = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
        
        // index of the last story which has been arrived
        int i=0;
        
        
        int total_actual_num_of_new =0; // actual total number of new stories
        int total_new = 0; // total number of novel stories according to our system.
        int total_misses = 0; // total number of new stories where our system missed.
        int total_false_alarms = 0;  // total number of non-new stories where our system false alarmed as new.
        String final_log = "";
        
        // Here, we use Weak Affinity Matrix with improved Gaussian core, but can by dynamically expanded to improve performance
        DynamicWeakGaussianAffinityMatrix dynamic_affinity = new DynamicWeakGaussianAffinityMatrix(
        		new LogCosineSimilarityToDistanceConverter());
        
        //DynamicSimilarityMatix dynamic_similarity = new DynamicSimilarityMatix();
        
        List<String> seenTopics = new ArrayList<String>();
        
        //while there still un-arrived stories, open a new buffer
        for(int batchNumber=0;i<docVectors.size();batchNumber++){
        	
        	System.out.println("\n============ Batch " + (batchNumber+1) + "=================");
        	
        	//buffer to put arriving stories in
        	List<TfidfVectorSpaceDocumentRepresentation> buffer = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();

        	for(int j=0;j<bufferSize && i<docVectors.size();j++){
        		buffer.add(docVectors.get(i));
        		c_docVectors.add(docVectors.get(i));
        		i++;
        	}
        	
        	// now, buffer is full,so process
        	// expand the affinity matrix with the new stories
        	dynamic_affinity.addStories(buffer);
	        double [][] affinity = dynamic_affinity.getAffinityMatrix();
	        
        	//dynamic_similarity.addStories(buffer);
        	//double [][] affinity = dynamic_similarity.getSimilarityMatirx();
        	
	        int num_of_new = 0;
	        int num_of_misses = 0;
	        int num_of_false_alarms = 0;
	        int actual_num_of_new=0;
	        
	        String log_output = "";
	        //the arriving story, should be compared to all previous stories
	        for(int k=0,c=i-bufferSize;k<buffer.size();k++,c++){
	        	
	        	// The currently-being-processed story
	        	TfidfVectorSpaceDocumentRepresentation currentDocument = buffer.get(k);
	        	// the actual topic of the current story (the label)
	        	String docTopic = DirectoryProcessor.extractTopicTag(currentDocument.getStoryFile().getStoryTag());
	        	
	        	// the actual decision (is the story actually novel?)
	        	boolean actual_isnew = (seenTopics.contains(docTopic)==false);
	        	
	        	if(actual_isnew){
	        		seenTopics.add(docTopic);
	        		actual_num_of_new++;
	        		total_actual_num_of_new++;
	        	}
	        	
	        	
	        	//Now,its time to see what will our system say about the story's novelty, is it new or not?
	        	
	        	boolean ourDecision = true;

	        	double maxSim=0;
	        	int bestMatch=-1;
	        	
	        	// comparing the story to all previously seen stories
	        	for(int j=0;j<c;j++){
	        		if(affinity[c][j] > maxSim){
	        			maxSim = affinity[c][j];
	        			bestMatch = j;
	        		}
	        	}
	        	
	        	if(maxSim>detectionThreshold){
		        	ourDecision = false;
	        	}
	        	
	        	if(ourDecision==true){
	        		num_of_new++; total_new++;
	        	}
	        	
	        	// Now, collecting some statistics
	        	//---------------------------------------------------------------------
	        	//in the case of false alarm
	        	if(ourDecision && actual_isnew==false){
	        		num_of_false_alarms++; total_false_alarms++;
	        		log_output += "False alarmed story " + currentDocument.getStoryFile().getStoryTag() + " on topic " + docTopic+"\n";
	        	}
	        	//in the case of a miss
	        	else if(ourDecision==false && actual_isnew==true){
	        		num_of_misses++; total_misses++;
	        		log_output += "Missed story " + currentDocument.getStoryFile().getStoryTag() + " on topic " + docTopic +"\n";
	        	}

	        	if(PRINT_DECESIONS || ourDecision!=actual_isnew){
		        	System.out.print(ourDecision + " --- " + actual_isnew);
		        	if(ourDecision != actual_isnew){
		        		System.out.print(" : " + currentDocument.getStoryFile().getStoryTag());
		        		if(bestMatch!=-1){
		        			System.out.print(" - best match " + c_docVectors.get(bestMatch).getStoryFile().getStoryTag() + " for " + maxSim);
		        		}
		        	}
		        	System.out.println();
	        	}
	        	//---------------------------------------------------------------------
	        }	        	
	        
	        // if there is some thing not normal (FA or misses)
	        if(num_of_false_alarms + num_of_misses > 0){
		        System.out.println("Count of new stories according to our system is " + num_of_new + " and actual number is " + actual_num_of_new);
		        System.out.println("Number of misses " + num_of_misses);
		        System.out.println("Number of false alarms " + num_of_false_alarms);
	        	System.out.print("Log:" + log_output);
	        	final_log += log_output +"----------------------\n";
	        }
	        else System.out.println("All decesions are true.");
	        
        }
        
        System.out.println("\n=========================\nFinal report:\n" + final_log);
        
        System.out.println("Count of new stories according to our system is " + total_new + " and actual number is " + total_actual_num_of_new);
        System.out.println("Number of misses " + total_misses);
        System.out.println("Number of false alarms " + total_false_alarms);
        
        
        
    }
    
    
    public static Result testTopicTrackingDynamically(List<TfidfVectorSpaceDocumentRepresentation> docVectors,IdfExternalProvider idfProvider,String targetTopicTag, int numOfTrainingStories , boolean shuffleTrainingStories 
    		,double C_miss, double C_false_alarm) throws Exception{
    	
    	boolean PRINT_DECESIONS=false;
    	
        
        //Now we'll simulate the story arriving procedure. First, we sorted stories by date and processed them.
        //Now, we will get the stories one by one, buffer them until we have for ex 20 stories, then we process them
        // together, then we wait for new stories to fill the buffer..
        
        //BUT first, we should put training stories first in the docVectors

        // finding all the stories belonging to the target topic
        List<TfidfVectorSpaceDocumentRepresentation> all_topicStories = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
        for(TfidfVectorSpaceDocumentRepresentation story:docVectors){
        	if(DirectoryProcessor.extractTopicTag(story.getStoryFile().getStoryTag()).equals(targetTopicTag))
        		all_topicStories.add(story);
        }
        
        if(all_topicStories.size()==0) throw new Exception("Target topic has no stoires in the dataset.");
        
        if(shuffleTrainingStories)
        	Collections.shuffle(all_topicStories);
        
        // now we want to add some stories from the topic stories to the training stories.
        List<TfidfVectorSpaceDocumentRepresentation> trainingStories = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
        for(int j=0;j<Math.min(numOfTrainingStories,all_topicStories.size());j++)
        	trainingStories.add(all_topicStories.get(j));

        String usedTrainingStories = "";
        for(int i=0;i<trainingStories.size();i++)
        	usedTrainingStories += trainingStories.get(i).getStoryFile().getStoryTag() + " ";
        
        if(DEBUG) System.out.println("Picked training stories : " + usedTrainingStories.trim());
        
        //Now, moving training stories to the beginning of docVectors
        docVectors = rearrangeDocVectors(docVectors, trainingStories);

        // now, we have training stories ready, and each training story has the method getIndex, which gives us its index in docVectors
        
        Topic trackedTopic = new Topic(idfProvider, new Date());
        for(TfidfVectorSpaceDocumentRepresentation story:trainingStories){
        	trackedTopic.addStoryToTopic(story.getStoryFile().getStoryTag(), story);
        	trackedTopic.addStoryToAdapted(story);
        }
        
        // Cumulative vectors, vectors for stories arrived so far
        List<TfidfVectorSpaceDocumentRepresentation> c_docVectors = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
        
        // index of the last story which has been arrived
        int i=0;
                
        int total_actual_yes =0; // actual number of stories that are on topic
        int total_yes = 0; //  number of stories that are on topic according to our system.
        int total_misses = 0; // total number of on-topic stories our system missed.
        int total_false_alarms = 0;  // total number off-topic stories where our system false alarmed.
        String final_log = "";
        
        // Here, we use Weak Affinity Matrix with improved Gaussian core, but can by dynamically expanded to improve performance
        DynamicWeakGaussianAffinityMatrix dynamic_affinity = new DynamicWeakGaussianAffinityMatrix(
        		new LogCosineSimilarityToDistanceConverter());
        
        
        //while there still un-arrived stories, open a new buffer
        for(int batchNumber=0;i<docVectors.size();batchNumber++){
        	
        	if(DEBUG) System.out.println("\n============ Batch " + (batchNumber+1) + "=================");
        	
        	//buffer to put arriving stories in
        	List<TfidfVectorSpaceDocumentRepresentation> buffer = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();

        	for(int j=0;j<bufferSize && i<docVectors.size();j++){
        		buffer.add(docVectors.get(i));
        		c_docVectors.add(docVectors.get(i));
        		i++;
        	}
        	
        	// now, buffer is full,so process
        	// expand the affinity matrix with the new stories
        	dynamic_affinity.addStories(buffer);
	       // double [][] affinity = dynamic_affinity.getAffinityMatrix();
        	double [][] affinity = null;
        	
        	if (USE_AFFINITY)
        		affinity = dynamic_affinity.getAffinityMatrix();
        	else
        		affinity = dynamic_affinity.getSimilarityMatrix();
	        
	        int num_of_yes = 0;
	        int num_of_misses = 0;
	        int num_of_false_alarms = 0;
	        int actual_num_of_yes=0;
	        
	        String log_output = "";
	        
	        for(int k=0,c=i-bufferSize;k<buffer.size();k++,c++){
	        	
	        	// The currently-being-processed story
	        	TfidfVectorSpaceDocumentRepresentation currentDocument = buffer.get(k);
	        	
	        	// the actual decision (is the story is actually on-topic?)
	        	boolean actual_is_on_topic = (all_topicStories.contains(currentDocument));
	        	
	        	if(actual_is_on_topic){
	        		actual_num_of_yes++; total_actual_yes++;
	        	}
	        	
	        	//Now,its time to see what will our system say about the story, is it on-topic or off-topic?
	        	
	        	double sum_similarity=0;
	        	
	        	// comparing the story to all previous stories in the topic
	        	for(int j=0;j<trackedTopic.stories.size();j++){
	        		int topic_story_index = trackedTopic.stories.get(j).getIndex();
	        		sum_similarity += affinity[topic_story_index][c];
	        	}
	        	
	        	double average_similarity = sum_similarity/trackedTopic.stories.size();
	        	
	        	boolean ourDecision = false;
	        	if(average_similarity>=trackingThreshold){
		        	ourDecision = true;
		        	//trackedTopic.addStoryToTopic(currentDocument.getStoryFile().getStoryTag(), currentDocument);
		        	if(average_similarity > trackingAdaptThreshould){
		        		trackedTopic.addStoryToAdapted(currentDocument);
		        		log_output += "Adapted story " + currentDocument.getStoryFile().getStoryTag() + "\n";
		        	}
	        	}
	        	
	        	if(ourDecision==true){
	        		num_of_yes++; total_yes++;
	        	}
	        	
	        	// Now, collecting some statistics
	        	//---------------------------------------------------------------------
	        	//in the case of false alarm
	        	if(ourDecision && actual_is_on_topic==false){
	        		num_of_false_alarms++; total_false_alarms++;
	        		log_output += "False alarmed story " + currentDocument.getStoryFile().getStoryTag() + " for " + average_similarity + "\n";
	        	}
	        	
	        	//in the case of a miss
	        	else if(ourDecision==false && actual_is_on_topic==true){
	        		num_of_misses++; total_misses++;
	        		log_output += "Missed story " + currentDocument.getStoryFile().getStoryTag() + " for " + average_similarity + "\n";;
	        	}

	        	if(DEBUG && (PRINT_DECESIONS || ourDecision!=actual_is_on_topic)){
		        	System.out.print(ourDecision + " --- " + actual_is_on_topic);
		        	if(ourDecision != actual_is_on_topic){
		        		System.out.print(" : " + currentDocument.getStoryFile().getStoryTag());
		        	}
		        	System.out.println();
	        	}
	        	//---------------------------------------------------------------------
	        }	        	
	        
	        // if there is some thing not normal (FA or misses)
	        if( DEBUG && num_of_false_alarms + num_of_misses > 0){
		        System.out.println("Count of on-topic stories according to our system is " + num_of_yes + " and actual number is " + actual_num_of_yes);
		        System.out.println("Number of misses " + num_of_misses);
		        System.out.println("Number of false alarms " + num_of_false_alarms);
	        	System.out.print("Log:" + log_output);
	        	final_log += log_output +"----------------------\n";
	        }
	        else if(DEBUG) System.out.println("All decesions are true.");
	        
        }
        
        if(DEBUG){
	        System.out.println("\n=========================\nFinal report:\n" + final_log);
	        System.out.println("Count of on-topic stories according to our system is " + total_yes + " and actual number is " + total_actual_yes);
	        System.out.println("Number of misses " + total_misses);
	        System.out.println("Number of false alarms " + total_false_alarms);
        }
        Result result = new Result(total_misses,total_false_alarms,total_actual_yes,docVectors.size());
        result.setCost( 
        		C_miss*(result.getMissRate())*result.getPriorYesRate() + C_false_alarm*result.getFalseAlarmRate()*result.getPriorNoRate()
        		);
        double normalize = Math.min(C_miss*result.getPriorYesRate(), C_false_alarm*result.getPriorNoRate());
        result.setCost(result.getCost()/normalize);
        return result;       	
    }    
    
    public static Result testTopicTrackingNonDynamically(List<TfidfVectorSpaceDocumentRepresentation> docVectors,IdfExternalProvider idfProvider,  String targetTopicTag, int numOfTrainingStories , boolean shuffleTrainingStories 
    		,double C_miss, double C_false_alarm) throws Exception{
    	
    	boolean PRINT_DECESIONS=false;
    	
        // finding all the stories belonging to the target topic
        List<TfidfVectorSpaceDocumentRepresentation> all_topicStories = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
        for(TfidfVectorSpaceDocumentRepresentation story:docVectors){
        	if(DirectoryProcessor.extractTopicTag(story.getStoryFile().getStoryTag()).equals(targetTopicTag))
        		all_topicStories.add(story);
        }
        if(all_topicStories.size()==0) throw new Exception("Target topic has no stoires in the dataset.");
        
        if(shuffleTrainingStories)
        	Collections.shuffle(all_topicStories);
        
        // now we want to add some stories from the topic stories to the training stories.
        List<TfidfVectorSpaceDocumentRepresentation> trainingStories = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
        for(int j=0;j<Math.min(numOfTrainingStories,all_topicStories.size());j++)
        	trainingStories.add(all_topicStories.get(j));

        String usedTrainingStories = "";
        for(int i=0;i<trainingStories.size();i++)
        	usedTrainingStories += trainingStories.get(i).getStoryFile().getStoryTag() + " ";
        
        System.out.println("Picked training stories : " + usedTrainingStories.trim());
        
        
        // now, we have training stories ready, and each training story has the method getIndex, which gives us its index in docVectors        
        Topic trackedTopic = new Topic(idfProvider, new Date());
        for(TfidfVectorSpaceDocumentRepresentation story:trainingStories){
        	trackedTopic.addStoryToTopic(story.getStoryFile().getStoryTag(), story);
        	trackedTopic.addStoryToAdapted(story);
        }
                
                
        int total_actual_yes =0; // actual number of stories that are on topic
        int total_yes = 0; //  number of stories that are on topic according to our system.
        int total_misses = 0; // total number of on-topic stories our system missed.
        int total_false_alarms = 0;  // total number off-topic stories where our system false alarmed.
        String final_log = "";
        
        double [][] affinity = getAffinityMatrix(docVectors);
        
        String log_output = "";
        
        for(int k=0;k<docVectors.size();k++){
        	
        	// The currently-being-processed story
        	TfidfVectorSpaceDocumentRepresentation currentDocument = docVectors.get(k);
        	
        	// the actual decision (is the story is actually on-topic?)
        	boolean actual_is_on_topic = (all_topicStories.contains(currentDocument));
        	
        	if(actual_is_on_topic){
        		total_actual_yes++;
        	}
        	
        	//Now,its time to see what will our system say about the story, is it on-topic or off-topic?
        	
        	double sum_similarity=0;
        	
        	// comparing the story to all previous stories in the topic
        	for(int j=0;j<trackedTopic.stories.size();j++){
        		int topic_story_index = trackedTopic.stories.get(j).getIndex();
        		sum_similarity += affinity[topic_story_index][k];
        	}
        	
        	double average_similarity = sum_similarity/trackedTopic.stories.size();
        	
        	boolean ourDecision = false;
        	if(average_similarity>=trackingThreshold){
	        	ourDecision = true;
	        	//trackedTopic.addStoryToTopic(currentDocument.getStoryFile().getStoryTag(), currentDocument);
	        	if(average_similarity > trackingAdaptThreshould){
	        		trackedTopic.addStoryToAdapted(currentDocument);
	        		log_output += "Adapted story " + currentDocument.getStoryFile().getStoryTag() + "\n";
	        	}
        	}
        	
        	if(ourDecision==true){
        		total_yes++;
        	}
        	
        	// Now, collecting some statistics
        	//---------------------------------------------------------------------
        	//in the case of false alarm
        	if(ourDecision && actual_is_on_topic==false){
        		total_false_alarms++;
        		log_output += "False alarmed story " + currentDocument.getStoryFile().getStoryTag() + " for " + average_similarity + "\n";
        	}
        	
        	//in the case of a miss
        	else if(ourDecision==false && actual_is_on_topic==true){
        		total_misses++;
        		log_output += "Missed story " + currentDocument.getStoryFile().getStoryTag() + " for " + average_similarity + "\n";;
        	}

        	if(PRINT_DECESIONS || ourDecision!=actual_is_on_topic){
	        	System.out.print(ourDecision + " --- " + actual_is_on_topic);
	        	if(ourDecision != actual_is_on_topic){
	        		System.out.print(" : " + currentDocument.getStoryFile().getStoryTag());
	        	}
	        	System.out.println();
        	}
        	//---------------------------------------------------------------------
        }	        	
        
        // if there is some thing not normal (FA or misses)
        if(total_misses + total_false_alarms > 0){
	        System.out.println("Count of on-topic stories according to our system is " + total_yes + " and actual number is " + total_actual_yes);
	        System.out.println("Number of misses " + total_misses);
	        System.out.println("Number of false alarms " + total_false_alarms);
        	System.out.print("Log:" + log_output);
        	final_log += log_output +"----------------------\n";
        }
        else System.out.println("All decesions are true.");
	        
                
        Result result = new Result(total_misses,total_false_alarms,total_actual_yes,docVectors.size());
        result.setCost( 
        		C_miss*(result.getMissRate())*result.getPriorYesRate() + C_false_alarm*result.getFalseAlarmRate()*result.getPriorNoRate()
        		);
        double normalize = Math.min(C_miss*result.getPriorYesRate(), C_false_alarm*result.getPriorNoRate());
        result.setCost(result.getCost()/normalize);
        return result;        	
    }
   
    public static double[][] getAffinityMatrix(List<TfidfVectorSpaceDocumentRepresentation> docs){
    	int nDocs = docs.size();
    	double [][] affinity = new double[nDocs][nDocs];
    	double [][] similarity = new double[nDocs][nDocs];
    	double [][] distance   = new double[nDocs][nDocs];
    	double []   sigma2	   = new double[nDocs];
    	
    	for(int i=0;i<nDocs;i++){
    		similarity[i][i]=1.0;
    		for(int j=i+1;j<nDocs;j++){
    			double sim = docs.get(i).getSimilarity(docs.get(j));
    			similarity[i][j] = sim;
    			similarity[j][i] = sim;
    		}
    	}
    	
    	for(int i=0;i<nDocs;i++)
    		for(int j=i;j<nDocs;j++)
    		{
    			distance[i][j] = Math.pow(-Math.log(similarity[i][j]+0.0001),8);
    			distance[j][i] = distance[i][j];
    		}
    	
    	double max_i[] = new double[nDocs-1];
    	double min_i[] = new double[nDocs-1];
    	
    	double sum_max = 0; double sum_min=0;
    	
    	for(int i=0;i<nDocs-2;i++)
    	{
    		max_i[i] = distance[i][i+1];
    		min_i[i] = distance[i][i+1];
    		
    		// computing max and min distance for row i
    		for(int j=i+1;j<nDocs;j++)
    		{
    			if(distance[i][j]>max_i[i]) max_i[i] = distance[i][j];
    			if(distance[i][j]<min_i[i]) min_i[i] = distance[i][j];
    		}
    		sum_max += max_i[i];
    		sum_min += min_i[i];
    	}
    	
    	// max and min of the before-last row are calculated as averages
    	max_i[nDocs-2] = sum_max/(nDocs-2);
    	min_i[nDocs-2] = sum_min/(nDocs-2);
    	
    	for(int i=0;i<nDocs-1;i++)
    		sigma2[i] = (max_i[i]-min_i[i])/(2*Math.log(max_i[i]/(min_i[i]+0.000001)+0.000001));
    	
    	for(int i=0;i<nDocs-1;i++)
    		for(int j=i+1;j<nDocs;j++){
    			affinity[i][j] = Math.exp( -Math.pow(distance[i][j],2)/(2*sigma2[i])  );
    			//affinity[i][j] = Math.exp( -Math.pow(distance[i][j],2)/(2*Math.sqrt(sigma2[i])*Math.sqrt(sigma2[j]))  );
    			affinity[j][i] = affinity[i][j];
    		}
    	
    	
    	
    	try{
    		writeArrayToFile(affinity, pathToAffinity);
    	}
    	catch(Exception ee){};
    	
    	return affinity;
    }
    
    public static void writeArrayToFile(double[][] a,String filePath) throws FileNotFoundException{
    	PrintWriter pr = new PrintWriter(new File(filePath));
    	
    	for(int i=0;i<a.length;i++){
    		String line = "";
    		for(int j=0;j<a[i].length;j++)
    			line += a[i][j] + " ";
    		pr.println(line.trim());
    	}
    	pr.close();
    }
    
    public static List<StoryFile> getStoryFilesSortedByDate(boolean language_process) throws Exception{
    	List<StoryFile> storyFiles = new ArrayList<StoryFile>();
    	// Building IDF and documents' vectors
        List<File> allStories = DirectoryProcessor.getListOfFiles(pathToAllStories);
        if(DEBUG)
        	System.out.println("Reading stories and sorting by date...");
        
        for(File file:allStories){
        	// with stop-words removed
        	StoryFile f = DirectoryProcessor.readStoryFile(file,language_process,0);
        	storyFiles.add(f);
        }
        Collections.sort(storyFiles);
        return storyFiles;
    }
    
    /**
     * Returns List of vectors representing each document, with the vector.getStoryFile set.
     * Also, accepts an empty idfProvider and fills it (adds the vector to he idfProvider)
     * @param language_process True to language-process the stories
     * @param idfProvider a handle to an empty idfProvider, the method fills it with the returned vectors.
     * @return List of document vectors, sorted by date ASC.
     * @throws Exception
     */
    public static List<TfidfVectorSpaceDocumentRepresentation> getDateSortedStoryVectorsAndBuildIdf(boolean language_process,IdfExternalProvider idfProvider) throws Exception{
    	
    	if(idfProvider.getTotalDocumentsCount()>0)
    		throw new Exception("Provided idfProvider is not empty!");
    	
    	List<StoryFile> storyFiles = getStoryFilesSortedByDate(language_process);
    	List<TfidfVectorSpaceDocumentRepresentation> docVectors = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
    	if(DEBUG)
    		System.out.println("Building document vectors and idfProvider...");
    	int index=0;
        for(StoryFile f:storyFiles)
        {
            String fileContent = f.getStoryContent();
            TfidfVectorSpaceDocumentRepresentation doc = new BoostableTfidfDocument(
            		fileContent,idfProvider,f.getStoryDate(),
            		TextProcessor.processText(f.getStoryTitle()),BOOST_TITLE
            		);
            doc.setStoryFile(f);
            doc.setIndex(index++);
            idfProvider.indexDocument(doc);
            docVectors.add(doc);
        }
        
        return docVectors;
    	
    }
   
    
    /**
    * Returns List of vectors representing each document, with the vector.getStoryFile set.
    * These vectors are then filtered with the given limit, and then empty vectors are removed from the list.
    * Also, accepts an empty idfProvider and fills it (adds the vector to he idfProvider)
    * @param language_process True to language-process the stories
    * @param idfProvider a handle to an empty idfProvider, the method fills it with the returned vectors.
    * @param filterLimit represents the number of terms to keep in each vector
    * @return List of document vectors,filtered, sorted by date ASC, with empty vectors removed.
    * @throws Exception
    */
	public static List<TfidfVectorSpaceDocumentRepresentation> getDateSortedAndFilteredStoryVectorsAndBuildIdf(
			boolean language_process, IdfExternalProvider idfProvider, int filterLimit) throws Exception {

		List<TfidfVectorSpaceDocumentRepresentation> docVectors = getDateSortedStoryVectorsAndBuildIdf(
				language_process, idfProvider);
		if(DEBUG)
			System.out.println("Filtering light words and removing empty docs..");
		
		List<TfidfVectorSpaceDocumentRepresentation> newDocs = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
		
		int index=0;
		for (int currentStory = 0; currentStory < docVectors.size(); currentStory++) {
			TfidfVectorSpaceDocumentRepresentation doc = docVectors
					.get(currentStory);
			
			doc.filterLightWords(filterLimit, false);
			if (doc.getWordsOfDocument().size() == 0)
				continue;
			newDocs.add(doc);
			doc.setIndex(index++);
		}
		
		return newDocs;
	}
	
	/**
	 * This method resorts the doc vectors, just putting the second parameter in the begining of the list, then adding the rest of elements
	 * @throws Exception 
	 */
	public static List<TfidfVectorSpaceDocumentRepresentation> rearrangeDocVectors(List<TfidfVectorSpaceDocumentRepresentation> docVectors,
			List<TfidfVectorSpaceDocumentRepresentation> vecsToPutFirst) throws Exception{
		
		List<TfidfVectorSpaceDocumentRepresentation> newList = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
		
		int index=0;
		// for each story in the second list, check if it exist in the first list, and if so, append it to the new list
		for(TfidfVectorSpaceDocumentRepresentation doc:vecsToPutFirst){
			if(!docVectors.contains(doc))
				throw new Exception("No all documents exist in the docVectors list");
			newList.add(doc);
			doc.setIndex(index++);
		}
		
		for(TfidfVectorSpaceDocumentRepresentation doc:docVectors){
			if(vecsToPutFirst.contains(doc))
				continue;
			newList.add(doc);
			doc.setIndex(index++);
		}
		
		return newList;
	}
   
}
