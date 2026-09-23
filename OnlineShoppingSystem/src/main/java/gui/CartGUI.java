package gui;

import database.CartDAO;
import model.Cart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Add-to-cart screen, styled the same way as Login.java (same palette,
 * fonts, rounded fields, art panel — all self-contained in this file).
 * Same Cart model and CartDAO.addToCart(Cart) as your teammate's original.
 *
 * Quantity is capped at 1-100, which doubles as a ready-made boundary-value
 * module (0 / 1 / 2 and 99 / 100 / 101) alongside the login password one.
 */
public class CartGUI extends JFrame {

    // ---------- palette (same as Login.java) ----------
    private static final Color BG           = Color.WHITE;
    private static final Color PANEL_PEACH  = new Color(252, 233, 222);
    private static final Color PANEL_PEACH2 = new Color(248, 217, 201);
    private static final Color INK          = new Color(23, 20, 41);
    private static final Color INK_SOFT     = new Color(91, 88, 112);
    private static final Color MUTED        = new Color(142, 138, 163);
    private static final Color LINE         = new Color(227, 221, 232);
    private static final Color CORAL        = new Color(242, 96, 60);
    private static final Color CORAL_HOVER  = new Color(216, 74, 41);
    private static final Color CORAL_GHOST  = new Color(253, 236, 230);
    private static final Color TEAL         = new Color(63, 214, 176);
    private static final Color BLUE         = new Color(47, 107, 255);
    private static final Color ERROR_COLOR  = new Color(199, 40, 28);
    private static final Color OK           = new Color(23, 135, 106);

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FONT_KICK  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_FIELD = new Font("Segoe UI", Font.PLAIN, 15);
    private static final Font FONT_HINT  = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_BTN   = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_ALT   = new Font("Segoe UI", Font.PLAIN, 12);

    private static final int MIN_QTY = 1;
    private static final int MAX_QTY = 100;

    private RoundedTextField idField;
    private RoundedTextField nameField;
    private RoundedTextField priceField;
    private RoundedTextField quantityField;
    private JLabel idError, nameError, priceError, quantityError, banner;
    private JButton addButton;

    public CartGUI() {
        setTitle("Add to cart — Kartly");
        setSize(880, 600);
        setMinimumSize(new Dimension(760, 540));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG);
        GridBagConstraints rc = new GridBagConstraints();
        rc.fill = GridBagConstraints.BOTH;
        rc.weighty = 1;

        rc.gridx = 0; rc.weightx = 1.05;
        root.add(buildFormPanel(), rc);

        rc.gridx = 1; rc.weightx = 1.0;
        root.add(buildArtPanel(), rc);

        setContentPane(root);
        getRootPane().setDefaultButton(addButton);
        setVisible(true);
    }

    // ================================================================
    //  Left side — the form
    // ================================================================
    private JPanel buildFormPanel() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(BG);
        wrap.setBorder(BorderFactory.createEmptyBorder(0, 56, 0, 40));

        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));

        col.add(brandMark());
        col.add(Box.createVerticalStrut(34));

        col.add(leftLabel("Cart", FONT_KICK, MUTED));
        col.add(Box.createVerticalStrut(4));
        col.add(leftLabel("Add to cart", FONT_TITLE, INK));
        col.add(Box.createVerticalStrut(18));

        banner = leftLabel(" ", FONT_ALT, ERROR_COLOR);
        banner.setOpaque(true);
        banner.setBackground(BG);
        banner.setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12));
        banner.setVisible(false);
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        col.add(banner);
        col.add(Box.createVerticalStrut(4));

        idField = addField(col, "Product ID");
        idError = errorLabel();
        col.add(idError);
        col.add(Box.createVerticalStrut(14));

        nameField = addField(col, "Product name");
        nameError = errorLabel();
        col.add(nameError);
        col.add(Box.createVerticalStrut(14));

        priceField = addField(col, "Price");
        priceError = errorLabel();
        col.add(priceError);
        col.add(Box.createVerticalStrut(14));

        quantityField = addField(col, "Quantity");
        JLabel hint = leftLabel("Between " + MIN_QTY + " and " + MAX_QTY + " units.", FONT_HINT, MUTED);
        hint.setBorder(BorderFactory.createEmptyBorder(7, 2, 0, 0));
        col.add(hint);
        quantityError = errorLabel();
        col.add(quantityError);
        col.add(Box.createVerticalStrut(24));

        addButton = roundedButton("Add to cart");
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        addButton.addActionListener(e -> attemptAddToCart());
        col.add(addButton);

        GridBagConstraints gc = new GridBagConstraints();
        wrap.add(col, gc);
        return wrap;
    }

    private RoundedTextField addField(JPanel col, String label) {
        col.add(fieldLabelRow(label, null));
        col.add(Box.createVerticalStrut(7));
        RoundedTextField field = new RoundedTextField();
        field.setFont(FONT_FIELD);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        col.add(field);
        return field;
    }

    private JComponent brandMark() {
        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(200, 30));

        JComponent mark = new JComponent() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = start(g);
                g2.setColor(CORAL);
                g2.fill(new RoundRectangle2D.Float(0, 6, 24, 18, 8, 8));
                g2.setColor(INK);
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(new java.awt.geom.Arc2D.Float(6, -2, 12, 16, 0, 180, java.awt.geom.Arc2D.OPEN));
                g2.dispose();
            }
        };
        mark.setPreferredSize(new Dimension(26, 26));
        mark.setMaximumSize(new Dimension(26, 26));

        JLabel word = new JLabel("Kartly");
        word.setFont(new Font("Segoe UI", Font.BOLD, 20));
        word.setForeground(INK);
        word.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        row.add(mark);
        row.add(word);
        return row;
    }

    private JComponent fieldLabelRow(String label, String rightLink) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        JLabel l = new JLabel(label);
        l.setFont(FONT_LABEL);
        l.setForeground(INK_SOFT);
        row.add(l, BorderLayout.WEST);

        if (rightLink != null) {
            JLabel r = new JLabel(rightLink);
            r.setFont(FONT_LABEL);
            r.setForeground(CORAL);
            r.setCursor(new Cursor(Cursor.HAND_CURSOR));
            row.add(r, BorderLayout.EAST);
        }
        return row;
    }

    private JLabel leftLabel(String text, Font f, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(f);
        l.setForeground(c);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel errorLabel() {
        JLabel l = new JLabel(" ");
        l.setFont(FONT_HINT);
        l.setForeground(ERROR_COLOR);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(BorderFactory.createEmptyBorder(5, 2, 0, 0));
        return l;
    }

    private JButton roundedButton(String text) {
        JButton b = new JButton(text) {
            private boolean hover = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
                });
            }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = start(g);
                g2.setColor(hover ? CORAL_HOVER : CORAL);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(FONT_BTN);
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ================================================================
    //  Right side — illustration
    // ================================================================
    private JComponent buildArtPanel() {
        JComponent art = new JComponent() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = start(g);
                int w = getWidth(), h = getHeight();

                g2.setPaint(new GradientPaint(0, 0, PANEL_PEACH, w, h, PANEL_PEACH2));
                g2.fillRect(0, 0, w, h);

                int cx = w / 2, cy = h / 2;
                g2.setColor(PANEL_PEACH2);
                g2.fill(new Ellipse2D.Float(cx - 130, cy - 130, 260, 260));

                g2.setColor(INK);
                g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int bx = cx - 90, by = cy - 20;
                g2.drawLine(bx, by, bx + 24, by);
                g2.drawLine(bx + 24, by, bx + 44, by + 74);
                g2.drawLine(bx + 44, by + 74, bx + 150, by + 74);
                g2.drawLine(bx + 150, by + 74, bx + 172, by + 18);
                g2.drawLine(bx + 44, by + 18, bx + 172, by + 18);
                g2.setColor(TEAL);
                g2.fillRoundRect(bx + 56, by + 30, 34, 30, 4, 4);
                g2.setColor(BLUE);
                g2.fillRoundRect(bx + 96, by + 30, 32, 30, 4, 4);
                g2.setColor(INK);
                g2.fillOval(bx + 58, by + 96, 15, 15);
                g2.fillOval(bx + 132, by + 96, 15, 15);

                g2.dispose();
            }
        };
        art.setPreferredSize(new Dimension(360, 560));
        art.setLayout(new BorderLayout());

        JPanel quote = new JPanel();
        quote.setOpaque(false);
        quote.setLayout(new BoxLayout(quote, BoxLayout.Y_AXIS));
        quote.setBorder(BorderFactory.createEmptyBorder(0, 32, 30, 32));
        JLabel q1 = new JLabel("Add it now,");
        JLabel q2 = new JLabel("checkout when ready.");
        Font qf = new Font("Segoe UI", Font.BOLD, 16);
        q1.setFont(qf); q2.setFont(qf);
        q1.setForeground(INK); q2.setForeground(INK);
        quote.add(q1); quote.add(q2);
        art.add(quote, BorderLayout.SOUTH);

        return art;
    }

    private static Graphics2D start(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return g2;
    }

    // ================================================================
    //  Validation — separate branches for empty / non-numeric / out of
    //  range so each maps to its own equivalence-class test case.
    // ================================================================
    private void attemptAddToCart() {
        clearErrors();
        boolean ok = true;

        int productId = 0;
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            setFieldError(idField, idError, "Enter a product ID.");
            ok = false;
        } else {
            try {
                productId = Integer.parseInt(idText);
                if (productId <= 0) {
                    setFieldError(idField, idError, "Product ID must be a positive number.");
                    ok = false;
                }
            } catch (NumberFormatException ex) {
                setFieldError(idField, idError, "Product ID must be a whole number.");
                ok = false;
            }
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            setFieldError(nameField, nameError, "Enter a product name.");
            ok = false;
        }

        double price = 0;
        String priceText = priceField.getText().trim();
        if (priceText.isEmpty()) {
            setFieldError(priceField, priceError, "Enter a price.");
            ok = false;
        } else {
            try {
                price = Double.parseDouble(priceText);
                if (price <= 0) {
                    setFieldError(priceField, priceError, "Price must be greater than 0.");
                    ok = false;
                }
            } catch (NumberFormatException ex) {
                setFieldError(priceField, priceError, "Price must be a number, e.g. 19.99.");
                ok = false;
            }
        }

        int quantity = 0;
        String qtyText = quantityField.getText().trim();
        if (qtyText.isEmpty()) {
            setFieldError(quantityField, quantityError, "Enter a quantity.");
            ok = false;
        } else {
            try {
                quantity = Integer.parseInt(qtyText);
                if (quantity < MIN_QTY || quantity > MAX_QTY) {
                    setFieldError(quantityField, quantityError,
                            "Quantity must be between " + MIN_QTY + " and " + MAX_QTY + ".");
                    ok = false;
                }
            } catch (NumberFormatException ex) {
                setFieldError(quantityField, quantityError, "Quantity must be a whole number.");
                ok = false;
            }
        }

        if (!ok) {
            showBanner("Check the highlighted fields and try again.", ERROR_COLOR);
            return;
        }

        Cart cart = new Cart(productId, name, price, quantity);
        CartDAO dao = new CartDAO();
        boolean result = dao.addToCart(cart);

        if (result) {
            showBanner("Added to cart.", OK);
        } else {
            showBanner("Could not add item to cart.", ERROR_COLOR);
        }
    }

    private void setFieldError(RoundedTextField field, JLabel label, String message) {
        field.setErrorState(true);
        label.setText(message);
    }

    private void clearErrors() {
        idField.setErrorState(false);
        nameField.setErrorState(false);
        priceField.setErrorState(false);
        quantityField.setErrorState(false);
        idError.setText(" ");
        nameError.setText(" ");
        priceError.setText(" ");
        quantityError.setText(" ");
    }

    private void showBanner(String message, Color color) {
        banner.setText(message);
        banner.setForeground(color);
        banner.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CartGUI::new);
    }

    // ================================================================
    //  Rounded, focus/error-aware chrome — identical pattern to Login.java
    // ================================================================
    private static void paintFieldFill(Graphics g, JComponent c) {
        Graphics2D g2 = start(g);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(1, 1, c.getWidth() - 2, c.getHeight() - 2, 12, 12));
        g2.dispose();
    }

    private static void paintFieldBorder(Graphics g, JComponent c, boolean errorState) {
        Graphics2D g2 = start(g);
        Color line = errorState ? ERROR_COLOR : (c.hasFocus() ? CORAL : LINE);
        g2.setColor(line);
        g2.setStroke(new BasicStroke(c.hasFocus() || errorState ? 1.6f : 1.2f));
        g2.draw(new RoundRectangle2D.Float(1, 1, c.getWidth() - 3, c.getHeight() - 3, 12, 12));
        if (c.hasFocus() && !errorState) {
            g2.setColor(CORAL_GHOST);
            g2.setStroke(new BasicStroke(4f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, c.getWidth() - 2, c.getHeight() - 2, 13, 13));
        }
        g2.dispose();
    }

    private static class RoundedTextField extends JTextField {
        private boolean errorState = false;

        RoundedTextField() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(11, 14, 11, 14));
            setCaretColor(INK);
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { repaint(); }
                public void focusLost(FocusEvent e) { repaint(); }
            });
        }

        void setErrorState(boolean error) { this.errorState = error; repaint(); }

        protected void paintComponent(Graphics g) { paintFieldFill(g, this); super.paintComponent(g); }
        protected void paintBorder(Graphics g)    { paintFieldBorder(g, this, errorState); }
    }
}