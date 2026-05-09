package hospital.gui;

import hospital.models.*;
import hospital.services.HospitalService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AdminPanel {
    private final HospitalService svc = HospitalService.getInstance();
    private final JFrame frame;
    private final JPanel content;

    public AdminPanel(JFrame frame, JPanel content) { this.frame = frame; this.content = content; }

    public String[] getMenuLabels() {
        return new String[]{"\u2630 Dashboard", "\u2795 Add Doctor", "\u2795 Add Patient", "\u2699 Manage Users"};
    }
    public Runnable[] getActions() {
        return new Runnable[]{this::showDashboard, this::showAddDoctor, this::showAddPatient, this::showManageUsers};
    }

    public void showDashboard() {
        reset();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Theme.label("Dashboard", Theme.FONT_TITLE, Theme.TEXT_WHITE));
        content.add(Box.createVerticalStrut(6));
        content.add(Theme.label("System overview at a glance", Theme.FONT_SMALL, Theme.TEXT_DIM));
        content.add(Box.createVerticalStrut(20));

        JPanel stats = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        stats.setOpaque(false);
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        stats.add(Theme.statCard("Doctors", String.valueOf(svc.getDoctorCount()), Theme.BLUE));
        stats.add(Theme.statCard("Patients", String.valueOf(svc.getPatientCount()), Theme.GREEN));
        stats.add(Theme.statCard("Appointments", String.valueOf(svc.getAppointmentCount()), Theme.ORANGE));
        stats.add(Theme.statCard("System", "Online", Theme.CYAN));
        content.add(stats);
        content.add(Box.createVerticalStrut(24));

        content.add(Theme.label("Registered Doctors", Theme.FONT_H2, Theme.TEXT_WHITE));
        content.add(Box.createVerticalStrut(10));
        
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (Doctor d : svc.getAllDoctors()) {
            JPanel row = Theme.card();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setBorder(new EmptyBorder(12, 16, 12, 16));
            row.add(Theme.label("Dr. " + d.getName(), Theme.FONT_BODY, Theme.TEXT_WHITE));
            row.add(Box.createHorizontalGlue());
            row.add(Theme.label(d.getSpecialization(), Theme.FONT_SMALL, Theme.CYAN));
            row.add(Box.createHorizontalStrut(20));
            row.add(Theme.label("$" + d.getConsultationFee(), Theme.FONT_SMALL, Theme.GREEN));
            listPanel.add(row);
            listPanel.add(Box.createVerticalStrut(4));
        }
        content.add(Theme.darkScroll(listPanel));
        done();
    }

    public void showAddDoctor() {
        reset();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Theme.label("Register New Doctor", Theme.FONT_TITLE, Theme.TEXT_WHITE));
        content.add(Box.createVerticalStrut(20));

        JTextField idF   = field("Doctor ID");
        JTextField nameF = field("Full Name");
        JTextField specF = field("Specialization");
        JTextField feeF  = field("Consultation Fee ($)");

        content.add(Box.createVerticalStrut(8));
        JButton save = Theme.makeButton("Register Doctor", Theme.GREEN);
        save.setAlignmentX(Component.LEFT_ALIGNMENT);
        save.addActionListener(e -> {
            try {
                notEmpty(idF, nameF, specF, feeF);
                Doctor d = new Doctor(idF.getText().trim(), nameF.getText().trim(), "pass",
                        specF.getText().trim(), Double.parseDouble(feeF.getText().trim()));
                d.addSlot("09:00 AM"); d.addSlot("10:00 AM"); d.addSlot("11:00 AM");
                d.addSlot("02:00 PM"); d.addSlot("03:00 PM"); d.addSlot("04:00 PM");
                if (svc.addDoctor(d)) { ok("Dr. " + nameF.getText().trim() + " registered!\nPassword: pass"); clear(idF, nameF, specF, feeF); }
                else err("ID already exists!");
            } catch (NumberFormatException ex) { err("Fee must be a number."); }
              catch (Exception ex) { err(ex.getMessage()); }
        });
        content.add(save);
        done();
    }

    public void showAddPatient() {
        reset();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Theme.label("Register New Patient", Theme.FONT_TITLE, Theme.TEXT_WHITE));
        content.add(Box.createVerticalStrut(20));

        JTextField idF   = field("Patient ID");
        JTextField nameF = field("Full Name");
        JTextField ageF  = field("Age");

        lbl("Gender");
        JComboBox<String> gBox = Theme.makeComboBox(new String[]{"Male", "Female"});
        gBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(gBox); content.add(Box.createVerticalStrut(12));

        lbl("Blood Group");
        JComboBox<String> bBox = Theme.makeComboBox(new String[]{"A+","A-","B+","B-","AB+","AB-","O+","O-"});
        bBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(bBox); content.add(Box.createVerticalStrut(16));

        JButton save = Theme.makeButton("Register Patient", Theme.GREEN);
        save.setAlignmentX(Component.LEFT_ALIGNMENT);
        save.addActionListener(e -> {
            try {
                notEmpty(idF, nameF, ageF);
                Patient p = new Patient(idF.getText().trim(), nameF.getText().trim(), "pass",
                        Integer.parseInt(ageF.getText().trim()), (String) gBox.getSelectedItem(), (String) bBox.getSelectedItem());
                if (svc.addPatient(p)) { ok(nameF.getText().trim() + " registered!\nPassword: pass"); clear(idF, nameF, ageF); }
                else err("ID already exists!");
            } catch (NumberFormatException ex) { err("Age must be a number."); }
              catch (Exception ex) { err(ex.getMessage()); }
        });
        content.add(save);
        done();
    }

    public void showManageUsers() {
        reset();
        content.setLayout(new BorderLayout(0, 14));
        
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(Theme.label("Manage Users", Theme.FONT_TITLE, Theme.TEXT_WHITE), BorderLayout.WEST);
        
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchBar.setOpaque(false);
        JTextField searchF = Theme.makeField("Search ID or Name...");
        searchF.setPreferredSize(new Dimension(200, 38));
        JButton searchBtn = Theme.makeButton("Search", Theme.BLUE);
        searchBtn.setPreferredSize(new Dimension(100, 38));
        searchBar.add(searchF); searchBar.add(searchBtn);
        top.add(searchBar, BorderLayout.EAST);
        
        content.add(top, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        Runnable loadUsers = () -> {
            listPanel.removeAll();
            String query = searchF.getText().trim();
            List<User> found = svc.searchUsers(query);
            
            if (found.isEmpty()) {
                listPanel.add(Theme.label("No users found matching query.", Theme.FONT_BODY, Theme.TEXT_DIM));
            } else {
                for (User u : found) {
                    if (u instanceof Admin) continue; // Prevent deleting admins
                    
                    JPanel card = Theme.card();
                    card.setLayout(new BoxLayout(card, BoxLayout.X_AXIS));
                    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                    card.setAlignmentX(Component.LEFT_ALIGNMENT);
                    card.setBorder(new EmptyBorder(10, 16, 10, 16));
                    
                    card.add(Theme.label(u.getId(), Theme.FONT_SMALL, Theme.TEXT_DIM));
                    card.add(Box.createHorizontalStrut(14));
                    card.add(Theme.label(u.getName(), Theme.FONT_BODY, Theme.TEXT_WHITE));
                    card.add(Box.createHorizontalStrut(14));
                    card.add(Theme.label(u.getRole().toUpperCase(), Theme.FONT_TINY, Theme.CYAN));
                    card.add(Box.createHorizontalGlue());
                    
                    JButton delBtn = Theme.makeButton("Delete", Theme.RED);
                    delBtn.setPreferredSize(new Dimension(90, 30));
                    delBtn.setFont(Theme.FONT_TINY);
                    delBtn.addActionListener(e -> {
                        int opt = JOptionPane.showConfirmDialog(frame, "Delete user " + u.getName() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                        if (opt == JOptionPane.YES_OPTION) {
                            svc.removeUser(u.getId());
                            searchBtn.doClick(); // reload
                        }
                    });
                    card.add(delBtn);
                    listPanel.add(card);
                    listPanel.add(Box.createVerticalStrut(5));
                }
            }
            listPanel.revalidate(); listPanel.repaint();
        };

        searchBtn.addActionListener(e -> loadUsers.run());
        loadUsers.run(); // initial load

        content.add(Theme.darkScroll(listPanel), BorderLayout.CENTER);
        done();
    }

    // helpers
    private void reset() { content.removeAll(); }
    private void done()  { content.revalidate(); content.repaint(); }
    private JTextField field(String l) { lbl(l); JTextField f = Theme.makeField("Enter " + l.toLowerCase()); f.setAlignmentX(Component.LEFT_ALIGNMENT); content.add(f); content.add(Box.createVerticalStrut(12)); return f; }
    private void lbl(String t) { JLabel l = Theme.label(t, Theme.FONT_SMALL, Theme.TEXT_DIM); l.setAlignmentX(Component.LEFT_ALIGNMENT); content.add(l); content.add(Box.createVerticalStrut(4)); }
    private void notEmpty(JTextField... ff) { for (JTextField f : ff) if (f.getText().trim().isEmpty()) throw new IllegalArgumentException("All fields required."); }
    private void clear(JTextField... ff) { for (JTextField f : ff) f.setText(""); }
    private void ok(String m)  { JOptionPane.showMessageDialog(frame, m, "Success", JOptionPane.INFORMATION_MESSAGE); }
    private void err(String m) { JOptionPane.showMessageDialog(frame, m, "Error", JOptionPane.ERROR_MESSAGE); }
}
