package tfidf;

import tdt.Word;

public class WeightedTerm implements Comparable<WeightedTerm> {

	private Word word;
	private double weight;
	
	public WeightedTerm(Word word,double value){
		this.word=word;
		this.weight=value;
	}
	
	public double getWeight(){
		return weight;
	}
	
	public Word getWord(){
		return word;
	}

	public int compareTo(WeightedTerm o) {
		return this.weight<o.weight?-1: (this.weight==o.weight?0:1) ;
	}
	
}
