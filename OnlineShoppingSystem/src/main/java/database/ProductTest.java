package database;

import model.Product;


public class ProductTest {


    public static void main(String[] args) {


        Product product = new Product(
                "Laptop",
                "Electronics",
                50000,
                10
        );


        ProductDAO dao = new ProductDAO();


        boolean result = dao.addProduct(product);



        if(result){

            System.out.println("Product Added");

        }
        else{

            System.out.println("Failed");

        }

    }

}
