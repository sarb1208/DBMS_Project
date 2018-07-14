/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

/**
 *
 * @author iiita
 */
public class Admin {
    private String AId;
    private String Name;
    private String Password;
    private String Email;
    Admin(String aid,String name,String e,String password){
        AId = aid;
        Name = name;
        Password = password; 
        Email = e;
    }
    public String getEmail(){
        return Email;
    }
    public void setEmail(String e){
        this.Email = e;
    }
    public String getAId(){
        return AId;
    }
    public String getName(){
        return Name;
    }
    public String getPassword(){
        return Password;
    }
    public void setPassword(String password){
        Password = password;
    }
    public void setName(String n){
        this.Name = n;
    }
    
    
}
