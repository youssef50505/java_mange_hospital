package hospital.gui;

import hospital.models.*;
import hospital.services.HospitalService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class HospitalGUI extends JFrame {
    private HospitalService service;

    public HospitalGUI() {
        service = HospitalService.getInstance();
        setTitle("Hospital Management System - OOP Course");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showLoginPanel();
    }

    private void showLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255)); // Alice Blue
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Hospital System Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; panel.add(new JLabel("User ID (e.g. admin, doc1, pat1):"), gbc);
        gbc.gridx = 1; JTextField idField = new JTextField(15); panel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; JPasswordField passField = new JPasswordField(15); panel.add(passField, gbc);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> {
            String id = idField.getText();
            String pass = new String(passField.getPassword());
            User user = service.login(id, pass);

            if (user != null) {
                if (user instanceof Admin) showAdminPanel((Admin) user);
                else if (user instanceof Doctor) showDoctorPanel((Doctor) user);
                else if (user instanceof Patient) showPatientPanel((Patient) user);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials! Check hints.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setContentPane(panel);
        revalidate();
    }

    private void showAdminPanel(Admin admin) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcome = new JLabel("Welcome Admin: " + admin.getName(), SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 18));
        welcome.setBorder(new EmptyBorder(10, 0, 10, 0));
        panel.add(welcome, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        btnPanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        JButton addDocBtn = new JButton("Register New Doctor");
        JButton addPatBtn = new JButton("Register New Patient");
        JButton logoutBtn = new JButton("Logout");

        addDocBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Doctor ID:");
            String name = JOptionPane.showInputDialog("Doctor Name:");
            String spec = JOptionPane.showInputDialog("Specialization:");
            if (id != null && name != null && spec != null) {
                Doctor d = new Doctor(id, name, "pass", spec);
                d.addSlot("09:00 AM"); d.addSlot("10:00 AM"); d.addSlot("02:00 PM");
                service.addDoctor(d);
                JOptionPane.showMessageDialog(this, "Doctor " + name + " added successfully!\nPassword defaults to 'pass'");
            }
        });

        addPatBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Patient ID:");
            String name = JOptionPane.showInputDialog("Patient Name:");
            if (id != null && name != null) {
                service.addPatient(new Patient(id, name, "pass"));
                JOptionPane.showMessageDialog(this, "Patient " + name + " added successfully!\nPassword defaults to 'pass'");
            }
        });

        logoutBtn.addActionListener(e -> showLoginPanel());

        btnPanel.add(addDocBtn);
        btnPanel.add(addPatBtn);
        btnPanel.add(logoutBtn);
        panel.add(btnPanel, BorderLayout.CENTER);

        setContentPane(panel);
        revalidate();
    }

    private void showDoctorPanel(Doctor doctor) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel header = new JLabel("Welcome Doctor: " + doctor.getName() + " (" + doctor.getSpecialization() + ")", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(header, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.append("=== Your Appointments ===\n\n");
        List<Appointment> appts = service.getAppointmentsForUser(doctor);
        if (appts.isEmpty()) area.append("No appointments scheduled.\n");
        for (Appointment a : appts) {
            area.append(a.toString() + "\n");
        }
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        JButton prescribeBtn = new JButton("Prescribe Medicine");
        JButton logoutBtn = new JButton("Logout");

        prescribeBtn.addActionListener(e -> {
            String pId = JOptionPane.showInputDialog("Enter Patient ID for Prescription:");
            if (pId != null) {
                Patient p = service.getAllPatients().stream().filter(pat -> pat.getId().equals(pId)).findFirst().orElse(null);
                if (p != null) {
                    String meds = JOptionPane.showInputDialog("Enter Medicines (comma separated):");
                    if (meds != null && !meds.trim().isEmpty()) {
                        p.addPrescription(new Prescription(doctor, "Today", Arrays.asList(meds.split(","))));
                        JOptionPane.showMessageDialog(this, "Prescription successfully added for " + p.getName());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Patient ID not found.");
                }
            }
        });

        logoutBtn.addActionListener(e -> showLoginPanel());

        bottom.add(prescribeBtn);
        bottom.add(logoutBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        setContentPane(panel);
        revalidate();
    }

    private void showPatientPanel(Patient patient) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel header = new JLabel("Welcome Patient: " + patient.getName(), SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(header, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.append("=== Medical History ===\n" + patient.getMedicalHistory() + "\n\n");
        
        area.append("=== Your Appointments ===\n");
        List<Appointment> appts = service.getAppointmentsForUser(patient);
        if (appts.isEmpty()) area.append("No appointments yet.\n");
        for (Appointment a : appts) {
            area.append(a.toString() + "\n");
        }
        
        area.append("\n=== Your Prescriptions ===\n");
        List<Prescription> prescriptions = patient.getPrescriptions();
        if (prescriptions.isEmpty()) area.append("No prescriptions on record.\n");
        for (Prescription p : prescriptions) {
            area.append(p.toString() + "\n");
        }
        
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        JButton bookBtn = new JButton("Book Appointment");
        JButton updateHistoryBtn = new JButton("Update History");
        JButton logoutBtn = new JButton("Logout");

        updateHistoryBtn.addActionListener(e -> {
            String hist = JOptionPane.showInputDialog(this, "Enter new Medical History:");
            if (hist != null && !hist.trim().isEmpty()) {
                patient.setMedicalHistory(hist);
                showPatientPanel(patient); // refresh
            }
        });

        bookBtn.addActionListener(e -> {
            List<Doctor> docs = service.getAllDoctors();
            if(docs.isEmpty()) { JOptionPane.showMessageDialog(this, "No doctors available."); return; }
            Doctor selectedDoc = (Doctor) JOptionPane.showInputDialog(this, "Select a Doctor", "Book Appointment", JOptionPane.QUESTION_MESSAGE, null, docs.toArray(), docs.get(0));
            if (selectedDoc != null) {
                List<String> slots = selectedDoc.getAvailableSlots();
                if(slots.isEmpty()) { JOptionPane.showMessageDialog(this, "No available slots for this doctor."); return; }
                String slot = (String) JOptionPane.showInputDialog(this, "Select a Time Slot", "Book Appointment", JOptionPane.QUESTION_MESSAGE, null, slots.toArray(), slots.get(0));
                if (slot != null) {
                    service.bookAppointment(patient, selectedDoc, slot);
                    JOptionPane.showMessageDialog(this, "Appointment successfully booked!");
                    showPatientPanel(patient); // refresh
                }
            }
        });

        logoutBtn.addActionListener(e -> showLoginPanel());
        
        bottom.add(bookBtn);
        bottom.add(updateHistoryBtn);
        bottom.add(logoutBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        setContentPane(panel);
        revalidate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HospitalGUI().setVisible(true);
        });
    }
}
