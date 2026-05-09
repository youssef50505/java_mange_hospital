package hospital.gui;

import hospital.models.*;
import hospital.services.HospitalService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class DoctorPanel {
    private final HospitalService svc = HospitalService.getInstance();
    private final JFrame frame;
    private final JPanel content;
    private final Doctor doctor;

    public DoctorPanel(JFrame f, JPanel c, Doctor d) { frame = f; content = c; doctor = d; }

    public String[] getMenuLabels() {
        return new String[]{"\u2630 Appointments", "\u270E Prescribe", "\u270E Update Records", "\u2139 My Profile"};
    }
    public Runnable[] getActions() {
        return new Runnable[]{this::showAppointments, this::showPrescribe, this::showUpdateRecords, this::showProfile};
    }

    public void showAppointments() {
        reset();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Theme.label("My Appointments", Theme.FONT_TITLE, Theme.TEXT_WHITE));
        content.add(Box.createVerticalStrut(6));
        content.add(Theme.label("Manage and complete scheduled appointments", Theme.FONT_SMALL, Theme.TEXT_DIM));
        content.add(Box.createVerticalStrut(16));

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        List<Appointment> list = svc.getAppointmentsForUser(doctor);
        if (list.isEmpty()) {
            JPanel empty = Theme.card();
            empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
            empty.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            empty.add(Theme.label("No appointments yet", Theme.FONT_BODY, Theme.TEXT_LIGHT));
            empty.add(Box.createVerticalStrut(4));
            empty.add(Theme.label("Patients will book via their portal.", Theme.FONT_SMALL, Theme.TEXT_DIM));
            listPanel.add(empty);
        } else {
            for (Appointment a : list) {
                JPanel row = Theme.card();
                row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);
                row.setBorder(new EmptyBorder(14, 18, 14, 18));

                JPanel top = new JPanel(new BorderLayout());
                top.setOpaque(false);
                top.add(Theme.label(a.getPatient().getName(), Theme.FONT_BODY, Theme.TEXT_WHITE), BorderLayout.WEST);
                
                Color stColor = a.getStatus() == Appointment.Status.SCHEDULED ? Theme.ORANGE : Theme.GREEN;
                if (a.getStatus() == Appointment.Status.CANCELLED) stColor = Theme.RED;
                top.add(Theme.label(a.getStatus().toString(), Theme.FONT_TINY, stColor), BorderLayout.EAST);
                row.add(top);
                row.add(Box.createVerticalStrut(4));
                row.add(Theme.label(a.getDate() + " @ " + a.getTimeSlot() + "  |  Blood: " + a.getPatient().getBloodGroup(),
                        Theme.FONT_SMALL, Theme.TEXT_DIM));
                
                if (a.getStatus() == Appointment.Status.SCHEDULED) {
                    JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
                    btns.setOpaque(false);
                    JButton compBtn = Theme.makeButton("Complete", Theme.GREEN);
                    compBtn.setPreferredSize(new Dimension(100, 30));
                    compBtn.setFont(Theme.FONT_TINY);
                    compBtn.addActionListener(e -> { a.complete(); showAppointments(); });
                    
                    JButton cancBtn = Theme.makeButton("Cancel", Theme.RED);
                    cancBtn.setPreferredSize(new Dimension(100, 30));
                    cancBtn.setFont(Theme.FONT_TINY);
                    cancBtn.addActionListener(e -> { a.cancel(); showAppointments(); });
                    
                    btns.add(compBtn); btns.add(cancBtn);
                    row.add(Box.createVerticalStrut(8));
                    row.add(btns);
                }
                listPanel.add(row);
                listPanel.add(Box.createVerticalStrut(5));
            }
        }
        content.add(Theme.darkScroll(listPanel));
        done();
    }

    public void showPrescribe() {
        reset();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Theme.label("Prescribe Medicine", Theme.FONT_TITLE, Theme.TEXT_WHITE));
        content.add(Box.createVerticalStrut(20));

        lbl("Select Patient");
        List<Patient> pats = svc.getAllPatients();
        JComboBox<Patient> patBox = Theme.makeComboBox(pats.toArray(new Patient[0]));
        patBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(patBox); content.add(Box.createVerticalStrut(12));

        JTextField diagF = field("Diagnosis");
        JTextField medsF = field("Medicines (comma separated)");
        JTextField noteF = field("Notes (optional)");

        JButton btn = Theme.makeButton("Submit Prescription", Theme.GREEN);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> {
            Patient p = (Patient) patBox.getSelectedItem();
            if (p == null || medsF.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Select patient & enter medicines.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<String> meds = Arrays.asList(medsF.getText().trim().split("\\s*,\\s*"));
            p.addPrescription(new Prescription(doctor, LocalDate.now().toString(),
                    diagF.getText().trim(), meds, noteF.getText().trim()));
            JOptionPane.showMessageDialog(frame, "Prescription added for " + p.getName());
            diagF.setText(""); medsF.setText(""); noteF.setText("");
        });
        content.add(Box.createVerticalStrut(6));
        content.add(btn);
        done();
    }

    public void showUpdateRecords() {
        reset();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Theme.label("Update Medical Records", Theme.FONT_TITLE, Theme.TEXT_WHITE));
        content.add(Box.createVerticalStrut(20));

        lbl("Select Patient");
        List<Patient> pats = svc.getAllPatients();
        JComboBox<Patient> patBox = Theme.makeComboBox(pats.toArray(new Patient[0]));
        patBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(patBox); content.add(Box.createVerticalStrut(12));

        lbl("Medical History");
        JTextArea histArea = new JTextArea();
        histArea.setFont(Theme.FONT_BODY);
        histArea.setBackground(Theme.BG_INPUT);
        histArea.setForeground(Theme.TEXT_WHITE);
        histArea.setCaretColor(Theme.BLUE);
        histArea.setLineWrap(true);
        histArea.setWrapStyleWord(true);
        histArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scroll = Theme.darkScroll(histArea);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        content.add(scroll);
        content.add(Box.createVerticalStrut(20));

        patBox.addActionListener(e -> {
            Patient p = (Patient) patBox.getSelectedItem();
            if (p != null) histArea.setText(p.getMedicalHistory());
        });
        if (!pats.isEmpty()) histArea.setText(pats.get(0).getMedicalHistory());

        JButton saveBtn = Theme.makeButton("Save Records", Theme.GREEN);
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> {
            Patient p = (Patient) patBox.getSelectedItem();
            if (p != null) {
                p.setMedicalHistory(histArea.getText());
                JOptionPane.showMessageDialog(frame, "Medical records updated for " + p.getName());
            }
        });
        content.add(saveBtn);
        done();
    }

    public void showProfile() {
        reset();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Theme.label("My Profile", Theme.FONT_TITLE, Theme.TEXT_WHITE));
        content.add(Box.createVerticalStrut(20));

        JPanel card = Theme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(Theme.label("Dr. " + doctor.getName(), Theme.FONT_H2, Theme.CYAN));
        card.add(Box.createVerticalStrut(12));
        card.add(Theme.infoRow("Specialization", doctor.getSpecialization(), Theme.TEXT_WHITE));
        card.add(Theme.infoRow("Consultation Fee", "$" + doctor.getConsultationFee(), Theme.GREEN));
        card.add(Theme.infoRow("Available Slots", String.valueOf(doctor.getAvailableSlots().size()), Theme.ORANGE));
        card.add(Theme.infoRow("Total Appointments", String.valueOf(svc.getAppointmentsForUser(doctor).size()), Theme.BLUE));
        card.add(Theme.infoRow("User ID", doctor.getId(), Theme.TEXT_DIM));
        content.add(card);
        done();
    }

    private void reset() { content.removeAll(); }
    private void done()  { content.revalidate(); content.repaint(); }
    private JTextField field(String l) { lbl(l); JTextField f = Theme.makeField("Enter " + l.toLowerCase()); f.setAlignmentX(Component.LEFT_ALIGNMENT); content.add(f); content.add(Box.createVerticalStrut(12)); return f; }
    private void lbl(String t) { JLabel l = Theme.label(t, Theme.FONT_SMALL, Theme.TEXT_DIM); l.setAlignmentX(Component.LEFT_ALIGNMENT); content.add(l); content.add(Box.createVerticalStrut(4)); }
}
