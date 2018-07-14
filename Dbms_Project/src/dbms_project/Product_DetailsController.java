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
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sarbjyot
 */
public class Product_DetailsController implements Initializable {
    Connection connection = null;
    @FXML
    private Label PId;
    @FXML
    private Label Name;
    @FXML
    private Label Description;
    @FXML
    private Label Price;
    @FXML
    private Label Manufacturer;
    @FXML
    private Label Quantity;
    @FXML
    private Label Vid;
    
    @FXML
    private TextField pidField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField manufacturerField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField vidField;
    @FXML
    private Button Upload;
    @FXML
    private Label status;
    @FXML
    private Button Addbutton;
    Product product = null;
    File selectedFile = null;
    @FXML
    private Label status1;
    @FXML
    private Button editbutton;
    @FXML
    private Button savebutton;
    Stage w = null;
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
           System.out.println("Product Details  Connected");
       }
       this.editbutton.setDisable(true);
       this.editbutton.setVisible(false);
       this.savebutton.setDisable(true);
       this.savebutton.setVisible(false);
       
    }    
    public void init(Product p){
        this.product = p;
        this.pidField.setText(p.getPId());
        this.nameField.setText(p.getName());
        this.pidField.setEditable(false);
        
        this.nameField.setEditable(false);
        this.manufacturerField.setText(p.getManufacturer());
        this.descriptionField.setText(p.getDescription());
        this.quantityField.setText(Integer.toString(p.getQuantity()));
        this.priceField.setText(Float.toString(p.getPrice()));
        this.vidField.setText(p.getVId());
        this.manufacturerField.setEditable(false);
        this.descriptionField.setEditable(false);
        this.quantityField.setEditable(false);
        this.priceField.setEditable(false);
        this.vidField.setEditable(false);
        this.Upload.setDisable(true);
        this.Upload.setVisible(false);
        this.status.setVisible(false);
        this.Addbutton.setDisable(true);
        this.Addbutton.setVisible(false);
        
        this.editbutton.setDisable(false);
       this.editbutton.setVisible(true);
       this.savebutton.setDisable(false);
       this.savebutton.setVisible(true);
        
    }
    public void init(Stage w){
        this.w = w;
    }

    @FXML
    private void imageUpload(MouseEvent event) {
        FileChooser fc = new FileChooser();
        
         fc.getExtensionFilters().addAll(new ExtensionFilter("JPG files" ,"*.jpg"));
          selectedFile = fc.showOpenDialog(null);
         if(selectedFile == null){
             System.out.println("Failed to upload");
         }
         else{
             status.setText(selectedFile.getName());
         }
        
    }
    @FXML
    private void addProduct(MouseEvent event) {
         PreparedStatement ps  = null;
        if(pidField.getText().length()<1||nameField.getText().length()<1||descriptionField.getText().length()<1||priceField.getText().length()<1||manufacturerField.getText().length()<1||quantityField.getText().length()<1||vidField.getText().length()<1||selectedFile==null){
            status1.setText("No Field can be null");
        }else{
             try {
                 FileInputStream fis;
                 int num_rows = 0;
                 fis = new FileInputStream(selectedFile);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 byte[] buf = new byte[1024];
                 for (int readNum; (readNum = fis.read(buf)) != -1;){
                     bos.write(buf, 0, readNum);
                 }
                 fis.close();
                
                String query = "Insert into Inventory(PId,Name,Description,Price,Manufacturer,Quantity,VId,photo) values(?,?,?,?,?,?,?,?)";
                ps = connection.prepareStatement(query);
                ps.setString(1,pidField.getText());
                ps.setString(2,nameField.getText());
                ps.setString(3,descriptionField.getText());
                ps.setFloat(4,Float.parseFloat(priceField.getText()));
                ps.setString(5,manufacturerField.getText());
                ps.setInt(6,Integer.parseInt(quantityField.getText()));
                ps.setString(7,vidField.getText());
                ps.setBytes(8, bos.toByteArray());
                num_rows = ps.executeUpdate();
			if(num_rows>0){
				System.out.println("Data has been inserted");
			}
			ps.close();
                nameField.clear();
                descriptionField.clear();
                priceField.clear();
                manufacturerField.clear();
                quantityField.clear();
                vidField.clear();
                pidField.clear();
             } catch (FileNotFoundException ex) {
                 System.out.println(ex);
             } catch (IOException ex) {
                   System.out.println(ex);
             } catch (SQLException ex) {
                 System.out.println(ex);
             }
        }
    }

    @FXML
    private void save(MouseEvent event) {
        if(this.Upload.isDisabled()==true){
            w.close();
        }
         PreparedStatement ps  = null;
        if(pidField.getText().length()<1||nameField.getText().length()<1||descriptionField.getText().length()<1||priceField.getText().length()<1||manufacturerField.getText().length()<1||quantityField.getText().length()<1||vidField.getText().length()<1){
            status1.setText("No Field can be null");
        }else{
             try {
                  int num_rows = 0;
                /* FileInputStream fis;
                
                 fis = new FileInputStream(selectedFile);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 byte[] buf = new byte[1024];
                 for (int readNum; (readNum = fis.read(buf)) != -1;){
                     bos.write(buf, 0, readNum);
                 }
                 fis.close();*/
                
                String query = "Update Inventory set PId = ?, Name = ?,Description = ?,Price = ?,Manufacturer = ?,Quantity = ?,VId = ? where PId ="+product.getPId();
                ps = connection.prepareStatement(query);
                ps.setString(1,pidField.getText());
                ps.setString(2,nameField.getText());
                ps.setString(3,descriptionField.getText());
                ps.setFloat(4,Float.parseFloat(priceField.getText()));
                ps.setString(5,manufacturerField.getText());
                ps.setInt(6,Integer.parseInt(quantityField.getText()));
                ps.setString(7,vidField.getText());
               // ps.setBytes(8, bos.toByteArray());
                num_rows = ps.executeUpdate();
			if(num_rows>0){
				System.out.println("Data has been inserted");
			}
			ps.close();
               w.close();
             } catch (SQLException ex) {
                 System.out.println(ex);
             }
        }
        
    }

    @FXML
    private void edit(MouseEvent event) {
        
         this.manufacturerField.setEditable(true);
        this.descriptionField.setEditable(true);
        this.quantityField.setEditable(true);
        this.priceField.setEditable(true);
        this.vidField.setEditable(true);
        this.pidField.setEditable(true);
        
        this.nameField.setEditable(true);
    }
    
}
