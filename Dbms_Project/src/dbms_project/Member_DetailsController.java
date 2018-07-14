/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author sarbjyot
 */
public class Member_DetailsController implements Initializable {
    
    Connection connection = null;
    private Member member = null;
    @FXML
    private TextField NameField;
    @FXML
    private TextField MobileField;
    @FXML
    private Button EditButton;
    @FXML
    private Button SaveButton;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private Label status;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void init(Member member){
        this.member = member;
        NameField.setText(member.getName());
        MobileField.setText(member.getMobile_no());
        PasswordField.setText(member.getPassword());
         connection = SqliteConnection.Connector();
   if (connection == null) {

   System.out.println("connection not successful");
    System.exit(1);}
    }

    @FXML
    private void edit(MouseEvent event) {
        NameField.setEditable(true);
        MobileField.setEditable(true);
        PasswordField.setEditable(true);
    }
    @FXML
    private void save(MouseEvent event){
       if(NameField.getText().length()<1||MobileField.getText().length()<1||PasswordField.getText().length()<1){
           status.setText("No entry can be null");
           return ;
       }
       member.setName(NameField.getText());
       member.setMobile_no(MobileField.getText());
       member.setPassword(PasswordField.getText());
       NameField.setEditable(false);
        MobileField.setEditable(false);
        PasswordField.setEditable(false);
        PreparedStatement pst = null;
        String query = "Update Member set Name = ? , Mobile = ? , Password = ? where UId = ?";
        try{
            pst = connection.prepareStatement(query);
            pst.setString(1,member.getName());
            pst.setString(2,member.getMobile_no());
            pst.setString(3,member.getPassword());
            pst.setInt(4,member.getUId());
            pst.execute();
            pst.close();
            status.setText("Successfully Saved Changes");
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
    }
    
    
}
