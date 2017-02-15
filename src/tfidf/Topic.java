
package tfidf;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import tdt.ISimilatityMeasurable;
import tdt.Word;

public class Topic implements ISimilatityMeasurable {
	
    public TfidfVectorSpaceDocumentRepresentation topicRepresentation = null;
    public TfidfVectorSpaceDocumentRepresentation cuttedTopicRepresentation = null;
    public IIDFProvider idfProvider;
    
    public List<String> storyTitles;
    public List<TfidfVectorSpaceDocumentRepresentation> stories ;
    public List<TfidfVectorSpaceDocumentRepresentation> adaptedStories ;
    
    private String topicTag;
    private double topicNormalizationFactor=-1;
    
   
    
    public String getTopicTag() {
		return topicTag;
	}

	public void setTopicTag(String topicTag) {
		this.topicTag = topicTag;
	}

	public int numOfAdaptedStories = 0;
    
    public Topic(IIDFProvider idfProvider , Date date){
        this.idfProvider = idfProvider;
        topicRepresentation = new TfidfVectorSpaceDocumentRepresentation(idfProvider,date);
        cuttedTopicRepresentation = new TfidfVectorSpaceDocumentRepresentation(idfProvider, date);
        stories = new ArrayList();
        storyTitles = new ArrayList();
        adaptedStories = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
    }
    
    /**
     * Updates term frequencies of this topic with frequencies obtained from the story.
     * strictly speaking, for each word w in story, increment tf(w|topic) by v, then divide by the 
     * numberOfAdaptedDocuments, where v is tf(w|story). This does not add the story to the topic. 
     * You still need to invoke addStoryToTopic to add it.
     * @param story the vector space representation of this story, as TfidfVectorSpaceDocumentRepresentation
     */
    public void adaptStoryIntoTopic(TfidfVectorSpaceDocumentRepresentation story ){
    	
    	Set s = story.getWordsOfDocument();
        for(Word w:story.getWordsOfDocument()){
        	double prevVal = topicRepresentation.getActualValue(w);
        	//double newVal = (this.numOfAdaptedStories*prevVal + story.getActualValue(w))/(numOfAdaptedStories+1.0);
        	double newVal = prevVal + story.getActualValue(w);
        	topicRepresentation.replaceWord(w, newVal);
        }
        
        cuttedTopicRepresentation = this.topicRepresentation.getFilteredLightWords(300, false);
        
        numOfAdaptedStories++;
    }

    /**
     * Adds a story to the topic without using it in updating the representation of this topic.
     * i.e: tf(word|topic) is not effected by this addition. To affect it, use adaptStoryIntoTopic
     * before/after calling this function
     * @param storyTitle an identifier with which the story will be identified (its just for you to know which stories is in this topic)
     * @param story the vector space representation of this story, as TfidfVectorSpaceDocumentRepresentation
     */
    public void addStoryToTopic(String storyTitle,TfidfVectorSpaceDocumentRepresentation story){
        stories.add(story);
        storyTitles.add(storyTitle);
    }
    
    public void addStoryToAdapted(TfidfVectorSpaceDocumentRepresentation story){
    	adaptedStories.add(story);
    }
    
    private double getSimilarityWithStory(TfidfVectorSpaceDocumentRepresentation other){
    	return cuttedTopicRepresentation.getSimilarity(other);
//    	double similaritySum = 0;
//    	for(TfidfVectorSpaceDocumentRepresentation story:stories){
//    		similaritySum += story.getSimilarity(other);
//    	}
//    	//System.out.println("Similarity sum is " + similaritySum);
//    	double avg = similaritySum/stories.size();
//    	return avg;
    }

	public double getSimilarity(ISimilatityMeasurable other) {
		if (other instanceof TfidfVectorSpaceDocumentRepresentation){
			return getSimilarityWithStory((TfidfVectorSpaceDocumentRepresentation)other);
		}
		else
		{
			throw new UnsupportedOperationException("Similairy measure of Topic and " + other.getClass().toString() + " is not supported yet.");
		}
		
	}
	
	 @SuppressWarnings("unused")
	public double getTopicNormalizationFactor(){

		 return 1;
//		 
//	    	if (topicNormalizationFactor ==-1 )
//	    		throw new RuntimeException("Topic normalization factor not initiated yet.");
//	    	else return topicNormalizationFactor;
	}
	 
	public double updateTopicNormalizationFactor(List<TfidfVectorSpaceDocumentRepresentation> storiesSeen){
		
		double sumSimilarity = 0;
		for(TfidfVectorSpaceDocumentRepresentation story:storiesSeen){
			sumSimilarity += this.getSimilarity(story);
		}
		double averageSimilarity = sumSimilarity/storiesSeen.size();
		topicNormalizationFactor=averageSimilarity;
		return averageSimilarity;
	}
}
