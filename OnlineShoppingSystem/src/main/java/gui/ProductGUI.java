package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Product Catalog page — searchable, filterable listing with categories,
 * brands, price range, and stock counts.
 *
 * NOTE: uses a small in-memory demo product list so the page runs
 * standalone. Swap loadDemoProducts() for a real ProductDAO.getAll()
 * call once you're ready to wire it to the database.
 */
public class ProductGUI extends JFrame {

    // ---------- palette ----------
    private static final Color BG        = Color.WHITE;
    private static final Color SURFACE   = new Color(248, 248, 249);
    private static final Color INK       = new Color(30, 30, 35);
    private static final Color INK_SOFT  = new Color(95, 95, 105);
    private static final Color MUTED     = new Color(150, 150, 160);
    private static final Color LINE      = new Color(225, 225, 230);
    private static final Color ACCENT    = new Color(242, 96, 60);
    private static final Color ACCENT_HOVER = new Color(216, 74, 41);
    private static final Color STOCK_LOW = new Color(199, 40, 28);
    private static final Color STOCK_OK  = new Color(23, 135, 106);

    private static final Font FONT_BRAND = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_H2    = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_BODY  = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_HINT  = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_BTN   = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_PRICE = new Font("Segoe UI", Font.BOLD, 16);

    // ---- demo model — replace with model.Product / ProductDAO when wiring to the DB ----
    static class Product {
        String name, category, brand, image;
        double price;
        int stock;


        Product(String name, String category, String brand, double price, int stock, String image) {
            this.name = name;
            this.category = category;
            this.brand = brand;
            this.price = price;
            this.stock = stock;
            this.image = image;
        }
    }

    private final List<Product> allProducts = loadDemoProducts();

    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JComboBox<String> brandFilter;

    // Price range
    private JSpinner minPriceSpinner;
    private JSpinner maxPriceSpinner;

    private JPanel gridPanel;
    private JLabel resultCountLabel;

    public ProductGUI() {
        setTitle("Kartly — Product Catalog");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 640));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        setContentPane(root);

        root.add(buildTopBar(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(20, 24, 20, 24));
        body.add(buildFilterSidebar(), BorderLayout.WEST);
        body.add(buildResultsArea(), BorderLayout.CENTER);
        root.add(body, BorderLayout.CENTER);

        applyFilters();
        setVisible(true);
    }

    // ================================================================
    //  Top bar
    // ================================================================
    private JComponent buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, LINE),
                new EmptyBorder(14, 24, 14, 24)
        ));

        JLabel brand = new JLabel("Kartly");
        brand.setFont(FONT_BRAND);
        brand.setForeground(INK);
        bar.add(brand, BorderLayout.WEST);

        searchField = new JTextField();
        searchField.setFont(FONT_BODY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                applyFilters();
            }
        });

        JPanel searchWrap = new JPanel(new BorderLayout());
        searchWrap.setOpaque(false);
        searchWrap.setBorder(new EmptyBorder(0, 40, 0, 40));
        searchWrap.add(searchField, BorderLayout.CENTER);
        bar.add(searchWrap, BorderLayout.CENTER);

        JPanel icons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        icons.setOpaque(false);
        icons.add(iconButton("\u2661", "Wishlist"));
        icons.add(iconButton("\uD83D\uDED2", "Cart"));
        bar.add(icons, BorderLayout.EAST);

        return bar;
    }

    private JComponent iconButton(String glyph, String tooltip) {
        JButton b = new JButton(glyph);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        b.setForeground(INK);
        b.setToolTipText(tooltip);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ================================================================
    //  Left sidebar — filters
    // ================================================================
    private JComponent buildFilterSidebar() {
        JPanel outer = new JPanel();
        outer.setOpaque(false);
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.setPreferredSize(new Dimension(230, 0));
        outer.setBorder(new EmptyBorder(0, 0, 0, 24));

        JPanel side = new JPanel();
        side.setBackground(BG);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE, 1, true),
                new EmptyBorder(18, 16, 18, 16)
        ));
        side.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.setMaximumSize(new Dimension(230, Integer.MAX_VALUE));

        JLabel filtersTitle = new JLabel("Filters");
        filtersTitle.setFont(FONT_H2);
        filtersTitle.setForeground(INK);
        filtersTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(filtersTitle);
        side.add(Box.createVerticalStrut(16));

        side.add(filterHeading("Category"));

        categoryFilter = new JComboBox<>(new String[]{
                "All Categories",
                "Electronics",
                "Fashion",
                "Home & Kitchen",
                "Books",
                "Sports"
        });

        styleCombo(categoryFilter);
        side.add(categoryFilter);
        side.add(Box.createVerticalStrut(16));

        side.add(filterHeading("Brand"));

        brandFilter = new JComboBox<>(new String[]{
                "All Brands",
                "Samsung",
                "Apple",
                "Sony",
                "Nike",
                "IKEA",
                "Generic"
        });

        styleCombo(brandFilter);
        side.add(brandFilter);
        side.add(Box.createVerticalStrut(16));

        // ============================================================
        //  PRICE RANGE
        // ============================================================

        side.add(filterHeading("Price range"));
        side.add(Box.createVerticalStrut(6));

        JPanel priceRow = new JPanel(new GridLayout(1, 2, 8, 0));
        priceRow.setOpaque(false);
        priceRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        priceRow.setMaximumSize(new Dimension(200, 55));

        // Low: user can type or use up/down buttons
        minPriceSpinner = priceSpinner(250);

        // High: user can type or use up/down buttons
        maxPriceSpinner = priceSpinner(10000);

        priceRow.add(labeledSpinner("Low", minPriceSpinner));
        priceRow.add(labeledSpinner("High", maxPriceSpinner));

        side.add(priceRow);
        side.add(Box.createVerticalStrut(20));

        JButton apply = pillButton("Apply Filters", true);
        apply.setAlignmentX(Component.LEFT_ALIGNMENT);
        apply.setMaximumSize(new Dimension(200, 38));
        apply.addActionListener(e -> applyFilters());
        side.add(apply);

        JButton clear = pillButton("Clear", false);
        clear.setAlignmentX(Component.LEFT_ALIGNMENT);
        clear.setMaximumSize(new Dimension(200, 36));
        clear.addActionListener(e -> clearFilters());
        side.add(Box.createVerticalStrut(8));
        side.add(clear);

        categoryFilter.addActionListener(e -> applyFilters());
        brandFilter.addActionListener(e -> applyFilters());

        outer.add(side);
        return outer;
    }

    // ================================================================
    //  Price field with Low / High label
    // ================================================================
    private JComponent labeledSpinner(String caption, JSpinner spinner) {
        JPanel box = new JPanel(new BorderLayout());
        box.setOpaque(false);

        JLabel label = new JLabel(caption);
        label.setFont(FONT_HINT);
        label.setForeground(MUTED);

        box.add(label, BorderLayout.NORTH);
        box.add(spinner, BorderLayout.CENTER);

        return box;
    }

    // ================================================================
    //  Price spinner
    // ================================================================
    private JSpinner priceSpinner(int value) {

        SpinnerNumberModel model = new SpinnerNumberModel(
                value,     // starting value
                0,         // minimum price
                10000,     // maximum price
                50         // increase/decrease by 50
        );

        JSpinner spinner = new JSpinner(model);

        spinner.setFont(FONT_BODY);
        spinner.setPreferredSize(new Dimension(90, 32));

        // Allows user to type a value
        JSpinner.NumberEditor editor =
                new JSpinner.NumberEditor(spinner, "0");

        spinner.setEditor(editor);

        editor.getTextField().setFont(FONT_BODY);
        editor.getTextField().setForeground(INK);

        editor.getTextField().setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(LINE, 1, true),
                        new EmptyBorder(6, 8, 6, 8)
                )
        );

        return spinner;
    }

    private JLabel filterHeading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(INK_SOFT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(0, 0, 6, 0));
        return l;
    }

    private void styleCombo(JComboBox<String> box) {
        box.setFont(FONT_BODY);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.setMaximumSize(new Dimension(200, 34));
    }

    // ================================================================
    //  Clear filters
    // ================================================================
    private void clearFilters() {
        categoryFilter.setSelectedIndex(0);
        brandFilter.setSelectedIndex(0);

        minPriceSpinner.setValue(250);
        maxPriceSpinner.setValue(10000);

        searchField.setText("");

        applyFilters();
    }

    // ================================================================
    //  Center — result count + product grid
    // ================================================================
    private JComponent buildResultsArea() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);

        resultCountLabel = new JLabel();
        resultCountLabel.setFont(FONT_BODY);
        resultCountLabel.setForeground(MUTED);
        resultCountLabel.setBorder(new EmptyBorder(0, 0, 14, 0));
        wrap.add(resultCountLabel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(BG);

        JPanel gridHolder = new JPanel(new BorderLayout());
        gridHolder.setBackground(BG);
        gridHolder.add(gridPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(gridHolder);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        wrap.add(scroll, BorderLayout.CENTER);

        return wrap;
    }

    private JComponent buildProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE, 1, true),
                new EmptyBorder(14, 14, 14, 14)
        ));
        card.setPreferredSize(new Dimension(260, 300));

        JPanel imagePlaceholder = new JPanel(new GridBagLayout());
        imagePlaceholder.setBackground(SURFACE);
        imagePlaceholder.setPreferredSize(new Dimension(0, 120));

        JLabel imageIcon = new JLabel();

        java.net.URL imageURL =
                getClass().getResource("/images/" + p.image);

        if (imageURL != null) {

            ImageIcon originalIcon = new ImageIcon(imageURL);

            Image scaledImage = originalIcon.getImage().getScaledInstance(
                    180,
                    110,
                    Image.SCALE_SMOOTH
            );

            imageIcon.setIcon(new ImageIcon(scaledImage));

        } else {

            imageIcon.setText("No Image");
            imageIcon.setFont(FONT_HINT);
            imageIcon.setForeground(MUTED);
        }

        imagePlaceholder.add(imageIcon);
        card.add(imagePlaceholder, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(12, 0, 0, 0));

        JLabel brandLabel = new JLabel(p.brand.toUpperCase());
        brandLabel.setFont(FONT_HINT);
        brandLabel.setForeground(MUTED);
        brandLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.add(brandLabel);

        JLabel nameLabel = new JLabel(p.name);
        nameLabel.setFont(FONT_H2);
        nameLabel.setForeground(INK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(Box.createVerticalStrut(2));
        info.add(nameLabel);

        JPanel priceRow = new JPanel(new BorderLayout());
        priceRow.setOpaque(false);
        priceRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        priceRow.setBorder(new EmptyBorder(8, 0, 10, 0));

        JLabel priceLabel = new JLabel(String.format("$%.2f", p.price));
        priceLabel.setFont(FONT_PRICE);
        priceLabel.setForeground(INK);
        priceRow.add(priceLabel, BorderLayout.WEST);

        boolean outOfStock = p.stock == 0;
        boolean lowStock = p.stock > 0 && p.stock <= 5;

        String stockText = outOfStock
                ? "Out of stock"
                : (lowStock
                ? "Only " + p.stock + " left"
                : p.stock + " in stock");

        Color stockBg = outOfStock
                ? new Color(252, 226, 224)
                : (lowStock
                ? new Color(255, 240, 210)
                : new Color(224, 246, 239));

        Color stockFg = outOfStock
                ? STOCK_LOW
                : (lowStock
                ? new Color(160, 110, 10)
                : STOCK_OK);

        priceRow.add(
                pill(stockText, stockBg, stockFg),
                BorderLayout.EAST
        );

        info.add(priceRow);

        JButton addToCart = pillButton(
                outOfStock ? "Out of Stock" : "Add to Cart",
                !outOfStock
        );

        addToCart.setEnabled(!outOfStock);
        addToCart.setAlignmentX(Component.LEFT_ALIGNMENT);
        addToCart.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 36)
        );
        addToCart.setPreferredSize(
                new Dimension(Integer.MAX_VALUE, 36)
        );

        // TODO: wire to CartDAO.addToCart(...) once catalog is connected to the DB
        info.add(addToCart);

        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JLabel pill(String text, Color bg, Color fg) {
        JLabel l = new JLabel(text);
        l.setOpaque(true);
        l.setBackground(bg);
        l.setForeground(fg);
        l.setFont(FONT_HINT);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setBorder(new EmptyBorder(4, 10, 4, 10));
        return l;
    }

    private JButton pillButton(String text, boolean primary) {
        JButton b = new JButton(text) {
            private boolean hover = false;

            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hover = true;
                        repaint();
                    }

                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                if (primary) {
                    g2.setColor(
                            isEnabled()
                                    ? (hover ? ACCENT_HOVER : ACCENT)
                                    : MUTED
                    );

                    g2.fill(
                            new RoundRectangle2D.Float(
                                    0,
                                    0,
                                    getWidth(),
                                    getHeight(),
                                    10,
                                    10
                            )
                    );
                } else {
                    g2.setColor(Color.WHITE);

                    g2.fill(
                            new RoundRectangle2D.Float(
                                    0.5f,
                                    0.5f,
                                    getWidth() - 1,
                                    getHeight() - 1,
                                    10,
                                    10
                            )
                    );

                    g2.setColor(LINE);
                    g2.setStroke(new BasicStroke(1.2f));

                    g2.draw(
                            new RoundRectangle2D.Float(
                                    0.5f,
                                    0.5f,
                                    getWidth() - 1.5f,
                                    getHeight() - 1.5f,
                                    10,
                                    10
                            )
                    );
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        b.setFont(FONT_BTN);
        b.setForeground(primary ? Color.WHITE : INK);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setBorder(new EmptyBorder(8, 0, 8, 0));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return b;
    }

    // ================================================================
    //  Filtering
    // ================================================================
    private void applyFilters() {

        String query = searchField.getText().trim().toLowerCase();

        String category =
                (String) categoryFilter.getSelectedItem();

        String brand =
                (String) brandFilter.getSelectedItem();

        // Get selected Low price
        double minPrice =
                ((Number) minPriceSpinner.getValue()).doubleValue();

        // Get selected High price
        double maxPrice =
                ((Number) maxPriceSpinner.getValue()).doubleValue();

        gridPanel.removeAll();

        int count = 0;

        for (Product p : allProducts) {

            boolean matchesQuery =
                    query.isEmpty()
                            || p.name.toLowerCase().contains(query);

            boolean matchesCategory =
                    "All Categories".equals(category)
                            || p.category.equals(category);

            boolean matchesBrand =
                    "All Brands".equals(brand)
                            || p.brand.equals(brand);

            boolean matchesPrice =
                    p.price >= minPrice
                            && p.price <= maxPrice;

            if (matchesQuery
                    && matchesCategory
                    && matchesBrand
                    && matchesPrice) {

                gridPanel.add(buildProductCard(p));
                count++;
            }
        }

        resultCountLabel.setText(
                count
                        + " product"
                        + (count == 1 ? "" : "s")
                        + " found"
        );

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    // ================================================================
    //  Demo products
    // ================================================================
    private static List<Product> loadDemoProducts() {
        List<Product> list = new ArrayList<>();

        list.add(new Product(
                "Wireless Headphones",
                "Electronics",
                "Sony",
                289.99,
                12,
                "headphone.jpg"
        ));

        list.add(new Product(
                "Smartphone X12",
                "Electronics",
                "Samsung",
                599.00,
                3,
                "samsung.jpg"

        ));

        list.add(new Product(
                "Running Shoes",
                "Sports",
                "Nike",
                274.50,
                0,
                "runningshoes.jpg"

        ));

        list.add(new Product(
                "Office Chair",
                "Home & Kitchen",
                "IKEA",
                329.00,
                8,
                "officechair.jpg"
        ));

        list.add(new Product(
                "Bluetooth Speaker",
                "Electronics",
                "Sony",
                345.00,
                20,
                "bluetoothspeaker.jpg"
        ));

        list.add(new Product(
                "Yoga Mat",
                "Sports",
                "Generic",
                319.99,
                40,
                "yogamat.jpg"
        ));

        list.add(new Product(
                "Laptop Stand",
                "Home & Kitchen",
                "Generic",
                425.00,
                2,
                "laptopstand.jpg"

        ));

        list.add(new Product(
                "Non-Fiction: IKIGAI",
                "Books",
                "Generic",
                314.99,
                15,
                "ikigai.jpg"
        ));

        list.add(new Product(
                "Smartwatch Pro",
                "Electronics",
                "Apple",
                649.00,
                6,
                "smartwatchpro.jpg"
        ));

        return list;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProductGUI::new);
    }
}