/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author SHUBHAM
 */
public class Inventory {
     private Map<Product,Integer> map; 
     
     Inventory(){
         this.map = new HashMap<>();
     }
     
    void increment(Product product){
          Integer count = map.get(product);
          if(count==null)
              map.put(product,1);
          else
              map.put(product,count + 1);
      }
      void add(Product product ,Integer quantity){
         map.put(product, quantity);
      }
      void delete(Product product){
          map.remove(product);
      }
      void decrement(Product product){
          Integer count = map.get(product);
          if(count!=null)
              map.put(product,count-1);
      }
      Map<Product,Integer> get_Inventory(){
          return map;
      }
}
