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
import java.sql.Timestamp;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sarbjyot
 */
public class CardDetailsController implements Initializable {

    @FXML
    private TextField cardField;
    @FXML
    private PasswordField cvvField;
    @FXML
    private TextField date;
    @FXML
    private TextField bankField;
    @FXML
    private Button payButton;
    @FXML
    private Button closeButton;
    @FXML
    private Label label;
    ObservableList<CartItem> a;
    Stage window;
    Connection connection = null;
    String uid;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        connection = SqliteConnection.Connector();
       if (connection == null) {

      System.out.println("connection not successful");
       System.exit(1);}
       else{
           System.out.println("Admin Connected");
       }
    }    
    public void init(Stage w,ObservableList<CartItem> a,String u){
        this.window = w;
        this.a = a;
        this.uid = u;
    }
    @FXML
    private void pay(MouseEvent event) {
        if(this.bankField.getText().length()<1||this.cardField.getText().length()<1||this.cvvField.getText().length()<1||this.date.getText().length()<1){
            this.label.setText("No Field can be empty");
            return ;
        }
        try{
                 Random rand = new Random();
                 String query = "Insert into Orders(OId,UId,Status,Order_date,PayType,PayId) values(?,?,?,?,?,?)";
                 PreparedStatement ps = connection.prepareStatement(query);
                 String o = Integer.toString(Integer.parseInt(uid)+rand.nextInt(10000));
                 ps.setInt(1,Integer.parseInt(o));
                 ps.setInt(2,Integer.parseInt(uid));
                 ps.setString(3,"Undelivered");
                
                 ps.setTimestamp(4,new Timestamp(System.currentTimeMillis()));
                 ps.setString(5,"Debit");
                  

                  String  s = Integer.toString(rand.nextInt(2000))+Integer.toString(rand.nextInt(1000));
                  ps.setString(6, s);
                  
                  ps.executeUpdate();
                  ps.close();
                  fun(a,o);
                  this.label.setText("Your order is placed !!!!");
                  this.payButton.setDisable(true);
                  
             } catch (SQLException ex) {
              System.out.println(ex);
              window.close();
            }
        
    }

    @FXML
    private void close(MouseEvent event) {
        window.close();
    }
    private void fun(ObservableList<CartItem> a,String o) {
        try{
            
            for(int i=0;i<a.size();i++){
            CartItem p = a.get(i);
            String query = "Insert into Ordered_Items values(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1,Integer.parseInt(o));
            ps.setInt(2,Integer.parseInt(p.getPId()));
            ps.setInt(3,p.getQuantity());
            ps.executeUpdate();
            ps.close();
            query = "Update Inventory set Quantity = Quantity - ? where PId = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1,p.getQuantity());
            ps.setInt(2,Integer.parseInt(p.getPId()));
            ps.executeUpdate();
            
            query = "Delete from Cart_Items where CId = ? and PId = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1,Integer.parseInt(uid));
            ps.setInt(2,Integer.parseInt(p.getPId()));
            ps.executeUpdate();
            ps.close();
            
           }
        } catch (SQLException ex) {
            System.out.println(ex);
            window.close();
           
        }
    }
    


}