/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

import java.sql.*;
public class LoginModel {
  Connection connection = null;
  public LoginModel () {
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
  
  public boolean isLogin(String email,String pass,Member member) throws SQLException{
      PreparedStatement preparedStatement = null;
      ResultSet resultset = null;
      String query = "select * from Member where E_Mail = ? and Password = ?";
     try{ 
      preparedStatement = connection.prepareStatement(query);
      
      preparedStatement.setString(1, email);
      preparedStatement.setString(2,pass);
      
      resultset = preparedStatement.executeQuery();
      
      if(resultset.next()){
          
          member.setUID(resultset.getInt("UID"));
          member.setPassword(resultset.getString("Password"));
          member.setMail_id(resultset.getString("E_Mail"));
          member.setMobile_no(resultset.getString("Mobile"));
          member.setName(resultset.getString("Name"));
          return true;
          
      }
      else{
          return false;
      }
      
     }catch(Exception e){
         return false;
     }finally{
         preparedStatement.close();
         resultset.close();
     }
  }
}
