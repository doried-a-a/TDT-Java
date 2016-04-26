/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tdt;

import java.util.Objects;

/**
 *
 * @author doried
 */
public class LexicalWord extends Word {
    private String word;
    
    public LexicalWord(String word){
        this.word = word;
    }
    
    public String getWord(){
        return word;
    }
    
    public String toString(){
        return word;
    }

    @Override
    public int hashCode() {
        return this.word.hashCode();
    }

   
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LexicalWord==false)
            return false;
        
       return word.equals(((LexicalWord)obj).getWord());
    }
    
    
}
