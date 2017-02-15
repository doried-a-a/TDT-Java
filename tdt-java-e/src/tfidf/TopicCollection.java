
package tfidf;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;


// 0.31964234 avg : 0.91 adap , 0.446 yesno
// 0.28981903 avg:  0.88 adap , 0.416 yesno
public class TopicCollection{
    
    public static double thresholdAdaption = 0.64 ;//0.953; t=0.25 ,log/2 ,0.6  //0.91;//0.91;//1.5;
    public static double thresholdYesNo    =  0.60 ;//0.66;  t=0.25 ,log/2 ,0.6  //0.446;//0.456;//0.45;
    
    
    public IdfExternalProvider idfProvider;
    public TfidfCollection stories ;
    public List<Topic> topics ;
    
    public TopicCollection(){
        this.topics = new ArrayList();
        this.idfProvider = new IdfExternalProvider();
        this.stories = new TfidfCollection(idfProvider, true);
    }
    
    public TopicCollection(IdfExternalProvider idfProvider,boolean enableAutoDFUpdating){
        this.topics = new ArrayList();
        this.idfProvider = idfProvider;
        this.stories = new TfidfCollection(idfProvider, enableAutoDFUpdating);
    }
    
    /**
     * When set to true, adding/removing new story will cause document frequency of story words to be
     * updated. When set to false, it will not be.
     */
    public void enableAutomaticDFUpdating(boolean enable){
        this.stories.enableAutoDFUpdate(enable);
    }
    
    public void updateTfidfWithStory(String story){
        idfProvider.indexLexicalDocument(story);
    }
    
    double similaritySum = 0;
    double storyCnt = 0;
    public void addStory(String story,String title,Date date){
        System.out.println(date);
        
    	String storyWords[] = story.split(" ");
    	if(storyWords.length<20 || storyWords.length>1200)
    	{
    		System.out.println("HIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
    		if(storyWords.length<20)
    			return;
    	}
        TfidfVectorSpaceDocumentRepresentation doc = 
                new TfidfVectorSpaceDocumentRepresentation(story, idfProvider,date);
        int storyIndex = stories.addDocument(doc);
        
        
        int numOfTopics = topics.size();
        double bestMatch = 0;
        int    bestIndex = -1;
        
        for (int i=0 ; i<numOfTopics;i++){
//            TfidfVectorSpaceDocumentRepresentation topic =   (TfidfVectorSpaceDocumentRepresentation) 
//                    topics.get(i).topic;
        	
        	Topic topic = topics.get(i);
            
            double similarity = topic.getSimilarity(doc);
            
            
            if(similarity > 1.110001){
            	System.out.println("Stopped with similarity of " + similarity);
            	System.exit(0);
            }
            
            if(similarity > bestMatch){
                bestMatch = similarity;
                bestIndex = i;
            } 
        }
        
        similaritySum += bestMatch;
        storyCnt++;
        
        
        
        
        System.err.println(bestMatch+"");
        if (bestMatch > thresholdAdaption){
            Topic topic = topics.get(bestIndex);
            topic.adaptStoryIntoTopic(doc);
            topic.addStoryToTopic(title, doc);
            //System.out.println("Attatched and adapted story " + title + " to topic " + bestIndex + ":\n" + story + "\n-----------------------------------\n");
            
        }
        else if(bestMatch > thresholdYesNo){
            Topic topic = topics.get(bestIndex);
            topic.addStoryToTopic(title, doc);
            //System.out.println("Attatched story " + title + " to topic " + bestIndex + ":\n" + story + "\n-----------------------------------\n");
        }
        else{
            Topic new_t = new  Topic(idfProvider,date);
            new_t.setTopicTag(title);
            new_t.adaptStoryIntoTopic(doc);
            new_t.addStoryToTopic(title, doc);
            topics.add(new_t);
            //System.out.println("Started topic [" + (topics.size()-1) + "] with story " +  title + " :\n " + story  + "\n--------------------------------");
        }
    }
    
    public double getAvgSimilarity(){
    	return similaritySum / storyCnt;
    }
    
    

}
