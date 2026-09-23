package service;

import model.Order;

public class OrderService {


    public boolean validateOrder(Order order) {


        if(order.getProductName() == null || order.getProductName().isEmpty()) {
            return false;
        }


        if(order.getQuantity() <= 0) {
            return false;
        }


        if(order.getTotal() <= 0) {
            return false;
        }


        if(order.getPaymentStatus() == null ||
                order.getPaymentStatus().isEmpty()) {

            return false;
        }


        return true;

    }

}