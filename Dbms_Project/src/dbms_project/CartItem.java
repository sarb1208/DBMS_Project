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
public class CartItem {
    private String PId;
    private String Name;
    private int Quantity;
    private float price;
    
    CartItem(String pid,String n,int q,float p){
        this.PId = pid;
        this.Name = n;
        this.Quantity = q;
        this.price = q*p;
    }
    public String getPId(){
        return this.PId;
    }
    public String getName(){
        return Name;
    }
    public int getQuantity(){
        return Quantity;
    }
    
    public float getPrice(){
        return price;
    }
    
    
   
    
}
