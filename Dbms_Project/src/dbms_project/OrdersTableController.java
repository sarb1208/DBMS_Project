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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
public class OrdersTableController implements Initializable {

    @FXML
    private TableView<Order> Table;
    @FXML
    private TableColumn<Order,Integer> columnOId;
    @FXML
    private TableColumn<Order,Integer> columnUId;
    @FXML
    private TableColumn<Order,String> columnStatus;
    @FXML
    private TableColumn<Order,Date> columnOrder_Date;
    @FXML
    private TableColumn<Order,Date> columnDdate;
    @FXML
    private TableColumn<Order,String> columnPtype;
    @FXML
    private TableColumn<Order,String> columnPid;
    private ObservableList<Order> data;
    @FXML
    private Button updateButton;
    Connection connection = null;
    @FXML
    private Button cancelButton;
    @FXML
    private Button invoiceButton;
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
       Table.setRowFactory( tv -> {
    TableRow<Order> row = new TableRow<>();
    row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
            Order rowData = row.getItem();
             try {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Products");
            window.setMinWidth(600);
            window.setMinHeight(419);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("OrderedProducts.fxml"));
            AnchorPane pane = loader.load();
            OrderedProductsController controller = loader.getController();
            controller.init(rowData.getOId());
            Scene scene = new Scene(pane);
            window.setScene(scene);
            window.showAndWait();
            
        } catch (IOException ex) {
            System.out.println(ex);
        }
        }
    });
    return row ;
});
       
    }   
    public void init(String s){
        try{
            ResultSet rs = null;
            PreparedStatement pst = null;
            data = FXCollections.observableArrayList();
            String query;
            if(s==null)
            {
                query = "Select * from Orders";
            }
            else{ 
                query = "Select * from Orders where UId = ?";
                this.updateButton.setDisable(true);
                this.updateButton.setVisible(false);
                
            }
            
            pst = connection.prepareStatement(query);
            if(s!=null)
                pst.setString(1,s);
            rs = pst.executeQuery();
              while (rs.next()) {
                //get string from db,whichever way 
                Order p = new Order(rs.getString("OId"),rs.getString("UId"),rs.getString("Status"),rs.getDate("Order_date"),rs.getDate("Cancel_date"),rs.getString("PayType"),rs.getString("PayId"));
                data.add(p);
            }
              rs.close();
              pst.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
           columnOId.setCellValueFactory(new PropertyValueFactory<>("OId"));
           columnUId.setCellValueFactory(new PropertyValueFactory<>("UId"));
           columnStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
           columnOrder_Date.setCellValueFactory(new PropertyValueFactory<>("Order_date"));
           columnDdate.setCellValueFactory(new PropertyValueFactory<>("Ddate"));
                         columnPtype.setCellValueFactory(new PropertyValueFactory<>("PayType"));
                                     columnPid.setCellValueFactory(new PropertyValueFactory<>("PayId"));




          Table.setItems(null);
          Table.setItems(data); 
    }
    @FXML
    private void update(MouseEvent event) {
        
       Order p = Table.getSelectionModel().getSelectedItem();
       if(p!=null&&p.getStatus().compareTo("Undelivered")==0){
          Table.getItems().remove(p);
          p.setStatus("Delivered");
          Timestamp ts=new Timestamp(System.currentTimeMillis());  
                Date date=new Date(ts.getTime());  
                p.setDdate(date);
              try{
           String query = "Update Orders set Cancel_date = ?,Status = ? where OId = ?";
           PreparedStatement pst = connection.prepareStatement(query);
           pst.setTimestamp(1,ts);
           pst.setString(2,"Delivered");
           pst.setInt(3,Integer.parseInt(p.getOId()));
           pst.executeUpdate();
           pst.close();
           Table.getItems().add(p);
              } catch (SQLException ex) {
               System.out.println(ex);
           }
           
       }
       
    
}

    @FXML
    private void cancel(MouseEvent event) {
        Order o = Table.getSelectionModel().getSelectedItem();
        if(o!=null&&o.getStatus().compareTo("Cancelled")!=0&&o.getStatus().compareTo("Delivered")!=0){
            try{
                PreparedStatement pst = null;
                String query = "Update Orders set Status = ?,Cancel_date = ? where OId = ?";
                pst = connection.prepareStatement(query);
                pst.setString(1,"Cancelled");
                pst.setTimestamp(2,new Timestamp(System.currentTimeMillis()));
                pst.setInt(3,Integer.parseInt(o.getOId()));
                pst.executeUpdate();
                pst.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }

    @FXML
    private void invoice(MouseEvent event) {
         Order o = Table.getSelectionModel().getSelectedItem();
         if(o!=null&&o.getStatus().compareTo("Delivered")==0){
             try {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Invoice");
            window.setMinWidth(685);
            window.setMinHeight(605);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Invoice.fxml"));
            AnchorPane pane = loader.load();
            InvoiceController controller = loader.getController();
            controller.init(o);
            Scene scene = new Scene(pane);
            window.setScene(scene);
            window.showAndWait();
            
        } catch (IOException ex) {
            System.out.println(ex);
        }
         }
    }
    
}
