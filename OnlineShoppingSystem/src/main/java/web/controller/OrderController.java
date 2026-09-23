package web.controller;

import database.OrderDAO;
import jakarta.servlet.http.HttpSession;
import model.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Web version of OrderGUI. Same Order model, same OrderDAO.placeOrder(Order)
 * call, same validation rules as the Swing version.
 */
@Controller
public class OrderController {

    @GetMapping("/order")
    public String showOrderForm(HttpSession session, Model model) {
        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }
        if (!model.containsAttribute("payment")) {
            model.addAttribute("payment", "Paid");
        }
        return "order";
    }

    @PostMapping("/order")
    public String handlePlaceOrder(
            HttpSession session,
            Model model,
            @RequestParam String productName,
            @RequestParam String quantity,
            @RequestParam String total,
            @RequestParam String payment
    ) {
        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }

        // Re-populate the form with whatever the user typed, same as
        // OrderGUI clearing only error state, not field contents.
        model.addAttribute("productName", productName);
        model.addAttribute("quantity", quantity);
        model.addAttribute("total", total);
        model.addAttribute("payment", payment);

        boolean ok = true;

        String productNameTrimmed = productName == null ? "" : productName.trim();
        if (productNameTrimmed.isEmpty()) {
            model.addAttribute("productError", "Enter a product name.");
            ok = false;
        }

        int quantityValue = 0;
        String qtyTrimmed = quantity == null ? "" : quantity.trim();
        if (qtyTrimmed.isEmpty()) {
            model.addAttribute("quantityError", "Enter a quantity.");
            ok = false;
        } else {
            try {
                quantityValue = Integer.parseInt(qtyTrimmed);
                if (quantityValue <= 0) {
                    model.addAttribute("quantityError", "Quantity must be greater than 0.");
                    ok = false;
                }
            } catch (NumberFormatException ex) {
                model.addAttribute("quantityError", "Quantity must be a whole number.");
                ok = false;
            }
        }

        double totalValue = 0;
        String totalTrimmed = total == null ? "" : total.trim();
        if (totalTrimmed.isEmpty()) {
            model.addAttribute("totalError", "Enter the total price.");
            ok = false;
        } else {
            try {
                totalValue = Double.parseDouble(totalTrimmed);
                if (totalValue <= 0) {
                    model.addAttribute("totalError", "Total price must be greater than 0.");
                    ok = false;
                }
            } catch (NumberFormatException ex) {
                model.addAttribute("totalError", "Total price must be a number, e.g. 49.99.");
                ok = false;
            }
        }

        if (!ok) {
            model.addAttribute("banner", "Check the highlighted fields and try again.");
            return "order";
        }

        Order order = new Order(productNameTrimmed, quantityValue, totalValue, payment);
        OrderDAO dao = new OrderDAO();
        boolean result = dao.placeOrder(order);

        if (result) {
            model.addAttribute("bannerOk", "Order placed successfully.");
            // Clear the form on success, same as OrderGUI leaving fields as-is
            // but showing a success banner — adjust here if you'd rather clear.
        } else {
            model.addAttribute("banner", "Order failed. Please try again.");
        }

        return "order";
    }
}