package hospital.gui;

import hospital.models.*;
import hospital.services.HospitalService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HospitalGUI extends JFrame {
    private final HospitalService svc = HospitalService.getInstance();
    private JPanel contentArea;

    public HospitalGUI() {
        setTitle("MedCore \u2014 Hospital Management System");
        setSize(1020, 660);
        setMinimumSize(new Dimension(880, 580));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showLogin();
    }

    private void showLogin() {
        JPanel bg = Theme.gradientPanel(new Color(8, 8, 18), new Color(18, 24, 52));
        bg.setLayout(new GridBagLayout());
        JPanel card = Theme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(370, 430));
        card.setBorder(new EmptyBorder(34, 42, 34, 42));

        JLabel icon = Theme.label("\u2695", new Font("Segoe UI Symbol", Font.PLAIN, 40), Theme.CYAN);
        icon.setAlignmentX(CENTER_ALIGNMENT);
        JLabel title = Theme.label("MedCore", Theme.FONT_TITLE, Theme.TEXT_WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);
        JLabel sub = Theme.label("Hospital Management System", Theme.FONT_TINY, Theme.TEXT_DIM);
        sub.setAlignmentX(CENTER_ALIGNMENT);
        card.add(icon); card.add(Box.createVerticalStrut(4));
        card.add(title); card.add(Box.createVerticalStrut(2));
        card.add(sub); card.add(Box.createVerticalStrut(28));

        addCLabel(card, "USER ID");
        JTextField idF = Theme.makeField("admin, doc1, pat1");
        idF.setAlignmentX(CENTER_ALIGNMENT);
        card.add(idF); card.add(Box.createVerticalStrut(12));
        addCLabel(card, "PASSWORD");
        JPasswordField passF = Theme.makePassField("Enter password");
        passF.setAlignmentX(CENTER_ALIGNMENT);
        card.add(passF); card.add(Box.createVerticalStrut(20));

        JButton loginBtn = Theme.makeButton("Sign In", Theme.BLUE);
        loginBtn.setAlignmentX(CENTER_ALIGNMENT);
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(16));
        JLabel hint = Theme.label("admin/admin123 \u00B7 doc1/pass \u00B7 pat1/pass", Theme.FONT_TINY, Theme.TEXT_DIM);
        hint.setAlignmentX(CENTER_ALIGNMENT);
        card.add(hint);

        loginBtn.addActionListener(e -> handleLogin(idF.getText(), new String(passF.getPassword())));
        passF.addActionListener(e -> loginBtn.doClick());
        bg.add(card);
        setContentPane(bg);
        revalidate(); repaint();
    }

    private void handleLogin(String id, String pass) {
        User user = svc.login(id.trim(), pass);
        if (user == null) { JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE); return; }
        contentArea = new JPanel();
        contentArea.setBackground(Theme.BG_DARK);
        contentArea.setBorder(new EmptyBorder(26, 28, 26, 28));

        if (user instanceof Admin) {
            AdminPanel p = new AdminPanel(this, contentArea);
            buildShell(user, p.getMenuLabels(), p.getActions());
            p.showDashboard();
        } else if (user instanceof Doctor) {
            DoctorPanel p = new DoctorPanel(this, contentArea, (Doctor) user);
            buildShell(user, p.getMenuLabels(), p.getActions());
            p.showAppointments();
        } else if (user instanceof Patient) {
            PatientPanel p = new PatientPanel(this, contentArea, (Patient) user);
            buildShell(user, p.getMenuLabels(), p.getActions());
            p.showRecords();
        }
    }

    private void buildShell(User user, String[] menu, Runnable[] acts) {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BG_DARK);
        JPanel sb = new JPanel();
        sb.setBackground(Theme.BG_SIDEBAR);
        sb.setPreferredSize(new Dimension(216, 0));
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBorder(new EmptyBorder(18, 10, 18, 10));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        brand.setOpaque(false);
        brand.setMaximumSize(new Dimension(200, 30));
        brand.add(Theme.label("\u2695", new Font("Segoe UI Symbol", Font.PLAIN, 20), Theme.CYAN));
        brand.add(Theme.label("MedCore", new Font("Segoe UI", Font.BOLD, 16), Theme.TEXT_WHITE));
        sb.add(brand);
        sb.add(Box.createVerticalStrut(6));
        sb.add(Theme.divider());
        sb.add(Box.createVerticalStrut(14));

        for (int i = 0; i < menu.length; i++) {
            String[] p = menu[i].split(" ", 2);
            JButton btn = Theme.sidebarBtn(p[0], p.length > 1 ? p[1] : "");
            if (i == 0) btn.setSelected(true);
            final int idx = i;
            btn.addActionListener(e -> acts[idx].run());
            sb.add(btn); sb.add(Box.createVerticalStrut(3));
        }
        sb.add(Box.createVerticalGlue());
        sb.add(Theme.divider());
        sb.add(Box.createVerticalStrut(12));

        JPanel badge = new JPanel();
        badge.setOpaque(false);
        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        badge.setBorder(new EmptyBorder(0, 6, 0, 0));
        badge.add(Theme.label(user.getName(), Theme.FONT_BODY, Theme.TEXT_WHITE));
        badge.add(Theme.label(user.getRole(), Theme.FONT_TINY, Theme.CYAN));
        badge.add(Box.createVerticalStrut(10));
        JButton logout = Theme.makeButton("\u2190  Logout", Theme.RED);
        logout.setMaximumSize(new Dimension(180, 34));
        logout.addActionListener(e -> showLogin());
        badge.add(logout);
        sb.add(badge);

        main.add(sb, BorderLayout.WEST);
        main.add(contentArea, BorderLayout.CENTER);
        setContentPane(main);
        revalidate(); repaint();
    }

    private void addCLabel(JPanel p, String t) {
        JLabel l = Theme.label(t, Theme.FONT_TINY, Theme.TEXT_DIM);
        l.setAlignmentX(CENTER_ALIGNMENT);
        p.add(l); p.add(Box.createVerticalStrut(4));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalGUI().setVisible(true));
    }
}
