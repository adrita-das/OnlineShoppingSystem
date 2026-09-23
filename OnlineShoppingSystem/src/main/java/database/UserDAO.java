package database;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {


    // Register User
    public boolean registerUser(User user) {

        String sql = "INSERT INTO users(name, email, password) VALUES (?, ?, ?)";


        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());


            int result = ps.executeUpdate();


            return result > 0;


        } catch(Exception e) {

            System.out.println(e.getMessage());
            return false;
        }
    }



    // Login Check
    public boolean loginUser(String email, String password) {


        String sql = "SELECT * FROM users WHERE email=? AND password=?";


        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);


            ps.setString(1, email);
            ps.setString(2, password);


            ResultSet rs = ps.executeQuery();


            return rs.next();


        } catch(Exception e) {

            System.out.println(e.getMessage());
            return false;
        }
    }
}