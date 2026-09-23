package database;

import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class OrderDAO {


    public boolean placeOrder(Order order) {


        String sql =
                "INSERT INTO orders(product_name, quantity, total, payment_status) VALUES (?, ?, ?, ?)";


        try {


            Connection con = DBConnection.getConnection();


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(1, order.getProductName());

            ps.setInt(2, order.getQuantity());

            ps.setDouble(3, order.getTotal());

            ps.setString(4, order.getPaymentStatus());


            int result = ps.executeUpdate();


            return result > 0;


        } catch(Exception e) {


            System.out.println(e.getMessage());

            return false;

        }

    }

}