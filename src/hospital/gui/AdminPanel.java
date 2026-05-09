package hospital.gui;

import hospital.models.*;
import hospital.services.HospitalService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminPanel {
    private final HospitalService svc = HospitalService.getInstance();
    private final JFrame frame;
    private final JPanel content;

    public AdminPanel(JFrame frame, JPanel content) { this.frame = frame; this.content = content; }

    public String[] getMenuLabels() {
        return new String[]{"\u2630 Dashboard", "\u2795 Add Doctor", "\u2795 Add Patient", "\u2699 All Records"};
    }
    public Runnable[] getActions() {
        return new Runnable[]{this::showDashboard, this::showAddDoctor, this::showAddPatient, this::showAllRecords};
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
        stats.setMaximumSize(new Dimension(700, 100));
        stats.add(Theme.statCard("Doctors", String.valueOf(svc.getDoctorCount()), Theme.BLUE));
        stats.add(Theme.statCard("Patients", String.valueOf(svc.getPatientCount()), Theme.GREEN));
        stats.add(Theme.statCard("Appointments", String.valueOf(svc.getAppointmentCount()), Theme.ORANGE));
        stats.add(Theme.statCard("System", "Online", Theme.CYAN));
        content.add(stats);
        content.add(Box.createVerticalStrut(24));

        // Recent doctors list
        content.add(Theme.label("Registered Doctors", Theme.FONT_H2, Theme.TEXT_WHITE));
        content.add(Box.createVerticalStrut(10));
        for (Doctor d : svc.getAllDoctors()) {
            JPanel row = Theme.card();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.setMaximumSize(new Dimension(620, 50));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setBorder(new EmptyBorder(12, 16, 12, 16));
            row.add(Theme.label("Dr. " + d.getName(), Theme.FONT_BODY, Theme.TEXT_WHITE));
            row.add(Box.createHorizontalGlue());
            row.add(Theme.label(d.getSpecialization(), Theme.FONT_SMALL, Theme.CYAN));
            row.add(Box.createHorizontalStrut(20));
            row.add(Theme.label("$" + d.getConsultationFee(), Theme.FONT_SMALL, Theme.GREEN));
            content.add(row);
            content.add(Box.createVerticalStrut(4));
        }
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

    public void showAllRecords() {
        reset();
        content.setLayout(new BorderLayout(0, 14));
        content.add(Theme.label("All System Records", Theme.FONT_TITLE, Theme.TEXT_WHITE), BorderLayout.NORTH);
        JTextArea area = Theme.makeTextArea();
        area.append("  \u2550\u2550\u2550 DOCTORS \u2550\u2550\u2550\n");
        for (Doctor d : svc.getAllDoctors())
            area.append("   \u2022 " + d + "  |  $" + d.getConsultationFee() + "  |  Slots: " + d.getAvailableSlots().size() + "\n");
        area.append("\n  \u2550\u2550\u2550 PATIENTS \u2550\u2550\u2550\n");
        for (Patient p : svc.getAllPatients()) area.append("   \u2022 " + p + "\n");
        area.append("\n  \u2550\u2550\u2550 APPOINTMENTS \u2550\u2550\u2550\n");
        if (svc.getAllAppointments().isEmpty()) area.append("   No appointments yet.\n");
        for (Appointment a : svc.getAllAppointments()) area.append("   \u2022 " + a + "\n");
        content.add(Theme.darkScroll(area), BorderLayout.CENTER);
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
