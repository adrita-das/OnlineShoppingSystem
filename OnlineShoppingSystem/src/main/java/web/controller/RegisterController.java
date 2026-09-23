package web.controller;

import database.UserDAO;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.regex.Pattern;

@Controller
public class RegisterController {

    // Name validation
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;

    // Password validation
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;

    // Email validation
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    // Password must contain at least one letter and one number
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).+$");


    // Show Register Page
    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        if (!model.containsAttribute("name")) {
            model.addAttribute("name", "");
        }

        if (!model.containsAttribute("email")) {
            model.addAttribute("email", "");
        }

        return "register";
    }


    // Process Registration
    @PostMapping("/register")
    public String handleRegister(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        // Remove unnecessary spaces
        String trimmedName = name == null ? "" : name.trim();
        String trimmedEmail = email == null ? "" : email.trim();

        // Keep entered values in the form
        model.addAttribute("name", trimmedName);
        model.addAttribute("email", trimmedEmail);


        // -----------------------------------------
        // Name validation
        // -----------------------------------------

        if (trimmedName.isEmpty()) {

            model.addAttribute(
                    "error",
                    "Enter your full name."
            );

            return "register";
        }

        if (trimmedName.length() < MIN_NAME_LENGTH ||
                trimmedName.length() > MAX_NAME_LENGTH) {

            model.addAttribute(
                    "error",
                    "Name must be between "
                            + MIN_NAME_LENGTH
                            + " and "
                            + MAX_NAME_LENGTH
                            + " characters."
            );

            return "register";
        }


        // -----------------------------------------
        // Email validation
        // -----------------------------------------

        if (trimmedEmail.isEmpty()) {

            model.addAttribute(
                    "error",
                    "Enter your email address."
            );

            return "register";
        }

        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {

            model.addAttribute(
                    "error",
                    "Enter a valid email, like name@example.com."
            );

            return "register";
        }


        // -----------------------------------------
        // Password validation
        // -----------------------------------------

        if (password == null || password.isEmpty()) {

            model.addAttribute(
                    "error",
                    "Enter a password."
            );

            return "register";
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {

            model.addAttribute(
                    "error",
                    "Password must be at least "
                            + MIN_PASSWORD_LENGTH
                            + " characters. Currently "
                            + password.length()
                            + "."
            );

            return "register";
        }

        if (password.length() > MAX_PASSWORD_LENGTH) {

            model.addAttribute(
                    "error",
                    "Password cannot exceed "
                            + MAX_PASSWORD_LENGTH
                            + " characters. Currently "
                            + password.length()
                            + "."
            );

            return "register";
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {

            model.addAttribute(
                    "error",
                    "Password must contain at least one letter and one number."
            );

            return "register";
        }


        // -----------------------------------------
        // Confirm Password validation
        // -----------------------------------------

        if (confirmPassword == null ||
                !password.equals(confirmPassword)) {

            model.addAttribute(
                    "error",
                    "Passwords do not match."
            );

            return "register";
        }


        // -----------------------------------------
        // Save user to database
        // -----------------------------------------

        User user = new User(
                trimmedName,
                trimmedEmail,
                password
        );

        UserDAO dao = new UserDAO();

        boolean success = dao.registerUser(user);


        // -----------------------------------------
        // Registration result
        // -----------------------------------------

        if (success) {

            return "redirect:/login";

        } else {

            model.addAttribute(
                    "error",
                    "Could not create account. That email may already be in use."
            );

            return "register";
        }
    }
}