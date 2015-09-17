package info.androidhive.slidingmenu.library;

import java.util.ArrayList;

public class ModelCart{
     
   private  ArrayList<ModelProducts> cartProducts = new ArrayList<ModelProducts>();
     
 
   public ModelProducts getProducts(int pPosition) {
         
        return cartProducts.get(pPosition);
    }
     
    public void setProducts(ModelProducts Products) {
        
        cartProducts.add(Products);
         
    }
    
    public void removeProducts(ModelProducts Products) {
        
        cartProducts.remove(Products);
         
    }
     
    public int getCartSize() {
            
        return cartProducts.size();
         
    }
  
    public boolean checkProductInCart(ModelProducts aProduct) {
            
        return cartProducts.contains(aProduct);
         
    }
 
}
