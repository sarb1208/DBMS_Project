/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author sarbjyot
 */
public class InventoryController implements Initializable {
   Connection connection = null;
  
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product,String> columnPId;
    @FXML
    private TableColumn<Product,String> columnName;
    @FXML
    private TableColumn<Product,String> columnDescription;
    @FXML
    private TableColumn<Product,String> columnManufacturer;
    @FXML
    private TableColumn<Product,Float> columnPrice;
    @FXML
    private TableColumn<Product,Integer> columnQuantity;
    @FXML
    private TableColumn<Product,String> columnVId;
   private ObservableList<Product> data;
    @FXML
    private Button addbutton;
    @FXML
    private Button reloadButton;
    @FXML
    private Button editbutton;
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
            controller.init(window);
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

    @FXML
    private void reloadTable(MouseEvent event) {
        reload();
    }

    private void delete(MouseEvent event) {
        ObservableList<Product> allproducts = productTable.getItems();
        Product p = productTable.getSelectionModel().getSelectedItem();
        allproducts.removeAll(p);
        try{
            PreparedStatement pst = null;
            String query = "Delete from Inventory where PId = ?";
            pst = connection.prepareStatement(query);
            pst.setInt(1,Integer.parseInt(p.getPId()));
            pst.executeUpdate();
            pst.close();
            
            
        } catch (SQLException ex) {
            System.out.println(ex);
       }
    }

    @FXML
    private void edit(MouseEvent event) {
        ObservableList<Product> allproducts = productTable.getItems();
        Product p = productTable.getSelectionModel().getSelectedItem();
        if(p!=null){
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
            controller.init(p);
            controller.init(window);
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
    }
    
}
