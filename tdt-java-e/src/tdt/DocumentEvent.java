/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tdt;

/**
 *
 * @author doried
 */
public class DocumentEvent {
    public enum EventType {DocumentAdded, DocumentDeatached , DocumentChanged};
    
    private EventType type;
    DocumentRepresentation doc;
    
    public DocumentEvent(EventType type, DocumentRepresentation doc){
        this.type = type;
        this.doc = doc;
    }
    
    public EventType getType(){
        return this.type;
    }
    
    public DocumentRepresentation getDocument(){
        if(doc !=null)
            return doc;
        else return null;
    }
}
