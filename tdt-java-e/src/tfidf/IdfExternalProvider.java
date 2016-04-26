/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfidf;

import java.util.HashMap;
import tdt.LexicalWord;
import tdt.Word;

/**
 *
 * @author dorieda
 */

public class IdfExternalProvider implements IIDFProvider{
    private int documentsCount ;
    private long sumDocLengths;

    private HashMap<Word,Integer> docFreq ;
    
    public IdfExternalProvider(){
        docFreq = new HashMap();
    }
    
    public int getDocumentsCount() {
        return documentsCount;
    }

    public void setDocumentsCount(int documentsCount) {
        this.documentsCount = documentsCount;
    }
    
    public void increateDocumentsCount(){
        this.documentsCount++;
    }
    
    public void decreaseDocumentsCount(){
        this.documentsCount--;
    }
    
    public void indexLexicalDocument(String doc){
        String [] words = doc.split(" ");
        // Document frequency of each word in the new document should be increased by one
        int lngth=0;
        for(String w : words){
        	if(w.length()==0)
        		continue;
        	lngth++;
        	LexicalWord word = new LexicalWord(w);
            docFreq.put( word , docFreq.getOrDefault(word,0)+ 1);
        }
        setDocumentsCount(getDocumentsCount()+1);
        sumDocLengths+=lngth;
        
    }
    
    public void indexDocument(TfidfVectorSpaceDocumentRepresentation doc){
    	int lngth=0;
        for(Word w:doc.getWordsOfDocument()){
        	lngth+=(int)doc.getActualValue(w)+0.0001;
            docFreq.put(w, docFreq.getOrDefault(w, 0)+1);
        }
        setDocumentsCount(getDocumentsCount()+1);
        sumDocLengths+=lngth;
    }
    
    public void unindexLexicalDocument(String doc){
        String [] words = doc.split(" ");
        int lngth=0;
        // Document frequency of each word in the deleted document should be decreased by one
        for(String w : words){
        	if(w.length()==0)
        		continue;
        	lngth++;
        	LexicalWord word = new LexicalWord(w);
            docFreq.put( word , docFreq.get(word)- 1);
        }
        setDocumentsCount(getDocumentsCount()-1);
        sumDocLengths-=lngth;
    }
    
    public void unindexDocument(TfidfVectorSpaceDocumentRepresentation doc){
    	int lngth=0;
        for(Word w:doc.getWordsOfDocument()){
            docFreq.put(w , docFreq.get(w)-1);
            lngth+=(int)doc.getActualValue(w)+0.0001;
        }
        setDocumentsCount(getDocumentsCount()-1);
        sumDocLengths-=lngth;
    }
    
    
    
    public int getDf(Word w){
        return docFreq.getOrDefault(w, 0);
    }
    
    public double getIdf(Word w){
    	int a = getDocumentsCount();
    	int b = getDf(w);
    	
    	
        return  Math.log(((double)a)/(b));
    }

    
    public double getInverseDocumentFrequency(Word w) {
        return  getIdf(w);
    }

	public double getAverageDocumentLength() {
		return ((double)sumDocLengths)/documentsCount;
	}

	public int getTotalDocumentsCount() {
		return documentsCount;
	}

	public int getDocumentFrequency(Word w) {
		return getDf(w);
	}
    
    
    
    
}
