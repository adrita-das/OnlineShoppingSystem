package gui;

import database.UserDAO;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.regex.Pattern;

public class Login extends JFrame {

    // ---------- palette ----------
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
    private static final Color ERROR_COLOR   = new Color(199, 40, 28);
    private static final Color OK           = new Color(23, 135, 106);

    private static final Font FONT_TITLE = new Font("Segue UI", Font.BOLD, 30);
    private static final Font FONT_KICK  = new Font("Segue UI", Font.PLAIN, 13);
    private static final Font FONT_LABEL = new Font("Segue UI", Font.BOLD, 12);
    private static final Font FONT_FIELD = new Font("Segue UI", Font.PLAIN, 15);
    private static final Font FONT_HINT  = new Font("Segue UI", Font.PLAIN, 11);
    private static final Font FONT_BTN   = new Font("Segue UI", Font.BOLD, 15);
    private static final Font FONT_ALT   = new Font("Segue UI", Font.PLAIN, 12);

    // Boundary used for BVA: password length must fall in [MIN, MAX].
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private RoundedTextField emailField;
    private RoundedField passwordField;
    private JLabel emailError;
    private JLabel passwordError;
    private JLabel banner;
    private JButton loginButton;
    private JToggleButton eyeToggle;

    public Login() {
        setTitle("Sign in — Kartly");
        setSize(880, 560);
        setMinimumSize(new Dimension(760, 520));
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
        getRootPane().setDefaultButton(loginButton);
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
        col.add(Box.createVerticalStrut(46));

        JLabel kicker = leftLabel("Welcome back", FONT_KICK, MUTED);
        col.add(kicker);
        col.add(Box.createVerticalStrut(4));

        JLabel title = leftLabel("Sign in", FONT_TITLE, INK);
        col.add(title);
        col.add(Box.createVerticalStrut(22));

        banner = leftLabel(" ", FONT_ALT, ERROR_COLOR);
        banner.setOpaque(true);
        banner.setBackground(BG);
        banner.setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12));
        banner.setVisible(false);
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        col.add(banner);
        col.add(Box.createVerticalStrut(6));

        // ---- email ----
        col.add(fieldLabelRow("Email", null));
        col.add(Box.createVerticalStrut(7));
        emailField = new RoundedTextField();
        emailField.setFont(FONT_FIELD);
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        emailField.setToolTipText("you@example.com");
        col.add(emailField);
        emailError = errorLabel();
        col.add(emailError);
        col.add(Box.createVerticalStrut(16));

        // ---- password ----
        col.add(fieldLabelRow("Password", "Forgot password?"));
        col.add(Box.createVerticalStrut(7));
        col.add(passwordRow());
        JLabel hint = leftLabel(
                MIN_PASSWORD_LENGTH + "–" + MAX_PASSWORD_LENGTH + " characters, with a letter and a number.",
                FONT_HINT, MUTED);
        hint.setBorder(BorderFactory.createEmptyBorder(7, 2, 0, 0));
        col.add(hint);
        passwordError = errorLabel();
        col.add(passwordError);
        col.add(Box.createVerticalStrut(26));

        // ---- submit ----
        loginButton = roundedButton("Sign in");
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        loginButton.addActionListener(e -> attemptLogin());
        col.add(loginButton);
        col.add(Box.createVerticalStrut(20));

        JLabel alt = leftLabel(
                "Don't have an account? Create one",
                FONT_ALT,
                INK_SOFT
        );

        alt.setCursor(new Cursor(Cursor.HAND_CURSOR));

        alt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Register();
                dispose();
            }
        });

        alt.setAlignmentX(Component.CENTER_ALIGNMENT);
        col.add(alt);

        GridBagConstraints gc = new GridBagConstraints();
        wrap.add(col, gc);
        return wrap;
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

    private JComponent passwordRow() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        passwordField = new RoundedField();
        passwordField.setEchoChar('•');
        passwordField.setFont(FONT_FIELD);

        eyeToggle = new JToggleButton("show");
        eyeToggle.setFont(new Font("Segue UI", Font.BOLD, 10));
        eyeToggle.setForeground(MUTED);
        eyeToggle.setContentAreaFilled(false);
        eyeToggle.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        eyeToggle.setFocusPainted(false);
        eyeToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeToggle.addActionListener(e -> {
            boolean shown = eyeToggle.isSelected();
            passwordField.setEchoChar(shown ? (char) 0 : '•');
            eyeToggle.setText(shown ? "hide" : "show");
        });

        JPanel overlay = new JPanel(new BorderLayout());
        overlay.setOpaque(false);
        overlay.add(passwordField, BorderLayout.CENTER);
        overlay.add(eyeToggle, BorderLayout.EAST);
        passwordField.setTrailingGap(48);

        wrap.add(overlay, BorderLayout.CENTER);
        return wrap;
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
    //  Right side — hand-painted illustration (mirrors the reference art)
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
                g2.fill(new Ellipse2D.Float(cx - 130, cy - 190, 260, 260));

                // cart
                g2.setColor(INK);
                g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int bx = cx - 150, by = cy + 10;
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

                // person
                int px = cx + 30, py = cy - 120;
                g2.setColor(new Color(247, 228, 216));
                g2.fill(new Ellipse2D.Float(px, py, 56, 56));
                g2.setColor(INK);
                g2.fillArc(px - 3, py - 6, 62, 48, 0, 180);
                g2.setColor(CORAL);
                g2.fill(new RoundRectangle2D.Float(px - 6, py + 52, 68, 78, 24, 24));
                g2.setColor(new Color(43, 39, 69));
                g2.fillRoundRect(px + 2, py + 118, 26, 84, 8, 8);
                g2.fillRoundRect(px + 34, py + 118, 26, 84, 8, 8);
                g2.setColor(CORAL_HOVER);
                g2.fillRoundRect(px + 68, py + 78, 16, 60, 8, 8);
                g2.setColor(new Color(233, 201, 168));
                g2.fillRoundRect(px + 76, py + 96, 58, 50, 6, 6);

                g2.dispose();
            }
        };
        art.setPreferredSize(new Dimension(360, 560));

        JPanel quoteWrap = new JPanel(new BorderLayout());
        quoteWrap.setOpaque(false);
        art.setLayout(new BorderLayout());

        JPanel quote = new JPanel();
        quote.setOpaque(false);
        quote.setLayout(new BoxLayout(quote, BoxLayout.Y_AXIS));
        quote.setBorder(BorderFactory.createEmptyBorder(0, 32, 30, 32));
        JLabel q1 = new JLabel("Everything in your cart,");
        JLabel q2 = new JLabel("waiting where you left it.");
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
    //  Validation — unchanged rules, so existing BVA test cases still apply
    // ================================================================
    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = String.valueOf(passwordField.getPassword());

        clearErrors();
        boolean ok = true;

        if (email.isEmpty()) {
            setFieldError(emailField, emailError, "Enter your email address.");
            ok = false;
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            setFieldError(emailField, emailError, "Enter a valid email, like name@example.com.");
            ok = false;
        }

        if (password.isEmpty()) {
            setFieldError(passwordField, passwordError, "Enter your password.");
            ok = false;
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            setFieldError(passwordField, passwordError,
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters. Currently " + password.length() + ".");
            ok = false;
        } else if (password.length() > MAX_PASSWORD_LENGTH) {
            setFieldError(passwordField, passwordError,
                    "Password cannot exceed " + MAX_PASSWORD_LENGTH + " characters. Currently " + password.length() + ".");
            ok = false;
        }

        if (!ok) {
            showBanner("Check the highlighted fields and try again.", ERROR_COLOR);
            return;
        }

        UserDAO dao = new UserDAO();
        boolean result = dao.loginUser(email, password);

        if (result) {
            showBanner("Signed in. Welcome back!", OK);

            new ProductGUI();
            dispose();

        } else {
            showBanner(
                    "That email and password don't match an account.",
                    ERROR_COLOR
            );
        }
    }

    private void setFieldError(JTextField field, JLabel label, String message) {
        if (field instanceof RoundedTextField) ((RoundedTextField) field).setErrorState(true);
        if (field instanceof RoundedField) ((RoundedField) field).setErrorState(true);
        label.setText(message);
    }

    private void clearErrors() {
        emailField.setErrorState(false);
        passwordField.setErrorState(false);
        emailError.setText(" ");
        passwordError.setText(" ");
    }

    private void showBanner(String message, Color color) {
        banner.setText(message);
        banner.setForeground(color);
        banner.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }

    // ================================================================
    //  Rounded, focus/error-aware chrome shared by the email and
    //  password fields. Swing has no built-in for this, so it's drawn
    //  by hand once here and reused by both field types below.
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

    /** Rounded plain-text field — used for email. */
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

    /** Rounded password field — used for password, with a togglable echo char. */
    private static class RoundedField extends JPasswordField {
        private boolean errorState = false;

        RoundedField() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(11, 14, 11, 14));
            setEchoChar('•');
            setCaretColor(INK);
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { repaint(); }
                public void focusLost(FocusEvent e) { repaint(); }
            });
        }

        void setErrorState(boolean error) { this.errorState = error; repaint(); }

        void setTrailingGap(int px) {
            setBorder(BorderFactory.createEmptyBorder(11, 14, 11, px));
        }

        protected void paintComponent(Graphics g) { paintFieldFill(g, this); super.paintComponent(g); }
        protected void paintBorder(Graphics g)    { paintFieldBorder(g, this, errorState); }
    }
}