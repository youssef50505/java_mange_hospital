package hospital.gui;

import hospital.models.*;
import hospital.services.HospitalService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PatientPanel {
    private final HospitalService svc = HospitalService.getInstance();
    private final JFrame frame;
    private final JPanel content;
    private final Patient patient;

    public PatientPanel(JFrame f, JPanel c, Patient p) { frame = f; content = c; patient = p; }

    public String[] getMenuLabels() {
        return new String[]{"\u2630 My Records", "\u2795 Book Appt", "\u2630 Prescriptions", "\u2630 Invoices"};
    }
    public Runnable[] getActions() {
        return new Runnable[]{this::showRecords, this::showBooking, this::showPrescriptions, this::showInvoices};
    }

    public void showRecords() {
        reset();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Theme.label("My Medical Records", Theme.FONT_TITLE, Theme.TEXT_WHITE));
        content.add(Box.createVerticalStrut(16));

        // Profile card
        JPanel info = Theme.card();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setMaximumSize(new Dimension(580, 150));
        info.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.add(Theme.label(patient.getName(), Theme.FONT_H2, Theme.CYAN));
        info.add(Box.createVerticalStrut(8));
        info.add(Theme.infoRow("Age", String.valueOf(patient.getAge()), Theme.TEXT_WHITE));
        info.add(Theme.infoRow("Gender", patient.getGender(), Theme.TEXT_WHITE));
        info.add(Theme.infoRow("Blood Group", patient.getBloodGroup(), Theme.ORANGE));
        info.add(Box.createVerticalStrut(6));
        info.add(Theme.label("Medical History", Theme.FONT_TINY, Theme.TEXT_DIM));
        info.add(Theme.label(patient.getMedicalHistory(), Theme.FONT_BODY, Theme.TEXT_LIGHT));
        content.add(info);
        content.add(Box.createVerticalStrut(14));

        // Stats
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(600, 100));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(Theme.statCard("Appointments", String.valueOf(svc.getAppointmentsForUser(patient).size()), Theme.BLUE));
        row.add(Theme.statCard("Prescriptions", String.valueOf(patient.getPrescriptions().size()), Theme.GREEN));
        row.add(Theme.statCard("Total Billed", "$" + String.format("%.0f", patient.getTotalBilled()), Theme.ORANGE));
        content.add(row);
        done();
    }

    public void showBooking() {
        reset();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Theme.label("Book an Appointment", Theme.FONT_TITLE, Theme.TEXT_WHITE));
        content.add(Box.createVerticalStrut(20));

        lbl("Select Doctor");
        List<Doctor> docs = svc.getAllDoctors();
        JComboBox<Doctor> docBox = Theme.makeComboBox(docs.toArray(new Doctor[0]));
        docBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(docBox); content.add(Box.createVerticalStrut(12));

        lbl("Select Time Slot");
        JComboBox<String> slotBox = Theme.makeComboBox(new String[]{});
        slotBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(slotBox); content.add(Box.createVerticalStrut(10));

        JLabel fee = Theme.label("", Theme.FONT_BODY, Theme.ORANGE);
        fee.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(fee); content.add(Box.createVerticalStrut(16));

        Runnable refresh = () -> {
            slotBox.removeAllItems();
            Doctor d = (Doctor) docBox.getSelectedItem();
            if (d != null) {
                for (String s : d.getAvailableSlots()) slotBox.addItem(s);
                fee.setText("Consultation Fee: $" + d.getConsultationFee());
            }
        };
        docBox.addActionListener(e -> refresh.run());
        refresh.run();

        JButton book = Theme.makeButton("Confirm Booking", Theme.BLUE);
        book.setAlignmentX(Component.LEFT_ALIGNMENT);
        book.addActionListener(e -> {
            Doctor d = (Doctor) docBox.getSelectedItem();
            String slot = (String) slotBox.getSelectedItem();
            if (d == null || slot == null) {
                JOptionPane.showMessageDialog(frame, "No slots available!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            svc.bookAppointment(patient, d, LocalDate.now().toString(), slot);
            JOptionPane.showMessageDialog(frame, "Booked with Dr. " + d.getName() + " at " + slot + "\nInvoice generated.");
            showBooking();
        });
        content.add(book);
        done();
    }

    public void showPrescriptions() {
        reset();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Theme.label("My Prescriptions", Theme.FONT_TITLE, Theme.TEXT_WHITE));
        content.add(Box.createVerticalStrut(16));

        List<Prescription> list = patient.getPrescriptions();
        if (list.isEmpty()) {
            JPanel empty = Theme.card();
            empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
            empty.setMaximumSize(new Dimension(500, 70));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            empty.add(Theme.label("No prescriptions on record", Theme.FONT_BODY, Theme.TEXT_LIGHT));
            empty.add(Theme.label("Visit a doctor to receive prescriptions.", Theme.FONT_SMALL, Theme.TEXT_DIM));
            content.add(empty);
        } else {
            for (Prescription rx : list) {
                JPanel card = Theme.card();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setMaximumSize(new Dimension(580, 100));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.setBorder(new EmptyBorder(14, 18, 14, 18));

                JPanel top = new JPanel(new BorderLayout());
                top.setOpaque(false);
                top.add(Theme.label("Dr. " + rx.getDoctor().getName(), Theme.FONT_BODY, Theme.CYAN), BorderLayout.WEST);
                top.add(Theme.label(rx.getDate(), Theme.FONT_SMALL, Theme.TEXT_DIM), BorderLayout.EAST);
                card.add(top);
                card.add(Box.createVerticalStrut(4));
                card.add(Theme.label("Diagnosis: " + rx.getDiagnosis(), Theme.FONT_SMALL, Theme.ORANGE));
                card.add(Theme.label("Medicines: " + String.join(", ", rx.getMedicines()), Theme.FONT_SMALL, Theme.TEXT_LIGHT));
                if (!rx.getNotes().isEmpty())
                    card.add(Theme.label("Notes: " + rx.getNotes(), Theme.FONT_TINY, Theme.TEXT_DIM));
                content.add(card);
                content.add(Box.createVerticalStrut(5));
            }
        }
        done();
    }

    public void showInvoices() {
        reset();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(600, 36));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(Theme.label("My Invoices", Theme.FONT_TITLE, Theme.TEXT_WHITE), BorderLayout.WEST);
        header.add(Theme.label("Total: $" + String.format("%.2f", patient.getTotalBilled()), Theme.FONT_H2, Theme.ORANGE), BorderLayout.EAST);
        content.add(header);
        content.add(Box.createVerticalStrut(16));

        List<Invoice> invs = patient.getInvoices();
        if (invs.isEmpty()) {
            JPanel empty = Theme.card();
            empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
            empty.setMaximumSize(new Dimension(500, 70));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            empty.add(Theme.label("No invoices yet", Theme.FONT_BODY, Theme.TEXT_LIGHT));
            empty.add(Theme.label("Invoices are generated when you book.", Theme.FONT_SMALL, Theme.TEXT_DIM));
            content.add(empty);
        } else {
            for (Invoice inv : invs) {
                JPanel card = Theme.card();
                card.setLayout(new BoxLayout(card, BoxLayout.X_AXIS));
                card.setMaximumSize(new Dimension(580, 50));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.setBorder(new EmptyBorder(12, 18, 12, 18));
                card.add(Theme.label("INV-" + inv.getId(), Theme.FONT_SMALL, Theme.TEXT_DIM));
                card.add(Box.createHorizontalStrut(14));
                card.add(Theme.label(inv.getDescription(), Theme.FONT_BODY, Theme.TEXT_WHITE));
                card.add(Box.createHorizontalGlue());
                card.add(Theme.label("$" + String.format("%.2f", inv.getAmount()), Theme.FONT_BODY, Theme.GREEN));
                card.add(Box.createHorizontalStrut(14));
                Color st = inv.isPaid() ? Theme.GREEN : Theme.RED;
                card.add(Theme.label(inv.isPaid() ? "PAID" : "UNPAID", Theme.FONT_TINY, st));
                content.add(card);
                content.add(Box.createVerticalStrut(5));
            }
        }
        done();
    }

    private void reset() { content.removeAll(); }
    private void done()  { content.revalidate(); content.repaint(); }
    private void lbl(String t) { JLabel l = Theme.label(t, Theme.FONT_SMALL, Theme.TEXT_DIM); l.setAlignmentX(Component.LEFT_ALIGNMENT); content.add(l); content.add(Box.createVerticalStrut(4)); }
}
