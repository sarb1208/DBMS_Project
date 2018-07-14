/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

/**
 *
 * @author SHUBHAM
 */
public class Address {
      private String State;
      private String City;
      private int Pincode;
      private String Street;
      Address(String state,String city,String street,int pincode){
          State = state;
          City = city;
          Pincode = pincode;
          Street = street;
          
      }
      String state(){
          return State;
      }
      String city(){
          return City;
      }
      int pincode(){
          return Pincode;
      }
      String street(){
          return Street;
      }
      void set_state(String state){
          State = state;
      }
      void set_city(String city){
          City = city;
      }
      void set_street(String street){
          Street = street;
      }
      void set_pincode(int pincode){
          Pincode = pincode;
      }
      
}
