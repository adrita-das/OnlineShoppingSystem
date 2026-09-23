package service;

import model.Cart;

public class CartService {


    public boolean validateCart(Cart cart) {


        if(cart.getProductName() == null || cart.getProductName().isEmpty()) {
            return false;
        }


        if(cart.getPrice() <= 0) {
            return false;
        }


        if(cart.getQuantity() <= 0) {
            return false;
        }


        return true;

    }

}