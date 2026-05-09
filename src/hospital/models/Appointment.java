package hospital.models;

/**
 * Represents a scheduled appointment between a Patient and a Doctor.
 *
 * OOP Concepts:
 *   - Encapsulation: all fields private, accessed via getters.
 *   - Association: links Patient and Doctor objects.
 *   - Enum: Status uses a type-safe enum instead of raw Strings.
 */
public class Appointment {

    /** Type-safe status — avoids magic strings. */
    public enum Status { SCHEDULED, COMPLETED, CANCELLED }

    private final String  id;
    private final Patient patient;
    private final Doctor  doctor;
    private final String  date;
    private final String  timeSlot;
    private Status status;

    public Appointment(String id, Patient patient, Doctor doctor,
                       String date, String timeSlot) {
        this.id       = id;
        this.patient  = patient;
        this.doctor   = doctor;
        this.date     = date;
        this.timeSlot = timeSlot;
        this.status   = Status.SCHEDULED;
    }

    // ── Getters ──────────────────────────────────────────
    public String  getId()       { return id; }
    public Patient getPatient()  { return patient; }
    public Doctor  getDoctor()   { return doctor; }
    public String  getDate()     { return date; }
    public String  getTimeSlot() { return timeSlot; }
    public Status  getStatus()   { return status; }

    // ── State Transitions ────────────────────────────────
    public void complete() { this.status = Status.COMPLETED; }
    public void cancel()   { this.status = Status.CANCELLED; }

    @Override
    public String toString() {
        return id + " | Dr. " + doctor.getName()
             + " with " + patient.getName()
             + " | " + date + " @ " + timeSlot
             + " [" + status + "]";
    }
}
