/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author sarbjyot
 */
public class MemberTableController implements Initializable {

    /**
     * Initializes the controller class.
     * 
     * 
     */
     @FXML
    private TableView<Member> MemberTable;
     @FXML
    private TableColumn<Member,Integer> columnUId;
    @FXML
    private TableColumn<Member,String> columnName;
    
    @FXML
    private TableColumn<Member,String> columnMail;
    
    @FXML
    private TableColumn<Member,String> columnMobile;
    private ObservableList<Member> memberlist;
    Connection connection = null;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        connection = SqliteConnection.Connector();
       if (connection == null) {

      System.out.println("connection not successful");
       System.exit(1);}
       else{
           System.out.println("Product Details  Connected");
       }
        init();
    }
   public void init(){
       try{
            memberlist = FXCollections.observableArrayList();
            String query = "Select UId,Name,Mobile,E_Mail from Member";
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(query);
            rs = pst.executeQuery();
              while (rs.next()) {
                //get string from db,whichever way 
                Member p = new Member();
                p.setUID(rs.getInt("UId"));
                p.setName(rs.getString("Name"));
                p.setMail_id(rs.getString("E_Mail"));
                p.setMobile_no(rs.getString("Mobile"));
                memberlist.add(p);
                
            }
              rs.close();
              pst.close();
            
        }catch (java.sql.SQLException ex) {
                System.out.println(ex);
         }
           columnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
           columnUId.setCellValueFactory(new PropertyValueFactory<>("UId"));
           columnMail.setCellValueFactory(new PropertyValueFactory<>("Mail_id"));
           columnMobile.setCellValueFactory(new PropertyValueFactory<>("Mobile_no"));




          MemberTable.setItems(null);
          MemberTable.setItems(memberlist);
   }    
    
}
