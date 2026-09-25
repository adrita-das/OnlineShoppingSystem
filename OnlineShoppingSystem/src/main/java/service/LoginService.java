package service;

import database.UserDAO;

public class LoginService {

    private final UserDAO dao;

    public LoginService(UserDAO dao) {
        this.dao = dao;
    }

    public boolean validateLogin(String email, String password) {

        if (email == null || email.isEmpty()) {
            return false;
        }

        if (password == null || password.isEmpty()) {
            return false;
        }

        return dao.loginUser(email, password);
    }
}