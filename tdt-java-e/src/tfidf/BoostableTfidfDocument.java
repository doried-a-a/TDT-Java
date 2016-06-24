package tfidf;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import tdt.LexicalWord;
import tdt.Word;


public class BoostableTfidfDocument extends TfidfVectorSpaceDocumentRepresentation {

	Set<Word> boostings ;
	double boost = 1;
	private String boostString;
	
	public void addBoostings(String str){
		String [] boosts = str.split(" ");
		for(String w : boosts){
			if (w.length()>0)
				boostings.add(new LexicalWord(w));
		}
	}
	
	public BoostableTfidfDocument(IIDFProvider idfProvider,Date date,String boostedString,double boostValue){
		super(idfProvider,date);
		boostings=new HashSet<Word>();
		this.addBoostings(boostedString);
		this.boost = boostValue;
		this.boostString=boostedString;
    }
	
	public BoostableTfidfDocument(HashMap<Word, Double> docVector, IIDFProvider idfProvider, Date date,String boostedString,double boostValue) {
		super(docVector, idfProvider, date);
		boostings=new HashSet<Word>();
		this.addBoostings(boostedString);
		this.boost = boostValue;
		this.boostString=boostedString;
	}
	
	public BoostableTfidfDocument(String docText , IIDFProvider idfProvider,Date date,String boostedString,double boostValue){
	  	super(docText,idfProvider,date);
	  	boostings=new HashSet<Word>();
	  	this.addBoostings(boostedString);
	  	this.boost = boostValue;
	  	this.boostString=boostedString;
	}
	
	 // This should be overriden to give the boosted value, rather than just value
    @Override
    protected double elementValueTransformer(Word w, Double actualValue) {
    	double value = super.elementValueTransformer(w, actualValue);
    	
    	if (boostings.contains(w)){
    	//	System.out.println("boosted " + w.toString() + " by " + boost );
    		return value*boost;
    	}
    	
        return value;
       
    }
    
    
    public BoostableTfidfDocument getFilteredLightWords(int keepLimit,boolean debug){
    	
    	Set<Word> set = this.getWordsOfDocument();
    	PriorityQueue<MyPair> p = new PriorityQueue<MyPair>();
    	
    	for(Word w:set)
    		p.add(new MyPair(w, this.getValue(w)));
    	
    	HashMap<Word,Double> newDocVector = new HashMap<Word, Double>();
    	double newSumOfValues=0;
    	
    	for(Word w : boostings){
    		double val = this.getActualValue(w);
    		if(val<0.000000001)
    			continue;
    		newSumOfValues+=val;
    		newDocVector.put(w, val);
    		keepLimit--;
    	}
    	
    	while(keepLimit-->0 && !p.isEmpty()){
    		Word w = p.poll().word;
    		if (boostings.contains(w))
    			continue;
    		double value = this.getActualValue(w);
    		if(debug)
    			System.out.println(keepLimit + " _ " + w.toString() + " : " + this.getValue(w));
    		newSumOfValues+=value;
    		newDocVector.put(w, value);
    	}
    	
    	return new  BoostableTfidfDocument(newDocVector,  idfProvider, this.getDate(),boostString,boost);
    }
	
	
	
	

}
