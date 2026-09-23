package service;

import database.UserDAO;

public class LoginService {


    public boolean validateLogin(String email, String password) {


        if(email == null || email.isEmpty()) {
            return false;
        }


        if(password == null || password.isEmpty()) {
            return false;
        }


        UserDAO dao = new UserDAO();

        return dao.loginUser(email, password);

    }

}