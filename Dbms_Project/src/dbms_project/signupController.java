package dbms_project;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class signupController implements Initializable{
   public signupModel SignupModel = new signupModel();
   private Member member = new Member();
   Connection connection = null;
    @FXML
    private Label label;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button signupButton;

    @FXML
    private TextField repassword;

    @FXML
    private TextField mobileNo;

    @FXML
    private TextField Name;

    @FXML
    private ImageView logo;

    @FXML
    private Label Welcome;

    @FXML
    private Label close;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button backButton;
    @FXML
    private TextArea addressField;
    
    
    @FXML
    void Close(MouseEvent event) {
            System.exit(0);
    }

    void initialize() {
        assert label != null : "fx:id=\"label\" was not injected: check your FXML file 'Signup.fxml'.";
        assert email != null : "fx:id=\"email\" was not injected: check your FXML file 'Signup.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'Signup.fxml'.";
        assert signupButton != null : "fx:id=\"signupButton\" was not injected: check your FXML file 'Signup.fxml'.";
        assert repassword != null : "fx:id=\"repassword\" was not injected: check your FXML file 'Signup.fxml'.";
        assert mobileNo != null : "fx:id=\"mobileNo\" was not injected: check your FXML file 'Signup.fxml'.";
        assert Name != null : "fx:id=\"Name\" was not injected: check your FXML file 'Signup.fxml'.";
        assert logo != null : "fx:id=\"logo\" was not injected: check your FXML file 'Signup.fxml'.";
        assert Welcome != null : "fx:id=\"Welcome\" was not injected: check your FXML file 'Signup.fxml'.";
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'Signup.fxml'.";
        

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
          if (SignupModel.isDbConnected()) {
   Welcome.setText("Connected");
  } else {

  Welcome.setText("Not Connected");
  }
    }
     @FXML
    public void signup(ActionEvent event){
        try{
            if(Name.getText().length()<1||email.getText().length()<1||mobileNo.getText().length()<1||password.getText().length()<1||repassword.getText().length()<1||addressField.getText().length()<1){
                Welcome.setText("No Entry Can be Null");
                return;
            }
            if(password.getText().compareTo(repassword.getText())!=0){
                Welcome.setText("Passwords do not match");
                return;
            }
            
           if(SignupModel.Signup(Name.getText(),email.getText(),mobileNo.getText(), password.getText(),repassword.getText(),addressField.getText())){
            Welcome.setText("Welcome");
            member.setName(Name.getText());
            member.setMail_id(email.getText());
            member.setMobile_no(mobileNo.getText());
            member.setPassword(password.getText());
            member.setAddress(addressField.getText());
             FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Home.fxml"));
            AnchorPane pane = loader.load();
            HomeController homeController = loader.getController();
             connection = SqliteConnection.Connector();
       if (connection == null) {

       System.out.println("connection not successful");
      System.exit(1);}
        try {
  if(!connection.isClosed()){
      System.out.println("Home Connected");
      this.member = member;
      
  }
 } catch (SQLException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
  //return false;
 }
              String query = "Select UId from Member where E_Mail = ?";
               PreparedStatement pst = connection.prepareStatement(query);
               pst.setString(1,member.getMail_id());
               ResultSet rs = pst.executeQuery();
               
               if(rs.next()){
                   member.setUID(rs.getInt("UId"));
                   member.make_cart();
                   pst.close();
                   rs.close();
                    homeController.init(member);
                     rootPane.getChildren().setAll(pane);
               }
               else{
                   System.out.println("NO member found with mail:"+member.getMail_id());
               }
              
           
        }
           else{
               Welcome.setText("Username or Password is incorrect");
           }
        }catch(SQLException e){
            Welcome.setText("Username or Password is incorrect");
            e.printStackTrace();
        } catch (IOException ex) {
           Logger.getLogger(signupController.class.getName()).log(Level.SEVERE, null, ex);
       }
    }

    @FXML
    private void back(MouseEvent event) {
          FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
        
        try{
            AnchorPane pane = loader.load();
           
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println(ex);
      }
    }
}
