/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

/**
 *
 * @author sarbjyot
 */
public class Oproduct {
    private String PId;
    private int Quantity;
   
    Oproduct(String p,int q){
        PId = p;
        this.Quantity = q;
    }
    
    public String getPId(){
        return PId;
    }
    public int getQuantity(){
        return this.Quantity;
    }
    
}
