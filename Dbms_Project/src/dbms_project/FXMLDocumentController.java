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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
public class FXMLDocumentController implements Initializable {
  public LoginModel loginModel = new LoginModel();
  private Member member = null;
    @FXML
    private Label label;

    @FXML
    private Label Close;
    
    
    @FXML
    private Label Welcome; 
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private ImageView logo;
    @FXML
    private Button signupButton;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ToggleButton adminButton;
    @FXML
    private ToggleButton userButton;
    private ToggleGroup group;
    Connection connection = null;
    @FXML
    void handleButtonAction(MouseEvent event) {
            System.exit(0);
    }

    void initialize() {
        assert label != null : "fx:id=\"label\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert email != null : "fx:id=\"email\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert logo != null : "fx:id=\"logo\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert Welcome != null : "fx:id=\"Welcome\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert signupButton != null : "fx:id=\"signupButton\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert Close != null : "fx:id=\"Close\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        group.getToggles().add(userButton);
        group.getToggles().add(adminButton);
        
        

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         
        if (loginModel.isDbConnected()) {
   Welcome.setText("Connected");
  } else {

  Welcome.setText("Not Connected");
  }
        group = new ToggleGroup();
        group.getToggles().add(userButton);
        group.getToggles().add(adminButton);
 }
    @FXML
    public void login(ActionEvent event){
        if(group.getSelectedToggle()==userButton){
        try{
            String user = null;
            member = new Member();
           if(loginModel.isLogin(email.getText(), password.getText(),member)){
            Welcome.setText("Welcome");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Home.fxml"));
            AnchorPane pane = loader.load();
            HomeController homeController = loader.getController();
            member.make_cart();
            homeController.init(member);
            rootPane.getChildren().setAll(pane);
            
        }
           else{
               Welcome.setText("Username or Password is incorrect");
           }
        }catch(SQLException e){
            Welcome.setText("Username or Password is incorrect");
            e.printStackTrace();
        } catch (IOException ex) {
          Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }else if(group.getSelectedToggle()==adminButton){
        connection = SqliteConnection.Connector();
       if (connection == null) {

       System.out.println("connection not successful");
      System.exit(1);}
        try{
              String query = "Select * from Admin where Email = ? and Password = ?";
              PreparedStatement pst = connection.prepareStatement(query);
              pst.setString(1,this.email.getText());
              pst.setString(2,this.password.getText());
              ResultSet rs = pst.executeQuery();
              if(rs.next()){
                  Admin a = new Admin(Integer.toString(rs.getInt("AId")),rs.getString("Name"),rs.getString("Email"),rs.getString("Password"));
                   try {
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().getResource("Admin_Home.fxml"));
          AnchorPane pane = loader.load();
          Admin_HomeController controller = loader.getController();
          controller.init(a);
          rootPane.getChildren().setAll(pane);
          
      } catch (IOException ex) {
          System.out.println(ex);
      }
              }else{
                   Welcome.setText("Username or Password is incorrect");
              }
              pst.close();
          rs.close();
              
        }   catch (SQLException ex) {
                  System.out.println(ex);
            }
    }
    }
    
    @FXML
    public void signup(ActionEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Signup.fxml"));
        
        try{
            AnchorPane pane = loader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println(ex);
      }
    }

    private void admin(MouseEvent event) {
        
      try {
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().getResource("Admin_Home.fxml"));
          AnchorPane pane = loader.load();
          rootPane.getChildren().setAll(pane);
      } catch (IOException ex) {
          System.out.println(ex);
      }
    }
    }

   
   
   
