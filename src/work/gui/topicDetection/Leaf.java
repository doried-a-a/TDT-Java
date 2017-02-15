/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package work.gui.topicDetection;

/**
 *
 * @author Asus
 */
public class Leaf implements MyTree{
 private String name;
 private boolean isCorrect;
 private String Contenent;

    public Leaf(String name, boolean isCorrect, String Contenent) {
        this.name = name;
        this.isCorrect = isCorrect;
        this.Contenent = Contenent;
    }
   
 
    @Override
    public String getName() {
      return name;
    }

    public String getContenent() {
        return Contenent;
    }

    public boolean isIsCorrect() {
        return isCorrect;
    }
 @Override
    public String toString() {
            return getName();
        }
    
}
