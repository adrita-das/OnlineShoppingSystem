package database;

import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class ProductDAO {


    public boolean addProduct(Product product) {


        String sql =
                "INSERT INTO products(name, category, price, quantity) VALUES (?, ?, ?, ?)";


        try {


            Connection con = DBConnection.getConnection();


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getQuantity());


            int result = ps.executeUpdate();


            return result > 0;


        } catch(Exception e) {


            System.out.println(e.getMessage());

            return false;

        }

    }

}