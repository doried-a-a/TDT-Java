package tfidf;


import java.sql.Date;
import java.util.HashMap;

import tdt.*;

public class TfidfCollection implements IDocumentEventListener{
    
    private DocumentCollection collection ;
    private IdfExternalProvider provider ;
    
    private boolean autoDFUpdate = true;
    
    public TfidfCollection(){
        this.collection = new DocumentCollection();
        this.provider = new IdfExternalProvider();
        this.collection.registerDocumentEventListener(this);
    }
    
    public TfidfCollection(IdfExternalProvider idfProvider, boolean enableAutoDFUpdate){
        this.collection = new DocumentCollection();
        this.provider = idfProvider;
        this.enableAutoDFUpdate(enableAutoDFUpdate);
        this.collection.registerDocumentEventListener(this);
    }
    
    
    /**
     * enables or disables automatic update of idf when documents added or removed
     */
    public void enableAutoDFUpdate(boolean isEnabled){
        this.autoDFUpdate = isEnabled;
    }
    
    /**
     * retrieves whether automatic update of idf is enabled or not
     */
    public boolean isAutoDFUpdateEnabled(){
        return autoDFUpdate;
    }
    
    
    
    public IdfExternalProvider getIdfExternalProvider(){
        return this.provider;
    }
    
    public DocumentCollection getDocumentCollection(){
        return this.collection;
    }
    
    
    
    /**
     * @param docStr the text of the document to be indexed
     * @return the index of the document in getDocumentCollection , -1 if failed
     */
    public int addDocument(String docStr,Date date){
       
        String[] words = docStr.split(" ");
        HashMap<Word,Double> freqs = new HashMap();
        
        // comuting words counts (frequencies)  [converting the document to vector space representation)
        for (String word:words){
            Word w = new LexicalWord(word);
            freqs.put(w, freqs.getOrDefault(w, 0.0)+1);
        }
        
        // wrapping the vector into a TfidfVectorSpaceDocumentRepresentation
        TfidfVectorSpaceDocumentRepresentation doc =
             new TfidfVectorSpaceDocumentRepresentation(freqs, provider,date);
        
        return addDocument(doc);
    }
    
    public int addDocument(TfidfVectorSpaceDocumentRepresentation doc){
        // adding the TfidfVectorSpaceDocumentRepresentation to the collection
        // (after collection change, it will notify this object that new document is added,
        // this object will then execute the (index) method which will update idf values
        if (doc.idfProvider != this.provider){
            System.err.println("Warning: IIDFProvider-s for collection and added document are not the same");
        }
        return this.getDocumentCollection().addDocument(doc);
    }
    
    public DocumentRepresentation removeDocument(int index){
        return this.getDocumentCollection().removeDocument(index);
    }
    
    public boolean removeDocument(DocumentRepresentation doc){
        return this.getDocumentCollection().removeDocument(doc);
    }

    
    public void indexDocument(DocumentRepresentation doc){
        if (doc instanceof TfidfVectorSpaceDocumentRepresentation == false)
            return;
        TfidfVectorSpaceDocumentRepresentation vector = (TfidfVectorSpaceDocumentRepresentation) doc;
        provider.indexDocument(vector);
    }
    
    public void deattachDocument(DocumentRepresentation doc){
        if (doc instanceof TfidfVectorSpaceDocumentRepresentation == false)
            return;
        TfidfVectorSpaceDocumentRepresentation vector = (TfidfVectorSpaceDocumentRepresentation) doc;
        provider.unindexDocument(vector);
    }
    
    
    public void registerDocumentEvent(DocumentEvent event) {
        if (autoDFUpdate == false)
            return;
        if (event.getType() == DocumentEvent.EventType.DocumentAdded){
            indexDocument(event.getDocument());
        }
        else if(event.getType()==DocumentEvent.EventType.DocumentDeatached){
            deattachDocument(event.getDocument());
        }
    } 
}
