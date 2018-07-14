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
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController implements Initializable{

    Connection connection = null;
    private Member member = null;
    private int low = 0;
    private int high = 0;
    private int no_pages = 0;
    private int current_page = 0;
    private int x = 0;
    final ToggleGroup group = new ToggleGroup();
    int mode  =0;
    @FXML
    private Label userName;
    @FXML
    private ImageView img00;
    @FXML
    private ImageView img01;
     @FXML
    private ImageView img02;
   
       @FXML
    private ImageView img10;
        @FXML
    private ImageView img11;
    
     
    @FXML
    private ToggleButton MyOrdersButton;
    @FXML
    private BorderPane rootPane;
    @FXML
    private ToggleButton cartButton;
    @FXML
    private ImageView logo;
    @FXML
    private ToggleButton Account;
    
    private ObservableList<Product> data;
    private ObservableList<ImageView> images;
    @FXML
    private Button logoutButton;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Button previousbutton;
    @FXML
    private Button nextbutton;
    @FXML
    private ImageView img12;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    @FXML
    private ToggleButton home;
    Map<Product,Integer> map;
    @FXML
    private Pane Tpane;
    @FXML
    private GridPane grid;
    @FXML
    private MenuItem menButton;
    @FXML
    private MenuItem booksButton;
    @FXML
    private MenuItem electronicsButton;
    @FXML
    private MenuItem womenButton;
    @FXML
    private MenuItem sports;
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
    image = new Image("file:photo.jpg",img00.getFitWidth(),img00.getFitHeight(),true,true);
     // img00.setImage(image);
      //img00.setPreserveRatio(true);
      System.out.println("Image Converted");
         
     
            
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (FileNotFoundException ex) {
             System.out.println(ex);
            
        } catch (IOException ex) {
          System.out.println(ex);

        }
          return image;    
     }
    @FXML
    void Cart(MouseEvent event) {
        this.cartButton.setSelected(true);
        Tpane.getChildren().setAll(grid);
        mode = 1;
             low = 0;
             high = 0;
             x = 0;
             no_pages = 0;
             current_page = 0;
             previousbutton.setDisable(true);
             nextbutton.setDisable(false);
             member.Cart.loadCart();
             map = member.Cart.get_Cart();
             int size = map.size();
             data = FXCollections.observableArrayList();
             Iterator it = map.entrySet().iterator();
             int no_rows = 0;
             while(it.hasNext()){
                 Map.Entry pair = (Map.Entry)it.next();
                  Product p = (Product)pair.getKey();
                 data.add(p);
                 no_rows++;
                  if(no_rows<=6){
                    images.get(no_rows-1).setImage(p.getPhoto());
                    images.get(no_rows-1).setPreserveRatio(true);
                    //img02.setImage(p.getPhoto());
                   // img02.setPreserveRatio(true);
                     images.get(no_rows-1).setDisable(false);
                       images.get(no_rows-1).setCursor(Cursor.HAND);
                    high = no_rows-1;
                }
                 //it.remove();
                 
             }
             int k = no_rows;
                for(;k<6;k++){
                    images.get(k).setImage(null);
                images.get(k).setCursor(Cursor.DEFAULT);
                images.get(k).setDisable(true);
                }
                no_pages = no_rows/6;
              x = no_rows;
              if(no_rows<=6){
                 
                  nextbutton.setDisable(true);
              }
             
             
             
    }
    void init(Member member){
          userName.setText(member.getName());
          this.member = member;
          int no_rows = 0;
               try{
            data = FXCollections.observableArrayList();
            String query = "Select PId,Name,Description,Price,Manufacturer,Quantity,VId,photo from Inventory ";
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(query);
            rs = pst.executeQuery();
            reloadImages(rs);
            pst.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch(Exception e){
            System.out.println(e);
        }
               
       
    }
    
    
   

    private void close(MouseEvent event) {
         System.exit(0);
    }

   

    @FXML
    private void EditDetails(MouseEvent event) {
        try {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("My Account");
            window.setMinWidth(857);
            window.setMinHeight(589);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Member_Details.fxml"));
            AnchorPane pane = loader.load();
            Member_DetailsController controller = loader.getController();
            controller.init(member);
            Scene scene = new Scene(pane);
            window.setScene(scene);
            window.showAndWait();
            userName.setText(member.getName());
            this.Account.setSelected(false);
            this.home.setSelected(true);
            
        } catch (IOException ex) {
            System.out.println(ex);
        }
           
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
          connection = SqliteConnection.Connector();
       if (connection == null) {

       System.out.println("connection not successful");
      System.exit(1);}
        images = FXCollections.observableArrayList();
        images.add(img00);
        images.add(img01);
         images.add(img02);
        
         images.add(img10);
         images.add(img11);
         images.add(img12);
        
         previousbutton.setDisable(true);
         home.setToggleGroup(group);
               home.setSelected(true);
               this.MyOrdersButton.setToggleGroup(group);
               this.Account.setToggleGroup(group);
               this.cartButton.setToggleGroup(group);
        
        
        
}

    @FXML
    private void logout(MouseEvent event) {
        try {
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
          AnchorPane pane = loader.load();
          anchor.getChildren().setAll(pane);
      } catch (IOException ex) {
          System.out.println(ex);
      }
    }
    @FXML
    public void nextPage(){
            low  = high+1;
            high = high+6;
            if(high>x-1){
                high = x-1;
            }
            int i = low;
            int j = 0;
            for(i=low;i<=high;i++,j++){
                images.get(j).setDisable(false);
                images.get(j).setImage(data.get(i).getPhoto());
                  images.get(j).setCursor(Cursor.HAND);
            }
               for(;j<6;j++){
                images.get(j).setImage(null);
                 images.get(j).setCursor(Cursor.DEFAULT);
                images.get(j).setDisable(true);
            }
            current_page++;
            if(current_page==no_pages)
                nextbutton.setDisable(true);
            previousbutton.setDisable(false);
    }
    @FXML
    public void previousPage(){
        high = low-1;
        low = low-6;
        if(low<0){
         low = 0;   
        }
         int i = low;
            int j = 0;
            for(i=low;i<=high;i++,j++){
                images.get(j).setDisable(false);
                images.get(j).setImage(data.get(i).getPhoto());
                 images.get(j).setCursor(Cursor.HAND);
                
            }
            for(;j<6;j++){
                images.get(j).setImage(null);
                images.get(j).setCursor(Cursor.DEFAULT);
                images.get(j).setDisable(true);
              
            }
             current_page--;
             if(current_page==0)
                  previousbutton.setDisable(true);
             nextbutton.setDisable(false);
        
    }
    public void loadProduct(int i){
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
            if(mode==1)
            controller.init(member,data.get(i),mode,map.get(data.get(i)));
            else
            controller.init(member,data.get(i),mode,0);
            //controller.init();
            Scene scene = new Scene(pane);
            window.setScene(scene);
            window.showAndWait();
            //userName.setText(member.Name());
           
            
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    @FXML
    private void img00action(MouseEvent event) {
        loadProduct(low);   
    }

    @FXML
    private void img01action(MouseEvent event) {
         loadProduct(low+1);   
    }

    @FXML
    private void img02action(MouseEvent event) {
         loadProduct(low+2);   
    }

    @FXML
    private void img10action(MouseEvent event) {
         loadProduct(low+3);   
    }

    @FXML
    private void img11action(MouseEvent event) {
         loadProduct(low+4);   
    }

    @FXML
    private void img12action(MouseEvent event) {
         loadProduct(low+5);   
    }

    @FXML
    private void search(MouseEvent event) {
        mode = 0;
        current_page = 0;
        no_pages = 0;
        x = 0;
        low = 0;
        high = 0;
        previousbutton.setDisable(true);
        nextbutton.setDisable(false);
       int no_rows = 0;
               try{
            data = FXCollections.observableArrayList();
            String query = "Select PId,Name,Description,Price,Manufacturer,Quantity,VId,photo from Inventory where Description like"
                    + "'%"+searchField.getText()+"%'";
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(query);
            rs = pst.executeQuery();
            reloadImages(rs);
              pst.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch(Exception e){
            System.out.println(e);
        }
          
    }
    public void reloadImages(ResultSet rs) throws SQLException{
         current_page = 0;
        no_pages = 0;
        x = 0;
        low = 0;
        high = 0;
        previousbutton.setDisable(true);
        nextbutton.setDisable(false);
       int no_rows = 0;
        while (rs.next()) {
                //get string from db,whichever way 
                Product p = new Product(rs.getString("PId"),rs.getString("Name"),rs.getString("Description"),rs.getFloat("Price"),rs.getString("Manufacturer"),rs.getInt("Quantity"),rs.getString("VId"));
                p.setphoto(convertToImage(rs));
                data.add(p);
                no_rows++;
                System.out.println("Hii");
                if(no_rows<=6){
                    images.get(no_rows-1).setImage(p.getPhoto());
                    images.get(no_rows-1).setPreserveRatio(true);
                    //img02.setImage(p.getPhoto());
                   // img02.setPreserveRatio(true);
                    images.get(no_rows-1).setDisable(false);
                       images.get(no_rows-1).setCursor(Cursor.HAND);
                    high = no_rows-1;
                }
                
            }
               int k = no_rows;
                for(;k<6;k++){
                    images.get(k).setImage(null);
                images.get(k).setCursor(Cursor.DEFAULT);
                images.get(k).setDisable(true);
                }
              rs.close();
            
              no_pages = no_rows/6;
              x = no_rows;
              if(no_rows<=6){
                 
                  nextbutton.setDisable(true);
              }
       
        
    }

    @FXML
    private void home(MouseEvent event) {
        this.home.setSelected(true);
        mode = 0;
        Tpane.getChildren().setAll(grid);
        init(member);
    }

    @FXML
    private void Order(MouseEvent event) {
        this.MyOrdersButton.setSelected(true);
        /*try{
           FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MyOrders.fxml"));
            AnchorPane pane = loader.load();
            rootPane.getChildren().setAll(pane);
      } catch (IOException ex) {
            Logger.getLogger(MyOrdersController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
         FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("OrdersTable.fxml"));
        
        try{
            Pane pane = loader.load();
            OrdersTableController controller = loader.getController();
            controller.init(Integer.toString(this.member.getUId()));
            Tpane.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println(ex);
      }
            
    }

    @FXML
    private void Men(ActionEvent event) {
        mode = 0;
        current_page = 0;
        no_pages = 0;
        x = 0;
        low = 0;
        high = 0;
        previousbutton.setDisable(true);
        nextbutton.setDisable(false);
       int no_rows = 0;
               try{
            data = FXCollections.observableArrayList();
            String query = "Select PId,Name,Description,Price,Manufacturer,Quantity,VId,photo from Inventory where Description like"
                    + "'%"+"Men"+"%'";
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(query);
            rs = pst.executeQuery();
            reloadImages(rs);
              pst.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch(Exception e){
            System.out.println(e);
        }
        
        
    }

    @FXML
    private void Books(ActionEvent event) {
         mode = 0;
        current_page = 0;
        no_pages = 0;
        x = 0;
        low = 0;
        high = 0;
        previousbutton.setDisable(true);
        nextbutton.setDisable(false);
       int no_rows = 0;
               try{
            data = FXCollections.observableArrayList();
            String query = "Select PId,Name,Description,Price,Manufacturer,Quantity,VId,photo from Inventory where Description like"
                    + "'%"+"Book"+"%'";
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(query);
            rs = pst.executeQuery();
            reloadImages(rs);
              pst.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    @FXML
    private void Electronics(ActionEvent event) {
         mode = 0;
        current_page = 0;
        no_pages = 0;
        x = 0;
        low = 0;
        high = 0;
        previousbutton.setDisable(true);
        nextbutton.setDisable(false);
       int no_rows = 0;
               try{
            data = FXCollections.observableArrayList();
            String query = "Select PId,Name,Description,Price,Manufacturer,Quantity,VId,photo from Inventory where Description like"
                    + "'%"+"Electronics"+"%'";
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(query);
            rs = pst.executeQuery();
            reloadImages(rs);
              pst.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    @FXML
    private void Women(ActionEvent event) {
         mode = 0;
        current_page = 0;
        no_pages = 0;
        x = 0;
        low = 0;
        high = 0;
        previousbutton.setDisable(true);
        nextbutton.setDisable(false);
       int no_rows = 0;
               try{
            data = FXCollections.observableArrayList();
            String query = "Select PId,Name,Description,Price,Manufacturer,Quantity,VId,photo from Inventory where Description like"
                    + "'%"+"Woman"+"%'";
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(query);
            rs = pst.executeQuery();
            reloadImages(rs);
              pst.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    @FXML
    private void Sports(ActionEvent event) {
         mode = 0;
        current_page = 0;
        no_pages = 0;
        x = 0;
        low = 0;
        high = 0;
        previousbutton.setDisable(true);
        nextbutton.setDisable(false);
       int no_rows = 0;
               try{
            data = FXCollections.observableArrayList();
            String query = "Select PId,Name,Description,Price,Manufacturer,Quantity,VId,photo from Inventory where Description like"
                    + "'%"+"Sports"+"%'";
            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(query);
            rs = pst.executeQuery();
            reloadImages(rs);
              pst.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
