package database;

import model.User;

public class UserTest {

    public static void main(String[] args) {


        User user = new User(
                "Sushen",
                "sushenc87@gmail.com",
                "12345"
        );


        UserDAO dao = new UserDAO();


        boolean result = dao.registerUser(user);


        if(result){
            System.out.println("User Registered");
        }
        else{
            System.out.println("Registration Failed");
        }

    }
}