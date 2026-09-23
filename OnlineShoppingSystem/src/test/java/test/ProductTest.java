package test;

import model.Product;
import org.junit.jupiter.api.Test;
import service.ProductService;

import static org.junit.jupiter.api.Assertions.*;


public class ProductTest {


    @Test
    void validProductTest(){


        Product product =
                new Product(
                        "Laptop",
                        "Electronics",
                        50000,
                        5
                );


        ProductService service =
                new ProductService();


        assertTrue(service.validateProduct(product));

    }



    @Test
    void invalidPriceTest(){


        Product product =
                new Product(
                        "Laptop",
                        "Electronics",
                        -100,
                        5
                );


        ProductService service =
                new ProductService();


        assertFalse(service.validateProduct(product));

    }



    @Test
    void zeroQuantityTest(){


        Product product =
                new Product(
                        "Mobile",
                        "Electronics",
                        20000,
                        0
                );


        ProductService service =
                new ProductService();


        assertFalse(service.validateProduct(product));

    }

}