package test;

import model.Order;
import org.junit.jupiter.api.Test;
import service.OrderService;

import static org.junit.jupiter.api.Assertions.*;


public class OrderTest {


    @Test
    void validOrderTest(){


        Order order =
                new Order(
                        "Laptop",
                        2,
                        100000,
                        "Paid"
                );


        OrderService service =
                new OrderService();


        assertTrue(service.validateOrder(order));

    }



    @Test
    void invalidQuantityTest(){


        Order order =
                new Order(
                        "Laptop",
                        0,
                        100000,
                        "Paid"
                );


        OrderService service =
                new OrderService();


        assertFalse(service.validateOrder(order));

    }



    @Test
    void invalidPaymentTest(){


        Order order =
                new Order(
                        "Laptop",
                        2,
                        100000,
                        ""
                );


        OrderService service =
                new OrderService();


        assertFalse(service.validateOrder(order));

    }

}