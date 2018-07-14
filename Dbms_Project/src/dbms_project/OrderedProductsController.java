/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sarbjyot
 */
public class OrderedProductsController implements Initializable {

    @FXML
    private TableView<Oproduct> Table;
    @FXML
    private TableColumn<Oproduct,Integer> columnPId;
    @FXML
    private TableColumn<Oproduct,Integer> columnQuantity;
    private ObservableList<Oproduct> data;
    /**
     * Initializes the controller class.
     */
    Connection connection = null;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         connection = SqliteConnection.Connector();
       if (connection == null) {

       System.out.println("connection not successful");
      System.exit(1);}
       Table.setRowFactory( tv -> {
    TableRow<Oproduct> row = new TableRow<>();
    row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
            Oproduct rowData = row.getItem();
            fun(rowData.getPId());
        }
    });
    return row ;
});
       //init();
    }    

    public void init(String oid) {
         try{
            data = FXCollections.observableArrayList();
            String query = "Select PId,Quantity from Ordered_Items where OId = ?";
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, oid);
            rs = pst.executeQuery();
              while (rs.next()) {
                //get string from db,whichever way 
                Oproduct p = new Oproduct(rs.getString("PId"),rs.getInt("Quantity"));
                data.add(p);
            }
              rs.close();
              pst.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
           columnPId.setCellValueFactory(new PropertyValueFactory<>("PId"));
           columnQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
          




          Table.setItems(null);
          Table.setItems(data); 
    }
     public Image convertToImage(ResultSet rs){
        Image image = null;
                try {
           
                 InputStream is= rs.getBinaryStream("photo");
                  OutputStream os=new FileOutputStream(new File("photo.jpg"));
                 byte [] content= new byte[1024];
                 int size=0;


        while ((size=is.read(content))!=-1){

            os.write(content, 0, size);
        }

    os.close();
    is.close();
    image = new Image("file:photo.jpg",360,374,true,true);
     // img00.setImage(image);
      //img00.setPreserveRatio(true);
      //System.out.println("Image Converted");
         
     
            
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (FileNotFoundException ex) {
             System.out.println(ex);
            
        } catch (IOException ex) {
          System.out.println(ex);

        }
          return image;    
     }

    public void fun(String pId) {
        try{
            String query = "Select * from Inventory where PId = ?";
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, pId);
            rs = pst.executeQuery();
             if(rs.next()){
             Product p = new Product(rs.getString("PId"),rs.getString("Name"),rs.getString("Description"),rs.getFloat("Price"),rs.getString("Manufacturer"),rs.getInt("Quantity"),rs.getString("VId"));
               p.setphoto(convertToImage(rs));
                 try {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Product Details");
            window.setMinWidth(857);
            window.setMinHeight(589);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ProductDetails.fxml"));
            AnchorPane pane = loader.load();
            ProductDetailsController controller = loader.getController();
            controller.init(window);
            controller.init(null, p,2, 0);
            Scene scene = new Scene(pane);
            window.setScene(scene);
            window.showAndWait();
            //userName.setText(member.Name());
             rs.close();
              pst.close();
            
        } catch (IOException ex) {
            System.out.println(ex);
        }
             }
              
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
}
