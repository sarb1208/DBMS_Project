/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author sarbjyot
 */
public class ProductDetailsController implements Initializable {

    @FXML
    private ImageView image;
    @FXML
    private Label nameField;
    @FXML
    private Label manufacturerFIeld;
    @FXML
    private Label priceField;
    @FXML
    private Label stock;
    @FXML
    private TextArea description;
    private Member member = null;
    private Product product = null;
    @FXML
    private Button cartButton;
    @FXML
    private Button shopButton;
    @FXML
    private Label quantityField;
    @FXML
    private Button incrementbutton;
    @FXML
    private Button decrementbutton;
    @FXML
    private Button removebutton;
    @FXML
    private Label quantity;
    Stage w;
    private int mode = 0;
    /**er
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    public void init(Stage w){
        this.w = w;
    }
    public void init(Member member,Product product,int mode,int q){
        this.mode = mode;
        if(mode==0){
            this.removebutton.setDisable(true);
            this.removebutton.setVisible(false);
            this.decrementbutton.setDisable(true);
            this.decrementbutton.setVisible(false);
            this.incrementbutton.setDisable(true);
            this.incrementbutton.setVisible(false);
            this.quantityField.setVisible(false);
            this.quantityField.setDisable(true);
            this.quantity.setDisable(true);
            this.quantity.setVisible(false);
        }else if (mode==1){
           if(q==1)
                this.decrementbutton.setDisable(true);
            this.quantityField.setText(Integer.toString(q));
           if(q==product.getQuantity())
               incrementbutton.setDisable(true);
           
            this.cartButton.setDisable(true);
            this.cartButton.setVisible(false);
           
        }else{
            this.cartButton.setDisable(true);
            this.cartButton.setVisible(false);
            this.shopButton.setDisable(true);
            this.shopButton.setVisible(false);
            this.removebutton.setDisable(true);
            this.removebutton.setVisible(false);
            this.quantityField.setVisible(false);
            this.quantityField.setDisable(true);
            this.decrementbutton.setDisable(true);
            this.decrementbutton.setVisible(false);
            this.incrementbutton.setDisable(true);
            this.incrementbutton.setVisible(false);
            this.quantity.setDisable(true);
            this.quantity.setVisible(false);
            
        }
        this.member = member;
        this.product = product;
        
        this.nameField.setText(product.getName());
        this.manufacturerFIeld.setText(product.getManufacturer());
        this.priceField.setText(Float.toString(product.getPrice()));
        this.image.setImage(product.getPhoto());
        if(product.getQuantity()>0){
            this.stock.setText("Yes");
        }else{
            this.shopButton.setDisable(true);
            this.cartButton.setDisable(true);
            this.stock.setText("No");
        }
        this.description.setText(product.getDescription());
                  
        
    }

    @FXML
    private void addtoCart(MouseEvent event) {
        member.Cart.increment(product);
        w.close();
        
    }

    @FXML
    private void showNow(MouseEvent event) {
        try {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Order");
            window.setMinWidth(600);
            window.setMinHeight(400);
            if(mode==0){
                 member.Cart.loadCart();
                Integer i = member.Cart.get_Cart().get(product);
                if(i==null){
                    member.Cart.increment(product);
                    member.Cart.loadCart();
                }
                
               
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ShopNow.fxml"));
            AnchorPane pane = loader.load();
            ShopNowController controller = loader.getController();
            controller.init(Integer.toString(member.getUId()),window);
            Scene scene = new Scene(pane);
            window.setScene(scene);
            window.showAndWait();
            w.close();
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    @FXML
    private void remove(MouseEvent event) {
        member.Cart.delete(product);
        w.close();
        
        
    }

    @FXML
    private void incrementAction(MouseEvent event) {
        int i = Integer.parseInt(quantityField.getText())+1;
        int j = product.getQuantity();
      
        member.Cart.increment(product);
        quantityField.setText(Integer.toString(i));
        if(i>1){
            decrementbutton.setDisable(false);
        }
          if(i==j)
        {
            incrementbutton.setDisable(true);
        }
        
    }

    @FXML
    private void decrementAction(MouseEvent event) {
         int i = Integer.parseInt(quantityField.getText())-1;
         member.Cart.decrement(product);
        quantityField.setText(Integer.toString(i));
        incrementbutton.setDisable(false);
        if(i==1){
            decrementbutton.setDisable(true);
        }
    }
    
    
    
    
}
