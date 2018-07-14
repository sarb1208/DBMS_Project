/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;


public class Member {
           private Integer UId ;
           private String Name;
           private String Address;
           private String Mail_id;
           private String Mobile_no;
           public Cart Cart;
           private String Password;
          
           Member(String Uid,String Name,String mobile,String mail,String P,String address){
             
               this.Name = Name;
               this.Address  = address;
               this.Mail_id = mail;
               this.Mobile_no = mobile;
               this.UId = Integer.parseInt(Uid);
               this.Password = P;
               
           }

    Member() {
         
    }
           
           public Integer getUId(){
               return UId;
           }
           public String getName(){
               return Name;
           }
           public String getAddress(){
               return Address;
           }
           public String getMail_id(){
               return Mail_id;
           }
           public String getMobile_no(){
               return Mobile_no;
           }
           public void setAddress(String address){
               this.Address = address;
           }
           
           public void setName(String name){
               Name = name;
           }
           public void setMail_id(String mail){
               Mail_id = mail;
           }
           public void setMobile_no(String mobile){
               Mobile_no = mobile;
           }
           
            public void make_cart(){
                Cart = new Cart(UId);
            }
           
    public void setPassword(String text) {
            Password = text;   
    }

    public void setUID(int string) {
        UId = string;
    }

    public String getPassword() {
        return Password;
    }
           
           
}
