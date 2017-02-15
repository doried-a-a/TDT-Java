/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package work.gui.topicDetection;

import java.util.ArrayList;

/**
 *
 * @author Asus
 */
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
