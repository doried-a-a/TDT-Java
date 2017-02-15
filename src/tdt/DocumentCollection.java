
package tdt;

import java.util.ArrayList;
import java.util.List;


public class DocumentCollection {
    private List<DocumentRepresentation> docs ;
    private List<IDocumentEventListener> listeners;
    
    public DocumentCollection(){
        docs = new ArrayList();
        listeners = new ArrayList();
    }
    
    public void registerDocumentEventListener(IDocumentEventListener listener){
        listeners.add(listener);
    }
    
    public void removeDocumentEventListener(IDocumentEventListener listener){
        listeners.remove(listener);
    }
    
    public int addDocument(DocumentRepresentation doc){
        boolean added = docs.add(doc);
        if (added){
            notifyAllDocumentListeners(new DocumentEvent(DocumentEvent.EventType.DocumentAdded, doc));
            return docs.size()-1;
        }
        else return -1;
    }
    
    public boolean removeDocument(DocumentRepresentation doc) {
        boolean isRemoved = docs.remove(doc);
        if(isRemoved)
            notifyAllDocumentListeners(new DocumentEvent(DocumentEvent.EventType.DocumentDeatached, doc));
        return isRemoved;
    }
    
    public DocumentRepresentation removeDocument(int index) {
        DocumentRepresentation doc= docs.remove(index);
        if(doc != null)
            notifyAllDocumentListeners(new DocumentEvent(DocumentEvent.EventType.DocumentDeatached, doc));
        return doc;
    }
    
    public int getSize(){
        return docs.size();
    }
    
    public DocumentRepresentation getDocument(int index){
        return docs.get(index);
    }
    
    private void notifyAllDocumentListeners(DocumentEvent event){
        for(IDocumentEventListener lis:listeners)
            lis.registerDocumentEvent(event);
    }
    
}
