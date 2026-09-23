package service;

import model.Product;

public class ProductService {


    public boolean validateProduct(Product product) {


        if(product.getName() == null || product.getName().isEmpty()) {
            return false;
        }


        if(product.getPrice() <= 0) {
            return false;
        }


        if(product.getQuantity() <= 0) {
            return false;
        }


        return true;

    }

}