package clusters;

import java.util.ArrayList;
import java.util.List;

import tfidf.TfidfVectorSpaceDocumentRepresentation;

public class DynamicSimilarityMatix {

	private List<TfidfVectorSpaceDocumentRepresentation> allDocs;
	private double similarityMatrix[][];
	private int nDocs;
	
	public DynamicSimilarityMatix(){
		similarityMatrix = new double[0][0];
		allDocs = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
		nDocs=0;
	}
	
	
	public List<TfidfVectorSpaceDocumentRepresentation> getAllDocuments(){
		return this.allDocs;
	}
	
	public int getNumberOfDocs(){
		return this.nDocs;
	}
	
	public double [][] getSimilarityMatirx(){
		return this.similarityMatrix;
	}
	
	public double getSimilarity(int i,int j){
		return this.similarityMatrix[i][j];
	}
	
	
	
	public void addStories(List<TfidfVectorSpaceDocumentRepresentation> stories){
		int newNDocs = nDocs + stories.size();
		double [][] newSimilarity = new double[newNDocs][newNDocs];
		
		//copying previous similarity matrix to the new one		
		for(int i=0;i<nDocs;i++)
			for(int j=0;j<nDocs;j++)
				newSimilarity[i][j] = similarityMatrix[i][j];
		
		// new filling the rest of the new similarity matrix ( rows and columns nDocs-->newNDocs-1)
		int currentStoryNum = nDocs;
		for(TfidfVectorSpaceDocumentRepresentation doc : stories){
			allDocs.add(doc);
			for(int i=0;i<currentStoryNum;i++){
				TfidfVectorSpaceDocumentRepresentation otherDoc = allDocs.get(i);
				double similarity = doc.getSimilarity(otherDoc);
				newSimilarity[currentStoryNum][i] = similarity;
				newSimilarity[i][currentStoryNum] = similarity;
			}
			newSimilarity[currentStoryNum][currentStoryNum] = 1; 
			currentStoryNum++;
		}
		
		nDocs = newNDocs;
		similarityMatrix = newSimilarity;
	}
}
