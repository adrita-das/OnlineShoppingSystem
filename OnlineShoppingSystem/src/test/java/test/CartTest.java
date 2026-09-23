package test;

import model.Cart;
import org.junit.jupiter.api.Test;
import service.CartService;

import static org.junit.jupiter.api.Assertions.*;


public class CartTest {


    @Test
    void validCartTest(){


        Cart cart =
                new Cart(
                        1,
                        "Laptop",
                        50000,
                        2
                );


        CartService service =
                new CartService();


        assertTrue(service.validateCart(cart));

    }



    @Test
    void invalidQuantityTest(){


        Cart cart =
                new Cart(
                        1,
                        "Laptop",
                        50000,
                        0
                );


        CartService service =
                new CartService();


        assertFalse(service.validateCart(cart));

    }



    @Test
    void invalidPriceTest(){


        Cart cart =
                new Cart(
                        1,
                        "Mobile",
                        -500,
                        2
                );


        CartService service =
                new CartService();


        assertFalse(service.validateCart(cart));

    }

}