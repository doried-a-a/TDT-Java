package clusters;

public class LogCosineSimilarityToDistanceConverter implements ICosineSimilarityToDistanceConverter {

	public double getDistance(double similarity) {
		return Math.pow(-Math.log(1*similarity+0.0001),8);
		//return Math.pow((1-similarity),8);
		//return Math.acos(similarity)/Math.acos(0);
	}

}
