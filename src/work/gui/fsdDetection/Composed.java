
package work.gui.fsdDetection;

import work.gui.topicDetection.*;
import java.util.ArrayList;

public class Composed implements MyTree{
   private String name;
private ArrayList<MyTree> con=new ArrayList<>();
    public Composed(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
       return name;
    }
    
    public void add(MyTree t){
    con.add(t);
    }

    public ArrayList<MyTree> getContenent() {
        return con;
    }
   @Override
     public String toString() {
            return getName();
        }
    
}
