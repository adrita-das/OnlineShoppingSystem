package database;

import model.Order;


public class OrderTest {


    public static void main(String[] args) {


        Order order = new Order(
                "Laptop",
                2,
                100000,
                "Paid"
        );


        OrderDAO dao = new OrderDAO();


        boolean result = dao.placeOrder(order);



        if(result){

            System.out.println("Order Placed");

        }
        else{

            System.out.println("Order Failed");

        }

    }

}