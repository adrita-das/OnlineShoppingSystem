package web.controller;

import database.UserDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.regex.Pattern;

@Controller
public class LoginController {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    @GetMapping("/")
    public String showRoot() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("email")) {
            model.addAttribute("email", "");
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {

        String trimmedEmail = email == null ? "" : email.trim();
        model.addAttribute("email", trimmedEmail);

        if (trimmedEmail.isEmpty()) {
            model.addAttribute("error", "Enter your email address.");
            return "login";
        }
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            model.addAttribute("error", "Enter a valid email, like name@example.com.");
            return "login";
        }
        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "Enter your password.");
            return "login";
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            model.addAttribute("error",
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters. Currently " + password.length() + ".");
            return "login";
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            model.addAttribute("error",
                    "Password cannot exceed " + MAX_PASSWORD_LENGTH + " characters. Currently " + password.length() + ".");
            return "login";
        }

        UserDAO dao = new UserDAO();
        boolean success = dao.loginUser(trimmedEmail, password);

        if (success) {
            session.setAttribute("userEmail", trimmedEmail);
            return "redirect:/catalog";
        } else {
            model.addAttribute("error", "That email and password don't match an account.");
            return "login";
        }
    }
}