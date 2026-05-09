package hospital.models;

import java.util.Collections;
import java.util.List;

/**
 * Represents a prescription issued by a Doctor for a Patient.
 *
 * OOP Concepts:
 *   - Encapsulation: all fields private and final (immutable object).
 *   - Association: references the prescribing Doctor.
 */
public class Prescription {

    private final Doctor       doctor;
    private final String       date;
    private final String       diagnosis;
    private final List<String> medicines;
    private final String       notes;

    public Prescription(Doctor doctor, String date, String diagnosis,
                        List<String> medicines, String notes) {
        this.doctor    = doctor;
        this.date      = date;
        this.diagnosis = diagnosis;
        this.medicines = List.copyOf(medicines);   // immutable copy
        this.notes     = (notes != null) ? notes : "";
    }

    // ── Getters ──────────────────────────────────────────
    public Doctor       getDoctor()    { return doctor; }
    public String       getDate()      { return date; }
    public String       getDiagnosis() { return diagnosis; }
    public List<String> getMedicines() { return medicines; }
    public String       getNotes()     { return notes; }

    @Override
    public String toString() {
        return "[" + date + "] Dr. " + doctor.getName()
             + " | Diagnosis: " + diagnosis
             + " | Meds: " + String.join(", ", medicines)
             + (notes.isEmpty() ? "" : " | Notes: " + notes);
    }
}
