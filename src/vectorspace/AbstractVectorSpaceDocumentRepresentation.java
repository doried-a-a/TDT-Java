
package vectorspace;


import java.util.Date;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;

import tdt.*;

/**
 * This is abstract class, which has an abstract method called elementValueTransformer. 
 * Element value transformer may apply a transform to the actual value of the word in the
 * vector before using it, for example, the vector may store raw tf(word), but when requested,
 * we want to return logarithmic tf*ifd of the word, here we have to override elementValueTransformer
 * and let it return something else like (logarithmic tf using actualValue) *  idf (word)
 * @author doried
 */
public abstract class AbstractVectorSpaceDocumentRepresentation extends DocumentRepresentation {

    protected HashMap<Word,Double> documentVector;
    protected double sumOfValues = 0;
    protected double originalSumOfValues=0;
    
    public AbstractVectorSpaceDocumentRepresentation(Date date){
        documentVector = new HashMap();
        // initialize magnitude to be computed when requested
        magnitude = -1;
        setDate(date);
    }
    
    public AbstractVectorSpaceDocumentRepresentation(HashMap<Word,Double> docVector,Date date){
        documentVector = docVector;
        sumOfValues = 0;
        for (Word w : docVector.keySet())
            sumOfValues += docVector.get(w);
        originalSumOfValues = sumOfValues;
        
        // initialize magnitude to be computed when requested
        magnitude = -1;
        setDate(date);
    }
    

	public Set<Word> getWordsOfDocument(){
    	
    	int a1 = documentVector.entrySet().size();
        int a2 = documentVector.keySet().size();
        return documentVector.keySet();
    }
    
    public  double  getSumOfValues(){
        return sumOfValues;
    }
    
    public double getOriginalSumOfValues(){
    	return originalSumOfValues;
    }
    
    public void addWord(Word w,double value){
        documentVector.put(w, value);
        sumOfValues += value;
        originalSumOfValues+=value;
        // reinitialize magnitude to be computed when requested
        magnitude = -1;
    }
    
    /**
     * This replaces the value of a word by another one, keeping a correct value in (sumOfValues). Use it instead of
     *  addWord if the word may exist, because addWord will increment (sumOfValues) by the new value. However, if you
     *  are sure that the word does not exit, then addWord is more efficient.
     */
    public void replaceWord(Word w,double newValue){
    	double oldValue = documentVector.getOrDefault(w,0.0);
        documentVector.put(w, newValue);
        sumOfValues += newValue - oldValue;
        originalSumOfValues += newValue - oldValue;
        // reinitialize magnitude to be computed when requested
        magnitude = -1;
    }
    
    
    
    
    //TODO if we made a document change listener, then consider this method
    public void addOrIncrementWord(Word w , double value){
        documentVector.put(w, documentVector.getOrDefault(w, 0.0)+value);
        sumOfValues+=value;
        originalSumOfValues+=value;
        magnitude = -1;
    }
    
    public void removeWord(Word w){
        sumOfValues -= documentVector.getOrDefault(w,0.0);
        originalSumOfValues -= documentVector.getOrDefault(w,0.0);
        documentVector.remove(w);
        // reinitialize magnitude to be computed when requested
        magnitude = -1;
    }
    
    public double getValue(Word w){
        double val = elementValueTransformer( w , this.documentVector.getOrDefault(w,0.0) );
      
        return val;
    }
    
    public double getActualValue(Word w){
        return this.documentVector.getOrDefault(w,0.0);
    }
    
    //Element value transformer may apply a transform to the value of the word, for example
    // the vector may store tf(word), but when requested, we want to return tf*ifd of the word,
    // here we have to override elementValueTransformer and let it return log_freq(actualValue) * idf (word)
    
    protected abstract double elementValueTransformer(Word w , Double actualValue );
    
    double magnitude=-1;
    
    public double getMagnitude(){
        
        if (magnitude != -1)
            return magnitude;
        
        magnitude =  0;

        for(Word w:documentVector.keySet())
            magnitude += Math.pow( this.getValue(w) , 2.0);
        
        magnitude =  Math.sqrt(magnitude);
        
        return magnitude;
    } 
    
    public void filterLightWords(int keepLimit,boolean debug){
    	
    	Set<Word> set = this.getWordsOfDocument();
    	
    	PriorityQueue<MyPair> p = new PriorityQueue<MyPair>();
    	for(Word w:set)
    		p.add(new MyPair(w, this.getValue(w)));
    	
    	HashMap<Word,Double> newDocVector = new HashMap<Word, Double>();
    	double newSumOfValues=0;
    	
    	while(keepLimit-->0 && !p.isEmpty()){
    		Word w = p.poll().word;
    		double value = this.getActualValue(w);
    		if(debug)
    			System.out.println(keepLimit + " _ " + w.toString() + " : " + this.getValue(w));
    		newSumOfValues+=value;
    		newDocVector.put(w, value);
    	}
    	magnitude = -1;
    	this.documentVector = newDocVector;
    	this.sumOfValues = newSumOfValues;
    }
    
    
    public void printWords(){
    	Set<Word> set = this.getWordsOfDocument();
    	for(Word w:set)
    		System.out.println(w.toString() + " : " + this.getValue(w));
    }
     
   
    
}

class MyPair implements Comparable<MyPair>{
	Word word;
	Double value;
	public MyPair(Word w,Double v){
		word=w;
		value=v;
	}
	
	public int compareTo(MyPair o) {
		return -this.value.compareTo(o.value);
	}
	
}
