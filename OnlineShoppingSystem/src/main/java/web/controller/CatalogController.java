package web.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Web version of ProductGUI's catalog page. Same demo data, same
 * filtering rules (search / category / brand / price range) as the
 * Swing version, so behavior stays identical across both UIs.
 *
 * NOTE: swap loadDemoProducts() for a real ProductDAO.getAll() call
 * once you're ready to wire it to the database.
 */
@Controller
public class CatalogController {

    // ---- demo model — replace with model.Product / ProductDAO when wiring to the DB ----
    public static class Product {
        public String name, category, brand, image;
        public double price;
        public int stock;

        public Product(String name, String category, String brand, double price, int stock, String image) {
            this.name = name;
            this.category = category;
            this.brand = brand;
            this.price = price;
            this.stock = stock;
            this.image = image;
        }

        public String getName() { return name; }
        public String getCategory() { return category; }
        public String getBrand() { return brand; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }
        public String getImage() { return image; }

        public boolean isOutOfStock() { return stock == 0; }
        public boolean isLowStock() { return stock > 0 && stock <= 5; }
        public String getStockText() {
            if (isOutOfStock()) return "Out of stock";
            if (isLowStock()) return "Only " + stock + " left";
            return stock + " in stock";
        }
        public String getStockClass() {
            if (isOutOfStock()) return "stock-out";
            if (isLowStock()) return "stock-low";
            return "stock-ok";
        }
    }

    @GetMapping("/catalog")
    public String showCatalog(
            HttpSession session,
            Model model,
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "All Categories") String category,
            @RequestParam(required = false, defaultValue = "All Brands") String brand,
            @RequestParam(required = false, defaultValue = "250") double minPrice,
            @RequestParam(required = false, defaultValue = "10000") double maxPrice
    ) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return "redirect:/login";
        }

        List<Product> all = loadDemoProducts();
        List<Product> filtered = new ArrayList<>();

        String q = query == null ? "" : query.trim().toLowerCase();

        for (Product p : all) {
            boolean matchesQuery = q.isEmpty() || p.name.toLowerCase().contains(q);
            boolean matchesCategory = "All Categories".equals(category) || p.category.equals(category);
            boolean matchesBrand = "All Brands".equals(brand) || p.brand.equals(brand);
            boolean matchesPrice = p.price >= minPrice && p.price <= maxPrice;

            if (matchesQuery && matchesCategory && matchesBrand && matchesPrice) {
                filtered.add(p);
            }
        }

        model.addAttribute("userEmail", userEmail);
        model.addAttribute("products", filtered);
        model.addAttribute("resultCount", filtered.size());
        model.addAttribute("query", query);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("categories", List.of("All Categories", "Electronics", "Fashion", "Home & Kitchen", "Books", "Sports"));
        model.addAttribute("brands", List.of("All Brands", "Samsung", "Apple", "Sony", "Nike", "IKEA", "Generic"));

        return "catalog";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private static List<Product> loadDemoProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("Wireless Headphones", "Electronics", "Sony", 289.99, 12, "headphone.jpg"));
        list.add(new Product("Smartphone X12", "Electronics", "Samsung", 599.00, 3, "samsung.jpg"));
        list.add(new Product("Running Shoes", "Sports", "Nike", 274.50, 0, "runningshoes.jpg"));
        list.add(new Product("Office Chair", "Home & Kitchen", "IKEA", 329.00, 8, "officechair.jpg"));
        list.add(new Product("Bluetooth Speaker", "Electronics", "Sony", 345.00, 20, "bluetoothspeaker.jpg"));
        list.add(new Product("Yoga Mat", "Sports", "Generic", 319.99, 40, "yogamat.jpg"));
        list.add(new Product("Laptop Stand", "Home & Kitchen", "Generic", 425.00, 2, "laptopstand.jpg"));
        list.add(new Product("Non-Fiction: IKIGAI", "Books", "Generic", 314.99, 15, "ikigai.jpg"));
        list.add(new Product("Smartwatch Pro", "Electronics", "Apple", 649.00, 6, "smartwatchpro.jpg"));
        return list;
    }
}