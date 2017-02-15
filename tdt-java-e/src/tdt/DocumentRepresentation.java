/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tdt;

import java.util.Date;

/**
 *
 * @author doried
 */
public abstract class DocumentRepresentation implements ISimilatityMeasurable{
    Date date;
    public void setDate(Date date){
    	this.date = date;
    }
    
    public Date getDate(){
    	return date;
    }
}
