package service;

import java.util.regex.Pattern;

public class RegisterService {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).+$");


    public boolean validateRegistration(
            String name,
            String email,
            String password,
            String confirmPassword) {

        // Name
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (name.trim().length() < MIN_NAME_LENGTH ||
                name.trim().length() > MAX_NAME_LENGTH) {
            return false;
        }

        // Email
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return false;
        }

        // Password
        if (password == null || password.isEmpty()) {
            return false;
        }

        if (password.length() < MIN_PASSWORD_LENGTH ||
                password.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return false;
        }

        // Confirm password
        if (confirmPassword == null ||
                !password.equals(confirmPassword)) {
            return false;
        }

        return true;
    }
}