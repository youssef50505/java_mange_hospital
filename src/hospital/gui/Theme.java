package hospital.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Modern UI Theme — all colors, fonts, and component factories.
 * Premium dark theme with gradients, shadows, and smooth hover effects.
 */
public class Theme {

    // ─── Color Palette (Modern Medical) ──────────────────
    public static final Color BG_DARK       = new Color(10, 10, 20);
    public static final Color BG_SIDEBAR    = new Color(16, 18, 32);
    public static final Color BG_CARD       = new Color(22, 27, 48);
    public static final Color BG_CARD_ALT   = new Color(28, 34, 58);
    public static final Color BG_HOVER      = new Color(35, 42, 72);
    public static final Color BG_INPUT      = new Color(14, 16, 30);
    public static final Color BORDER        = new Color(40, 48, 80);
    public static final Color BORDER_FOCUS  = new Color(99, 140, 255);

    // Accent colors
    public static final Color BLUE          = new Color(88, 130, 255);
    public static final Color BLUE_DIM      = new Color(40, 60, 140);
    public static final Color CYAN          = new Color(56, 200, 220);
    public static final Color GREEN         = new Color(60, 210, 140);
    public static final Color GREEN_DIM     = new Color(30, 90, 60);
    public static final Color RED           = new Color(240, 85, 95);
    public static final Color ORANGE        = new Color(250, 165, 50);
    public static final Color PURPLE        = new Color(160, 120, 240);
    public static final Color PINK          = new Color(230, 100, 170);

    // Text
    public static final Color TEXT_WHITE    = new Color(230, 235, 245);
    public static final Color TEXT_LIGHT    = new Color(180, 190, 210);
    public static final Color TEXT_DIM      = new Color(120, 130, 155);

    // ─── Fonts ───────────────────────────────────────────
    public static final Font FONT_TITLE     = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_H2        = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_BODY      = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL     = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TINY      = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BTN       = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_MONO      = new Font("Consolas", Font.PLAIN, 13);

    // ─── Gradient Background Panel ───────────────────────
    public static JPanel gradientPanel(Color c1, Color c2) {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    // ─── Rounded Button with hover glow ──────────────────
    public static JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            float alpha = 0f;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { alpha = 0.15f; repaint(); }
                public void mouseExited(MouseEvent e)  { alpha = 0f; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Button fill
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                // Hover glow overlay
                if (alpha > 0) {
                    g2.setColor(new Color(255, 255, 255, (int)(alpha * 255)));
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                }
                // Text
                g2.setFont(FONT_BTN);
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(200, 38));
        btn.setMaximumSize(new Dimension(320, 38));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ─── Text Field with focus border ────────────────────
    public static JTextField makeField(String placeholder) {
        JTextField f = new JTextField() {
            { addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { setBorder(makeBorder(BORDER_FOCUS)); repaint(); }
                public void focusLost(FocusEvent e)   { setBorder(makeBorder(BORDER)); repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(TEXT_DIM);
                    g2.setFont(FONT_BODY);
                    g2.drawString(placeholder, getInsets().left + 2, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        styleInput(f);
        return f;
    }

    public static JPasswordField makePassField(String placeholder) {
        JPasswordField f = new JPasswordField() {
            { addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { setBorder(makeBorder(BORDER_FOCUS)); repaint(); }
                public void focusLost(FocusEvent e)   { setBorder(makeBorder(BORDER)); repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(TEXT_DIM);
                    g2.setFont(FONT_BODY);
                    g2.drawString(placeholder, getInsets().left + 2, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        styleInput(f);
        return f;
    }

    private static void styleInput(JTextField f) {
        f.setFont(FONT_BODY);
        f.setForeground(TEXT_WHITE);
        f.setBackground(BG_INPUT);
        f.setCaretColor(BLUE);
        f.setBorder(makeBorder(BORDER));
        f.setPreferredSize(new Dimension(300, 40));
        f.setMaximumSize(new Dimension(320, 40));
    }

    private static Border makeBorder(Color c) {
        return BorderFactory.createCompoundBorder(
            new LineBorder(c, 1, true), new EmptyBorder(8, 12, 8, 12));
    }

    // ─── Label ───────────────────────────────────────────
    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    // ─── Card Panel with subtle shadow ───────────────────
    public static JPanel card() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Shadow
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fill(new RoundRectangle2D.Float(3, 3, getWidth() - 3, getHeight() - 3, 14, 14));
                // Card body
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 3, getHeight() - 3, 14, 14));
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(20, 22, 20, 22));
        return p;
    }

    // ─── Stat Card with colored top line ─────────────────
    public static JPanel statCard(String title, String value, Color accent) {
        JPanel c = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Shadow
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fill(new RoundRectangle2D.Float(2, 2, getWidth() - 2, getHeight() - 2, 12, 12));
                // Card
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 2, getHeight() - 2, 12, 12));
                // Top accent line
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth() - 2, 3, 3, 3);
                g2.dispose();
            }
        };
        c.setOpaque(false);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(18, 20, 16, 20));
        c.setPreferredSize(new Dimension(155, 90));
        c.add(label(title, FONT_TINY, TEXT_DIM));
        c.add(Box.createVerticalStrut(6));
        c.add(label(value, new Font("Segoe UI", Font.BOLD, 22), accent));
        return c;
    }

    // ─── Sidebar Button with active indicator ────────────
    public static JButton sidebarBtn(String icon, String text) {
        JButton btn = new JButton() {
            boolean hover = false;
            boolean active = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
            }); }
            public void setSelected(boolean b) { active = b; repaint(); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (active) {
                    g2.setColor(BLUE_DIM);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                    // Left indicator bar
                    g2.setColor(BLUE);
                    g2.fillRoundRect(0, 4, 3, getHeight() - 8, 3, 3);
                } else if (hover) {
                    g2.setColor(BG_HOVER);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                }
                g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
                g2.setColor(active ? BLUE : TEXT_DIM);
                g2.drawString(icon, 14, 25);
                g2.setFont(FONT_BODY);
                g2.setColor(active ? TEXT_WHITE : TEXT_LIGHT);
                g2.drawString(text, 40, 25);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(196, 38));
        btn.setMaximumSize(new Dimension(196, 38));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ─── TextArea (dark styled) ──────────────────────────
    public static JTextArea makeTextArea() {
        JTextArea a = new JTextArea();
        a.setFont(FONT_MONO);
        a.setForeground(TEXT_WHITE);
        a.setBackground(BG_INPUT);
        a.setCaretColor(BLUE);
        a.setEditable(false);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(new EmptyBorder(14, 16, 14, 16));
        return a;
    }

    // ─── ComboBox (dark styled) ──────────────────────────
    @SuppressWarnings("unchecked")
    public static <T> JComboBox<T> makeComboBox(T[] items) {
        JComboBox<T> box = new JComboBox<>(items);
        box.setFont(FONT_BODY);
        box.setBackground(BG_INPUT);
        box.setForeground(TEXT_WHITE);
        box.setMaximumSize(new Dimension(320, 38));
        box.setBorder(new LineBorder(BORDER, 1, true));
        box.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> list, Object val, int i, boolean sel, boolean foc) {
                super.getListCellRendererComponent(list, val, i, sel, foc);
                setBackground(sel ? BLUE_DIM : BG_CARD);
                setForeground(TEXT_WHITE);
                setFont(FONT_BODY);
                setBorder(new EmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
        return box;
    }

    // ─── Scroll Pane (dark styled, custom scrollbar) ─────
    public static JScrollPane darkScroll(Component view) {
        JScrollPane sp = new JScrollPane(view);
        sp.setBorder(null);
        sp.getViewport().setBackground(BG_INPUT);
        sp.getVerticalScrollBar().setBackground(BG_SIDEBAR);
        sp.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = BORDER; trackColor = BG_SIDEBAR;
            }
            @Override protected JButton createDecreaseButton(int o) { return zeroBtn(); }
            @Override protected JButton createIncreaseButton(int o) { return zeroBtn(); }
            private JButton zeroBtn() {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0, 0)); return b;
            }
        });
        return sp;
    }

    // ─── Info Row (key: value styled) ────────────────────
    public static JPanel infoRow(String key, String value, Color valueColor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(500, 24));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(label(key + ":  ", FONT_SMALL, TEXT_DIM));
        row.add(label(value, FONT_BODY, valueColor));
        return row;
    }

    // ─── Section Divider ─────────────────────────────────
    public static JSeparator divider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setBackground(BG_DARK);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }
}
