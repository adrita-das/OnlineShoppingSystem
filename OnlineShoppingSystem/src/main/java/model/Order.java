package model;

public class Order {

    private int id;
    private String productName;
    private int quantity;
    private double total;
    private String paymentStatus;


    public Order(String productName, int quantity, double total, String paymentStatus) {

        this.productName = productName;
        this.quantity = quantity;
        this.total = total;
        this.paymentStatus = paymentStatus;

    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getProductName() {
        return productName;
    }


    public int getQuantity() {
        return quantity;
    }


    public double getTotal() {
        return total;
    }


    public String getPaymentStatus() {
        return paymentStatus;
    }

}
