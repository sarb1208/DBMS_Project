/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author sarbjyot
 */
public class Admin_HomeController implements Initializable {
    Connection connection = null;
    
    
    /*
    private int UId ;
           private String Name;
           private Address address;
           private String mail_id;
           private String mobile_no;
           public Cart cart;
           private String password;
    */
     final ToggleGroup group = new ToggleGroup();
    private TableView<Product> productTable;
    
    private TableColumn <Product,String> columnPId;
    
    private TableColumn <Product,String> columnName;
    
    private TableColumn <Product,String> columnDescription;
    
    private TableColumn <Product,String> columnManufacturer;
    
    private TableColumn <Product,String> columnVId;
    
    private TableColumn <Product,Float> columnPrice;
    
    private TableColumn <Product,Integer> columnQuantity;
    @FXML
    private AnchorPane rootPane;
        
     private ObservableList<Product> data;
     
    @FXML
    private Button logoutButton;
    @FXML
    private ToggleButton Inventorybutton;
    @FXML
    private ToggleButton Membersbutton;
    @FXML
    private ToggleButton Ordersbutton;
    @FXML
    private Pane Tpane;
    private Admin admin = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       connection = SqliteConnection.Connector();
       if (connection == null) {

      System.out.println("connection not successful");
       System.exit(1);}
       else{
           System.out.println("Admin Connected");
       }
       //reload();
       this.Inventorybutton.setToggleGroup(group);
       this.Membersbutton.setToggleGroup(group);
       this.Ordersbutton.setToggleGroup(group);
       this.Inventorybutton.setSelected(true);
         FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Inventory.fxml"));
        
        try{
            Pane pane = loader.load();
            Tpane.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println(ex);
      }
  
       
       
    }    
    public void  init(Admin a){
        this.admin = a;
    }

    private void ADD(MouseEvent event) {
        try {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Add Product");
            window.setMinWidth(857);
            window.setMinHeight(589);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Product_Details.fxml"));
            AnchorPane pane = loader.load();
            Product_DetailsController controller = loader.getController();
            Scene scene = new Scene(pane);
            window.setScene(scene);
            window.showAndWait();
            //userName.setText(member.Name());
           // if(p!=null){
                //productTable.getItems().add(p);
          //}
           
            
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
    }

    private void reloadtable(MouseEvent event) {
       reload();
    }
    
    public void reload(){
      try{
            data = FXCollections.observableArrayList();
            String query = "Select PId,Name,Description,Price,Manufacturer,Quantity,VId,photo from Inventory";
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(query);
            rs = pst.executeQuery();
              while (rs.next()) {
                //get string from db,whichever way 
                Product p = new Product(rs.getString("PId"),rs.getString("Name"),rs.getString("Description"),rs.getFloat("Price"),rs.getString("Manufacturer"),rs.getInt("Quantity"),rs.getString("VId"));
                data.add(p);
            }
              rs.close();
              pst.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
           columnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
           columnPId.setCellValueFactory(new PropertyValueFactory<>("PId"));
            columnDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
             columnPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
                         columnManufacturer.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
                         columnQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
                                     columnVId.setCellValueFactory(new PropertyValueFactory<>("VId"));




          productTable.setItems(null);
          productTable.setItems(data);   
    }

    @FXML
    private void logout(MouseEvent event) {
        try {
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
          AnchorPane pane = loader.load();
          rootPane.getChildren().setAll(pane);
      } catch (IOException ex) {
          System.out.println(ex);
      }
    }


    @FXML
    private void Inventory(MouseEvent event) {
         FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Inventory.fxml"));
        
        try{
            Pane pane = loader.load();
            Tpane.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println(ex);
      }
    }

    @FXML
    private void Members(MouseEvent event) {
          FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MemberTable.fxml"));
        
        try{
            Pane pane = loader.load();
            Tpane.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println(ex);
      }
        
    }

    @FXML
    private void Orders(MouseEvent event) {
         FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("OrdersTable.fxml"));
        
        try{
            Pane pane = loader.load();
            OrdersTableController controller = loader.getController();
            controller.init(null);
            Tpane.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println(ex);
      }
    }

    
    
    }
    

