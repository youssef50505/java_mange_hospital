package hospital.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Patient — books appointments, views prescriptions and invoices.
 *
 * OOP Concepts:
 *   - Inheritance: extends User.
 *   - Aggregation: has-a relationship with Prescription and Invoice lists.
 *   - Encapsulation: lists returned as unmodifiable views.
 */
public class Patient extends User {

    private int    age;
    private String gender;
    private String bloodGroup;
    private String medicalHistory;
    private final List<Prescription> prescriptions;
    private final List<Invoice>      invoices;

    public Patient(String id, String name, String password,
                   int age, String gender, String bloodGroup) {
        super(id, name, id + "@mail.com", "N/A", password);
        this.age            = age;
        this.gender         = gender;
        this.bloodGroup     = bloodGroup;
        this.medicalHistory = "No history recorded.";
        this.prescriptions  = new ArrayList<>();
        this.invoices       = new ArrayList<>();
    }

    // ── Getters ──────────────────────────────────────────
    public int    getAge()            { return age; }
    public String getGender()         { return gender; }
    public String getBloodGroup()     { return bloodGroup; }
    public String getMedicalHistory() { return medicalHistory; }

    public List<Prescription> getPrescriptions() {
        return Collections.unmodifiableList(prescriptions);
    }

    public List<Invoice> getInvoices() {
        return Collections.unmodifiableList(invoices);
    }

    // ── Setters / Mutators ───────────────────────────────
    public void setMedicalHistory(String history) {
        this.medicalHistory = history;
    }

    public void addPrescription(Prescription p) {
        if (p != null) prescriptions.add(p);
    }

    public void addInvoice(Invoice i) {
        if (i != null) invoices.add(i);
    }

    // ── Computed Properties ──────────────────────────────
    public double getTotalBilled() {
        return invoices.stream().mapToDouble(Invoice::getAmount).sum();
    }

    public double getTotalPaid() {
        return invoices.stream().filter(Invoice::isPaid).mapToDouble(Invoice::getAmount).sum();
    }

    // ── Overrides ────────────────────────────────────────
    @Override
    public String getRole() {
        return "Patient";
    }

    @Override
    public String toString() {
        return name + " (Age: " + age + ", " + gender + ", Blood: " + bloodGroup + ")";
    }
}
