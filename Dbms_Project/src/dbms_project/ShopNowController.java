/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sarbjyot
 */
public class ShopNowController implements Initializable {

    @FXML
    private TableView<CartItem> Cart;
    @FXML
    private TextField total;
    @FXML
    private Button Orderbutton;
    String uid;
    /**
     * Initializes the controller class.
     */
    Connection connection = null;
    
    private ObservableList<CartItem> data;
     @FXML
    private TableColumn<CartItem,String> columnPId;
    @FXML
    private TableColumn<CartItem,String> columnName;
    @FXML
    private TableColumn<CartItem,Float> columnPrice;
    @FXML
    private TableColumn<CartItem,Integer> columnQuantity;
    Stage w = null;
    @FXML
    private RadioButton COD;
    @FXML
    private RadioButton Debit;
    
    final  ToggleGroup group = new ToggleGroup();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connection = SqliteConnection.Connector();
       if (connection == null) {

      System.out.println("connection not successful");
       System.exit(1);}
       else{
           System.out.println("Shop now  Connected");
       }
       this.total.setText("0");
       COD.setToggleGroup(group);
       Debit.setToggleGroup(group);
       Cart.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
       
    }
    public void init(String UId,Stage w){
        this.w = w;
        uid = UId;
        try{
            data = FXCollections.observableArrayList();
           String query = "Select V.PId as P,Name,Price,V.Quantity AS VQ from Inventory,"
                   + "(Select PId,Quantity from Cart_Items where CId = ?) AS V where V.PId = Inventory.PId and Inventory.Quantity != 0 and V.Quantity <= Inventory.Quantity ";
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, UId);
            rs = pst.executeQuery();
              while (rs.next()) {
                //get string from db,whichever way 
                CartItem p = new CartItem(rs.getString("P"),rs.getString("Name"),rs.getInt("VQ"),rs.getFloat("Price"));
                data.add(p);
            }
              rs.close();
              pst.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);
        } 
             columnPId.setCellValueFactory(new PropertyValueFactory<>("PId"));
           columnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
             columnPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
            columnQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
                                     



          Cart.setItems(null);
          Cart.setItems(data); 
          
          Cart.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
    if (newSelection != null) {
        
        this.total.setText(Float.toString(newSelection.getPrice()));
    }
});
    }    

    @FXML
    private void Order(MouseEvent event) {
        ObservableList<CartItem> a = Cart.getSelectionModel().getSelectedItems();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        if(group.getSelectedToggle()==Debit&&a.size()!=0){
            try {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Card Details");
            window.setMinWidth(369);
            window.setMinHeight(374);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("CardDetails.fxml"));
            AnchorPane pane = loader.load();
            CardDetailsController controller = loader.getController();
            controller.init(window,a,uid);
            Scene scene = new Scene(pane);
            window.setScene(scene);
            window.showAndWait();
            w.close();
            
        } catch (IOException ex) {
            System.out.println(ex);
        }
        }
        else if(group.getSelectedToggle()==COD&&a.size()!=0){
             try{
                 Random rand = new Random();
                 String query = "Insert into Orders(OId,UId,Status,Order_date,PayType,PayId) values(?,?,?,?,?,?)";
                 PreparedStatement ps = connection.prepareStatement(query);
                 String o = Integer.toString(Integer.parseInt(uid)+rand.nextInt(10000));
                 ps.setInt(1,Integer.parseInt(o));
                 ps.setInt(2,Integer.parseInt(uid));
                 ps.setString(3,"Undelivered");
                
                 ps.setTimestamp(4,new Timestamp(System.currentTimeMillis()));
                 ps.setString(5,"COD");
                  

                  String  s = Integer.toString(rand.nextInt(2000))+Integer.toString(rand.nextInt(1000));
                  ps.setString(6, s);
                  
                  ps.executeUpdate();
                  ps.close();
                  fun(a,o);
                  
             } catch (SQLException ex) {
              System.out.println(ex);
            }
         }
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
            w.close();
           }
        } catch (SQLException ex) {
            System.out.println(ex);
            w.close();
        }
    }
    
}
