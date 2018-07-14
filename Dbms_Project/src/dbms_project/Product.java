/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author SHUBHAM
 */
public class Product {
    
    private String PId;
    private String Name;
    private String Description;
    private String Manufacturer;
    private float Price;
    private int Quantity;
    private String VId;
    private Image image;
    Connection connection = null;
    Product(String pid,String name,String d,float p,String m,int q,String v){
        PId = pid;
        Name = name;
        Description = d;
        Manufacturer = m;
        Price = p;
        Quantity = q;
        VId = v;
         connection = SqliteConnection.Connector();
       if (connection == null) {

      System.out.println("connection not successful");
       System.exit(1);}
       else{
           System.out.println("Product Connected");
       }
    }
    Product(){
           connection = SqliteConnection.Connector();
       if (connection == null) {

      System.out.println("connection not successful");
       System.exit(1);}
       
    }
    Product(String N,float p,int q){
        this.Name = N;
        this.Price = p;
        this.Quantity = q;
    }
    public String getPId(){
        return this.PId;
    }
    public String getName(){
        return Name;
    }
    public String getDescription(){
        return Description;
    }
    public String getManufacturer(){
        return Manufacturer;
    }
    public float getPrice(){
        return Price;
    }
    public int getQuantity(){
        return Quantity;
    }
    public String getVId(){
        return VId;
    }
    public Image getPhoto(){
        return image;
    }
    public void setPId(String pid){
        PId = pid;
        

    }
    public void setphoto(Image image){
        this.image = image;
    }
    public void setName(String name){
         Name = name;
         String query = "Update Inventory set Name = ? where  PId = ?";
         PreparedStatement pst = null;
        
         try{
             pst = connection.prepareStatement(query);
             pst.setString(1,name);
             pst.setString(2,this.PId);
             int rs = pst.executeUpdate();
             if(rs>0){
                 System.out.println("Name is updated");
             }
             pst.close();
         } catch (SQLException ex) {
             System.out.println(ex);
        }
    }
    public void setDescription(String d){
         Description = d;
          String query = "Update Inventory set Description = ? where  PId = ?";
         PreparedStatement pst = null;
        
         try{
             pst = connection.prepareStatement(query);
             pst.setString(1,d);
             pst.setString(2,this.PId);
             int rs = pst.executeUpdate();
             if(rs>0){
                 System.out.println("Description is updated");
             }
             pst.close();
         } catch (SQLException ex) {
             System.out.println(ex);
        }
    }
    public void setManufacturer(String m){
        Manufacturer = m;
         String query = "Update Inventory set Manufacturer = ? where  PId = ?";
         PreparedStatement pst = null;
        
         try{
             pst = connection.prepareStatement(query);
             pst.setString(1,m);
             pst.setString(2,this.PId);
             int rs = pst.executeUpdate();
             if(rs>0){
                 System.out.println("Manufacturer is updated");
             }
             pst.close();
         } catch (SQLException ex) {
             System.out.println(ex);
        }
    }
    public void setPrice(float price){
         Price = price;
          String query = "Update Inventory set Price = ? where  PId = ?";
         PreparedStatement pst = null;
        
         try{
             pst = connection.prepareStatement(query);
             pst.setFloat(1,price);
             pst.setString(2,this.PId);
             int rs = pst.executeUpdate();
             if(rs>0){
                 System.out.println("Price is updated");
             }
             pst.close();
         } catch (SQLException ex) {
             System.out.println(ex);
        }
    }
    public  void setQuantity(int q){
         Quantity = q;
          String query = "Update Inventory set Quantity = ? where  PId = ?";
         PreparedStatement pst = null;
        
         try{
             pst = connection.prepareStatement(query);
             pst.setInt(1,q);
             pst.setString(2,this.PId);
             int rs = pst.executeUpdate();
             if(rs>0){
                 System.out.println("Quantity is updated");
             }
             pst.close();
         } catch (SQLException ex) {
             System.out.println(ex);
        }
    }
    public void setVId(String v){
        VId = v;
         String query = "Update Inventory set VId = ? where  PId = ?";
         PreparedStatement pst = null;
        
         try{
             pst = connection.prepareStatement(query);
             pst.setString(1,v);
             pst.setString(2,this.PId);
             int rs = pst.executeUpdate();
             if(rs>0){
                 System.out.println("VId is updated");
             }
             pst.close();
         } catch (SQLException ex) {
             System.out.println(ex);
        }
    }
    
    public boolean equals(Object o){
        if(o!=null && o instanceof Product){
            String id = ((Product)o).getPId();
            if(id!=null&&id.equals(this.PId)){
                return true;
            }
            
        }
        return false;
    }
    
    public int hashCode(){
        return this.PId.hashCode();
    }
    
    
    
}
