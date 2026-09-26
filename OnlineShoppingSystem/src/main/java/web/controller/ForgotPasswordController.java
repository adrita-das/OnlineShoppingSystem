package web.controller;


import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForgotPasswordController {

    @GetMapping("/forgot-password")
    public String showForgetPasswordPage(Model model){

        model.addAttribute("email" , "");
        return "forgot-password";

    }

}
