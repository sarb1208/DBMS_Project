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
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author sarbjyot
 */
public class InvoiceController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextArea addressField;
    @FXML
    private TableView<Product> Table;
    @FXML
    private TableColumn<Product,String> columnName;
    @FXML
    private TableColumn<Product,Float> columnPrice;
    @FXML
    private TableColumn<Product,Integer> columnQuantity;
    @FXML
    private TextField totalField;
    private ObservableList<Product> memberlist;
     Connection connection = null;
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
       this.nameField.setEditable(false);
       this.addressField.setEditable(false);
       
           
    }    
    
    public void init(Order o){
        try{
            String query = "Select Name,Address from Member where UId = ?";
             PreparedStatement pst = connection.prepareStatement(query);
             pst.setInt(1,Integer.parseInt(o.getUId()));
             ResultSet rs = pst.executeQuery();
             if(rs.next()){
                 this.nameField.setText(rs.getString("Name"));
                 this.addressField.setText(rs.getString("Address"));
             }
             pst.close();
             rs.close();
        } catch (SQLException ex) {
           System.out.println(ex);
        }
        float total = 0;
        memberlist = FXCollections.observableArrayList();
        try{
            String query = "Select Name,Price,V.Quantity as VQ from Inventory,(Select PId,Quantity from Ordered_Items where OId = ?) as V where Inventory.PId = V.PId";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1,Integer.parseInt(o.getOId()));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                //get string from db,whichever way 
                Product p = new Product(rs.getString("Name"),rs.getFloat("Price"),rs.getInt("VQ"));
                
                memberlist.add(p);
                total += p.getPrice()*p.getQuantity();
                
            }
              rs.close();
              pst.close();
            
        
           columnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
           columnPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
           columnQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
           




          Table.setItems(null);
          Table.setItems(memberlist);
          this.totalField.setText(Float.toString(total));
        } catch (java.sql.SQLException ex) {
                System.out.println(ex);
         }
}
}

    
    

