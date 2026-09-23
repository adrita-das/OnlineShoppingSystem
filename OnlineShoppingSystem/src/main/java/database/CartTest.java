package database;

import model.Cart;


public class CartTest {


    public static void main(String[] args) {


        Cart cart = new Cart(
                1,
                "Laptop",
                50000,
                2
        );


        CartDAO dao = new CartDAO();


        boolean result = dao.addToCart(cart);



        if(result){

            System.out.println("Added to Cart");

        }
        else{

            System.out.println("Failed");

        }

    }

}