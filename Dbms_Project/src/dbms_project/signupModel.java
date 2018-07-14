/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javax.swing.JOptionPane;
public class signupModel {
  Connection connection = null;
  public signupModel () {
   connection = SqliteConnection.Connector();
   if (connection == null) {

   System.out.println("connection not successful");
    System.exit(1);}
  }
  
  public boolean isDbConnected() {
   try {
  return !connection.isClosed();
 } catch (SQLException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
  return false;
 }
}
  
  public boolean Signup(String name,String email,String mobile,String pass,String repass,String address) throws SQLException{
      PreparedStatement preparedStatement = null;
     
      String query = "Insert into Member (Name,Mobile,E_Mail,Password,Address) values(?,?,?,?,?)";
     try{ 
        ///*&&email!=null&&email.length()>=5&&mobile!=null&&mobile.length()==10&&pass!=null&&repass!=null&&pass.compareTo(repass)==0*/){
            
        
        
             
         
      preparedStatement = connection.prepareStatement(query);
      
      preparedStatement.setString(1, name);
      preparedStatement.setString(2,mobile);
      preparedStatement.setString(3,email);
      preparedStatement.setString(4,pass);
      preparedStatement.setString(5,address);
       preparedStatement.execute();
      // JOptionPane.showMessageDialog(null,"Registered Successfully");
 
         return true;
    
        
       // return false;
     /* if(resultset.next()){
          return true;
      }
      else{
          return false;
      }*/
      
     }catch(Exception e){
         System.out.println(e);
         return false;
     }finally{
         preparedStatement.close();
     }
  }
}
