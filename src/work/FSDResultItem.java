
package work;

import tfidf.TfidfVectorSpaceDocumentRepresentation;


public class FSDResultItem {
    private boolean actualDecesion;
    private TfidfVectorSpaceDocumentRepresentation document;
    
    public boolean isActualDecesion() {
        return actualDecesion;
    }

    public void setActualDecesion(boolean actualDecesion) {
        this.actualDecesion = actualDecesion;
    }

    public boolean isOurDesecion() {
        return ourDesecion;
    }

    public void setOurDesecion(boolean ourDesecion) {
        this.ourDesecion = ourDesecion;
    }
    
    public TfidfVectorSpaceDocumentRepresentation getDocument(){
        return this.document;
    }
    
    private boolean ourDesecion;
    
    public FSDResultItem(boolean actual,boolean our,TfidfVectorSpaceDocumentRepresentation doc){
        this.actualDecesion = actual;
        this.ourDesecion = our;
        this.document=doc;
    }
    
    
    
}
