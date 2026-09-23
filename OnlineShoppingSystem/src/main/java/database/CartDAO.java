package database;

import model.Cart;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class CartDAO {


    public boolean addToCart(Cart cart) {


        String sql =
                "INSERT INTO cart(product_id, product_name, price, quantity, total) VALUES (?, ?, ?, ?, ?)";


        try {


            Connection con = DBConnection.getConnection();


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setInt(1, cart.getProductId());

            ps.setString(2, cart.getProductName());

            ps.setDouble(3, cart.getPrice());

            ps.setInt(4, cart.getQuantity());

            ps.setDouble(5, cart.getTotal());



            int result = ps.executeUpdate();


            return result > 0;



        } catch(Exception e) {


            System.out.println(e.getMessage());

            return false;

        }

    }

}