/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author SHUBHAM
 */
public class Cart {
      private int CId;
      private Map<Product,Integer> map;  
      Connection connection = null;
      PreparedStatement pst = null;
          ResultSet rs = null;
      public Cart(int id) {
       
        CId = id;
         connection = SqliteConnection.Connector();
         if (connection == null) {

       System.out.println("connection not successful");
       System.exit(1);}
          loadCart();
           
           
         
       
      }
      public void loadCart(){
           this.map = new HashMap<>();
          String query = "Select Inventory.PId AS IP,Name,Description,Price,Manufacturer,Inventory.Quantity AS IQ,VId,photo,V.Quantity AS VQ from Inventory,"
                   + "(Select PId,Quantity from Cart_Items where CId = ?) AS V where V.PId = Inventory.PId and Inventory.Quantity != 0";
        try{
            pst = connection.prepareStatement(query);
            pst.setInt(1,CId);
            rs = pst.executeQuery();
            while(rs.next()){
                Product p = new Product(rs.getString("IP"),rs.getString("Name"),rs.getString("Description"),rs.getFloat("Price"),rs.getString("Manufacturer"),rs.getInt("IQ"),rs.getString("VId"));
                p.setphoto(this.convertToImage(rs));
                int k = rs.getInt("VQ");
                if(k>rs.getInt("IQ"))
                  k = rs.getInt("IQ");
                map.put(p,k);
            }
            rs.close();
            pst.close();
            System.out.println("Successfully Retreived data from db to cart");
        } catch (SQLException ex) {
             System.out.println(ex);
          }
      }
      public Image convertToImage(ResultSet rs){
        Image image = null;
                try {
           
                 InputStream is= rs.getBinaryStream("photo");
                  OutputStream os=new FileOutputStream(new File("photo.jpg"));
                 byte [] content= new byte[1024];
                 int size=0;


        while ((size=is.read(content))!=-1){

            os.write(content, 0, size);
        }

    os.close();
    is.close();
    image = new Image("file:photo.jpg",360,374,true,true);
     // img00.setImage(image);
      //img00.setPreserveRatio(true);
      System.out.println("Image Converted");
         
     
            
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (FileNotFoundException ex) {
             System.out.println(ex);
            
        } catch (IOException ex) {
          System.out.println(ex);

        }
          return image;    
     }
      int CId(){
          return CId;
      }
      
      void increment(Product product){
          Integer count = map.get(product);
          if(count==null)
             count = 0;
           map.put(product,count + 1);
           if(count==0){
               System.out.println(count);
              String q = "Insert into Cart_Items(CId,PId,Quantity) values(?,?,?)";
              try{
                pst = connection.prepareStatement(q);
                  pst.setInt(1,CId);
                  pst.setInt(2,Integer.parseInt(product.getPId()));
                  pst.setInt(3,count+1);
                  pst.executeUpdate();
                  pst.close();
                  System.out.println("Successfully incremented");
          } catch (SQLException ex) {
              System.out.println(ex);
              System.out.println("Could not update cart");
          }  
        }else{
              String query = "Update Cart_Items Set Quantity = ? Where CId = ? and PId = ?";
          try{
                pst = connection.prepareStatement(query);
                  pst.setInt(1,count+1);
                  pst.setInt(2,CId);
                  pst.setInt(3,Integer.parseInt(product.getPId()));
                  pst.executeUpdate();
                  pst.close();
                  System.out.println("Successfully incremented");
          } catch (SQLException ex) {
              System.out.println(ex);
              System.out.println("Could not update cart");
          }     
           }
          
      }
     
      void delete(Product product){
          map.remove(product);
           String query = "Delete from Cart_Items where CId = ? and PId = ?";
           try{
               pst = connection.prepareStatement(query);
               pst.setInt(1, CId);
               pst.setString(2,product.getPId());
               pst.execute();
               pst.close();
                  System.out.println("Successfully Removed");
           } catch (SQLException ex) {
                System.out.println(ex);
                System.out.println("Could not update cart");
          }
           
          
      }
      void decrement(Product product){
          Integer count = map.get(product);
          if(count!=null)
          {
              map.put(product,count-1);
              String query = "Update Cart_Items set Quantity = ? where CId = ? and PId = ?";
              try{
                  pst = connection.prepareStatement(query);
                  pst.setInt(1,count-1);
                  pst.setInt(2,CId);
                  pst.setString(3,product.getPId());
                  pst.execute();
                  pst.close();
                  System.out.println("Successfully decremented");
              } catch (SQLException ex) {
                  System.out.println(ex);
                  System.out.println("Could not update cart");
              }
              
          }
      }
      Map<Product,Integer> get_Cart(){
          return map;
      }
      
}
