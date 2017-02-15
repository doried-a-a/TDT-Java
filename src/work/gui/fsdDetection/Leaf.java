/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package work.gui.fsdDetection;

import work.FSDResultItem;
import work.gui.topicDetection.*;

/**
 *
 * @author Asus
 */
public class Leaf implements MyTree{
 private String name;
 private FSDResultItem result;
 private String Contenent;

    public Leaf(String name, FSDResultItem result, String Contenent) {
        this.name = name;
        this.result=result;
        this.Contenent = Contenent;
    }
   
 
    @Override
    public String getName() {
      return name;
    }

    public String getContenent() {
        return Contenent;
    }

    public FSDResultItem getResult() {
        return result;
    }
 @Override
    public String toString() {
            return getName();
    }
    
}
