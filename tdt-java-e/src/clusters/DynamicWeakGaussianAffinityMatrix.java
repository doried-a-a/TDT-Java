package clusters;

import java.util.ArrayList;
import java.util.List;

import tfidf.TfidfVectorSpaceDocumentRepresentation;

public class DynamicWeakGaussianAffinityMatrix {
	
	private DynamicSimilarityMatix simMatrix;
	private ICosineSimilarityToDistanceConverter converter;
	private double [][] distance;
	private double [][] affinity;
	private List<Double> maxRowElement;
	private List<Double> minRowElement;
	private double [] sigma;
	
	
	public DynamicWeakGaussianAffinityMatrix(ICosineSimilarityToDistanceConverter cosineSimToDistanceConverter){
		this.converter=cosineSimToDistanceConverter;
		this.simMatrix = new DynamicSimilarityMatix();
		this.distance = new double[0][0];
		this.affinity = new double [0][0];
		this.maxRowElement = new ArrayList<Double>();
		this.minRowElement = new ArrayList<Double>();
		this.sigma = new double[0];
	}
	
	
	public void addStories(List<TfidfVectorSpaceDocumentRepresentation> stories){
		
		int nDocs = simMatrix.getNumberOfDocs();
		int newNDocs =  nDocs + stories.size();
		
		// expanding similarity matrix
		simMatrix.addStories(stories);
		double [][] newSimilarity = simMatrix.getSimilarityMatirx();
		
		// Distance matrix
		//---------------------------------------------------------------
		double [][] newDistance = new double[newNDocs][newNDocs];
		for(int i=0;i<nDocs;i++)
			for(int j=0;j<nDocs;j++)
				newDistance[i][j]=distance[i][j];
		
		// now, expanding distance matrix
		for(int i=nDocs;i<newNDocs;i++){
			// computing distance for the new stories
			for(int j=0;j<newNDocs;j++){
				newDistance[i][j] = converter.getDistance(newSimilarity[i][j]);
				newDistance[j][i] = newDistance[i][j];
			}
			
		}
		distance = newDistance;
		//----------------------------------------------------------------
		
		// if there was docs, the max and min of the last row is not initialized yet, so initialize it
		if(nDocs>0){
			maxRowElement.add(distance[nDocs-1][nDocs]);
			minRowElement.add(distance[nDocs-1][nDocs]);
		}	
		// this values were the average of the values above its row, so I'll re-initiate them
		if(nDocs>2){
			maxRowElement.set(nDocs-2,distance[nDocs-2][nDocs-1]);
			minRowElement.set(nDocs-2,distance[nDocs-2][nDocs-1]);
		}
		
		//now,updating maximum and minimum for the existing rows
		for(int i=0;i<nDocs;i++){
			double maxDistance = maxRowElement.get(i);
			double minDistance = minRowElement.get(i);
			for(int j=nDocs;j<newNDocs;j++){
				if(distance[i][j]>maxDistance)
					maxDistance = distance[i][j];
				if(distance[i][j]<minDistance)
					minDistance = distance[i][j];
			}
			maxRowElement.set(i, maxDistance);
			minRowElement.set(i, minDistance);
		}
		
		// now, computing maximum and minimum for the new rows
		// these rows are new, so they don't have previous maximum or minimum
		for(int i=nDocs;i<newNDocs-1;i++){
			//setting initial maximum and minimum for row i
			maxRowElement.add(distance[i][i+1]);
			minRowElement.add(distance[i][i+1]);
			
			double maxDistance = maxRowElement.get(i);
			double minDistance = minRowElement.get(i);
			
			for(int j=i+1;j<newNDocs;j++){
				if(distance[i][j]>maxDistance)
					maxDistance = distance[i][j];
				if(distance[i][j]<minDistance)
					minDistance = distance[i][j];
			}
			
			maxRowElement.set(i, maxDistance);
			minRowElement.set(i, minDistance);
		}
		
		double sum_max=0;
		double sum_min=0;
		for(int i=0;i<newNDocs-2;i++){
			sum_max+=maxRowElement.get(i);
			sum_min+=minRowElement.get(i);
		}
		
		if(newNDocs>2){
			maxRowElement.set(newNDocs-2,sum_max/(newNDocs-2));
			minRowElement.set(newNDocs-2,sum_min/(newNDocs-2));
		}	
		
		// now computing sigma vector
		//----------------------------------------------------------
		double [] sigma = new double[newNDocs-1];
		
		for(int i=0;i<newNDocs-1;i++)
    		sigma[i] = (maxRowElement.get(i)-minRowElement.get(i))/(2*Math.log(maxRowElement.get(i)/(minRowElement.get(i)+0.000001)+0.000001));
    	
		// and finally, computing affinity matrix
		//----------------------------------------------------------
		affinity = new double[newNDocs][newNDocs];
    	for(int i=0;i<newNDocs-1;i++){
    		affinity[i][i]=1;
    		for(int j=i+1;j<newNDocs;j++){
    			affinity[i][j] = Math.exp( -Math.pow(distance[i][j],2)/(2*sigma[i])  );
    			//else affinity[i][j] = Math.exp( -Math.pow(distance[i][j],2)/(2*Math.sqrt(sigma[i])*Math.sqrt(sigma[j]))  );
    			affinity[j][i] = affinity[i][j];
    		}
    	}
    	
    	nDocs = newNDocs;
    	
	}
	
	public double [][] getAffinityMatrix(){
		return this.affinity;
	}
	
	public double getAffinity(int i,int j){
		return this.affinity[i][j];
	}
	
	public double getSimilarity(int i,int j){
		return this.simMatrix.getSimilarity(i, j);
	}
	
	public double [][] getSimilarityMatrix(){
		return this.simMatrix.getSimilarityMatirx();
	}
	
	
	

}
