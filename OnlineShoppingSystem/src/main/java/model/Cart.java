package model;

public class Cart {

    private int id;
    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private double total;


    public Cart(int productId, String productName, double price, int quantity) {

        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.total = price * quantity;

    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public int getProductId() {
        return productId;
    }


    public String getProductName() {
        return productName;
    }


    public double getPrice() {
        return price;
    }


    public int getQuantity() {
        return quantity;
    }


    public double getTotal() {
        return total;
    }

}