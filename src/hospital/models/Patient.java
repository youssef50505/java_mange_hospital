package hospital.models;
import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
    private String medicalHistory;
    private List<Prescription> prescriptions;
    private List<Invoice> invoices;

    public Patient(String id, String name, String password) {
        super(id, name, password);
        this.medicalHistory = "No history available.";
        this.prescriptions = new ArrayList<>();
        this.invoices = new ArrayList<>();
    }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String history) { this.medicalHistory = history; }
    
    public void addPrescription(Prescription p) { this.prescriptions.add(p); }
    public List<Prescription> getPrescriptions() { return prescriptions; }

    public void addInvoice(Invoice i) { this.invoices.add(i); }
    public List<Invoice> getInvoices() { return invoices; }

    @Override
    public String getRole() {
        return "Patient";
    }
}
