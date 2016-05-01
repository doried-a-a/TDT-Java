package tfidf;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import tdt.ISimilatityMeasurable;
import tdt.LexicalWord;
import tdt.Word;
import vectorspace.AbstractVectorSpaceDocumentRepresentation;


public class TfidfVectorSpaceDocumentRepresentation extends vectorspace.AbstractVectorSpaceDocumentRepresentation{
    IIDFProvider idfProvider;
    
    public TfidfVectorSpaceDocumentRepresentation(IIDFProvider idfProvider,Date date){
        super(date);
        this.idfProvider = idfProvider;
    }
    
    public TfidfVectorSpaceDocumentRepresentation(HashMap<Word,Double> docVector, IIDFProvider idfProvider,Date date){
        super(docVector,date);
        this.idfProvider = idfProvider;
    }
    
    public TfidfVectorSpaceDocumentRepresentation(String docText , IIDFProvider idfProvider,Date date){
    	  super(date);
    	  String[] words = docText.split(" ");
          HashMap<Word,Double> freqs = new HashMap();
          // comuting words counts (frequencies)  [converting the document to vector space representation)
          for (String word:words){
              if(word.length()==0)
                  continue;
              Word w = new LexicalWord(word);
              freqs.put(w, freqs.getOrDefault(w, 0.0)+1);
          }
          
          for(Word w:freqs.keySet())
              super.addWord(w, freqs.get(w));
   
          @SuppressWarnings("unused")
		int ttt = this.getWordsOfDocument().size();
          
          this.idfProvider = idfProvider;
          
          
    }
    
    public TfidfVectorSpaceDocumentRepresentation getFilteredLightWords(int keepLimit,boolean debug){
    	
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
    	
    	return new  TfidfVectorSpaceDocumentRepresentation(newDocVector,  idfProvider, this.getDate());
    }
    

    
    // This should be overriden to give the tf.idf of the word, rather than log-freq which is stored in the vectors
    @Override
    protected double elementValueTransformer(Word w, Double actualValue) {
        
        // computing words log frequencies * idf(word) : 
        // log-freq(word) = {  0                    when count(word)=0
        //                     1+log(count(word))   when count(word)>0 }
        
        //return  (actualValue==0.0 ? (0.0) : (1.0 + Math.log(actualValue)) ) 
        //        * idfProvider.getInverseDocumentFrequency(w);
    	if(w.toString().equals("unsurpisngli")){
    		int aa=1;
    	}
        double tf = actualValue/(actualValue + 0.5 + 1.5*this.getOriginalSumOfValues()/idfProvider.getAverageDocumentLength());
        double idf = idfProvider.getInverseDocumentFrequency(w);
        
        double val = tf*idf;
        
        if(val>400){
        	int a=0;
        	System.out.println(w.toString() + " : " + val);
        }
        return val;
       
    }
    
   
    public double getSimilarity(ISimilatityMeasurable other) {

        if ( other instanceof AbstractVectorSpaceDocumentRepresentation == false)
            throw new RuntimeException("Only VectorSpaceDocuments can be mesaures to " +
                    "a VectorSpaceDocumentReresentation.");
        
        AbstractVectorSpaceDocumentRepresentation doc = (AbstractVectorSpaceDocumentRepresentation) other;
        
        double cosineSimilarity=0;
        
        //computing cosine the angle between the vectors : dot product / product of the two magnitudes
        // 1] computing scalar product:
        //   for eachcol.getDocumentCollection().getDocument(i1).getSimilarity(col.
        //   getDocumentCollection().getDocument(i2)) word in this document, compute the word weight in this doc * word weight
        //   in the other doc (or 0 if not exist) and add it to cosineSimilarity;
        
        double dotProd = 0;
        int wordCnt=0;
        for(Word w:doc.getWordsOfDocument()){
        	wordCnt++;
            double v1 = this.getValue(w) , v2 = doc.getValue(w);
            dotProd += (v1*v2);
        }
        
       // System.out.println("Magnitudes = " + this.getMagnitude() + "," + doc.getMagnitude());
         // 2] divide by the product of the two magnitudes
        cosineSimilarity =   dotProd / (this.getMagnitude() * doc.getMagnitude());
        
        //double days = 1 + Math.abs(Math.round((this.getDate().getTime() - doc.getDate().getTime()) / (double) 86400000));
       // double timeSimilarity = Math.log( 1 + 1.0/days) / Math.log(2.0); // for hours in [1 - inf ] => timeSimilarity in [1 - 0]
     //   System.out.println(days + " : "  + ( 1 + 1.0/days)+ " - " + Math.log(1+1.0/days));
        
        double days = Math.abs(Math.round((this.getDate().getTime() - doc.getDate().getTime()) / (double) 86400000));
        double timeSimilarity = Math.exp(-0.5*days);
        return 1 * cosineSimilarity ;// + 0.2*timeSimilarity;
        
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

