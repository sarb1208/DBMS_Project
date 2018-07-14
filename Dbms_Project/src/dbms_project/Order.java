/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

/**
 *
 * @author iiita
 */
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
public class Order {
       private String OId;
       private Map<Product,Integer>map;
       private String UId;
       private String Status;
       private Date Order_date;
       private Date Ddate;
       private String PayType;
       private String PayId;
      private  Connection connection;
       Order(String oid,String uid,String s,Date Odate,Date Ddate,String pt,String pi){
            connection = SqliteConnection.Connector();
         if (connection == null) {

       System.out.println("connection not successful");
       System.exit(1);}
           OId = oid;
           UId = uid;
           Order_date = Odate;
           Status = s;
           this.Ddate = Ddate ;
           this.PayType = pt;
           this.PayId = pi;
           map = new HashMap<>();
      }
      public String getOId(){
           return OId;
       }
       
       public String getStatus(){
           return Status;
       }
      public  String getUId(){
           return UId;
       }
       public void setStatus(String Status){
           this.Status = Status;
       }
     public Date getOrder_date(){
           return Order_date;
       }
       
     public  Map<Product,Integer>  getMap(){
          return map;
      }
      
      public void setDdate(Date date){
          this.Ddate = date;
      }
     public  Date getDdate(){
         return Ddate;
     }
      public String getPayType(){
           return this.PayType;
       }
      public  String getPayId(){
           return this.PayId;
       }
       public void getItems(){
           this.map = new HashMap<>();
           PreparedStatement pst = null;
           ResultSet rs = null;
          String query = "Select Inventory.PId AS IP,Name,Description,Price,Manufacturer,Inventory.Quantity AS IQ,VId,photo,V.Quantity AS VQ from Inventory,"
                   + "(Select PId,Quantity from Ordered_Items where OId = ?) AS V where V.PId = Inventory.PId ";
        try{
            pst = connection.prepareStatement(query);
            pst.setInt(1,Integer.parseInt(OId));
            rs = pst.executeQuery();
            while(rs.next()){
                Product p = new Product(rs.getString("IP"),rs.getString("Name"),rs.getString("Description"),rs.getFloat("Price"),rs.getString("Manufacturer"),rs.getInt("IQ"),rs.getString("VId"));
               map.put(p,rs.getInt("VQ"));
            }
                
                           
            rs.close();
            pst.close();
        } catch (SQLException ex) {
             System.out.println(ex);
          }
       }

     
    
}
