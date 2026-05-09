package hospital.services;

import hospital.models.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton service — centralized data manager for the entire application.
 *
 * OOP Concepts:
 *   - Singleton Pattern: only one instance exists (private constructor + static accessor).
 *   - Abstraction: GUI never touches raw data — it calls service methods instead.
 *   - Polymorphism: filters users by runtime type (instanceof) to return typed lists.
 */
public class HospitalService {

    // ── Singleton Instance ───────────────────────────────
    private static HospitalService instance;

    // ── Data Storage ─────────────────────────────────────
    private final List<User>        users;
    private final List<Appointment> appointments;
    private int appointmentCounter = 1;
    private int invoiceCounter     = 1;

    // ── Private Constructor (Singleton) ──────────────────
    private HospitalService() {
        users        = new ArrayList<>();
        appointments = new ArrayList<>();
        seedData();
    }

    /** Returns the single shared instance. Creates it on first call. */
    public static HospitalService getInstance() {
        if (instance == null) {
            instance = new HospitalService();
        }
        return instance;
    }

    // ═══════════════════════════════════════════════════════
    //  AUTHENTICATION
    // ═══════════════════════════════════════════════════════

    /** Authenticates a user by ID and password. Returns null on failure. */
    public User login(String id, String password) {
        if (id == null || password == null) return null;
        return users.stream()
                .filter(u -> u.getId().equals(id) && u.authenticate(password))
                .findFirst()
                .orElse(null);
    }

    // ═══════════════════════════════════════════════════════
    //  REGISTRATION
    // ═══════════════════════════════════════════════════════

    /** Registers a new doctor. Returns false if ID already exists. */
    public boolean addDoctor(Doctor doctor) {
        if (doctor == null || findUserById(doctor.getId()) != null) return false;
        users.add(doctor);
        return true;
    }

    /** Registers a new patient. Returns false if ID already exists. */
    public boolean addPatient(Patient patient) {
        if (patient == null || findUserById(patient.getId()) != null) return false;
        users.add(patient);
        return true;
    }

    // ═══════════════════════════════════════════════════════
    //  QUERIES (uses Polymorphism — filters by runtime type)
    // ═══════════════════════════════════════════════════════

    public User findUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Doctor> getAllDoctors() {
        return users.stream()
                .filter(u -> u instanceof Doctor)
                .map(u -> (Doctor) u)
                .collect(Collectors.toList());
    }

    public List<Patient> getAllPatients() {
        return users.stream()
                .filter(u -> u instanceof Patient)
                .map(u -> (Patient) u)
                .collect(Collectors.toList());
    }

    public List<Appointment> getAllAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    public List<Appointment> getAppointmentsForUser(User user) {
        return appointments.stream()
                .filter(a -> a.getDoctor().equals(user) || a.getPatient().equals(user))
                .collect(Collectors.toList());
    }

    // ═══════════════════════════════════════════════════════
    //  APPOINTMENT BOOKING + AUTO-BILLING
    // ═══════════════════════════════════════════════════════

    /**
     * Books an appointment and auto-generates an invoice for the patient.
     * Returns the new Appointment, or null if the slot is unavailable.
     */
    public Appointment bookAppointment(Patient patient, Doctor doctor,
                                       String date, String slot) {
        if (patient == null || doctor == null || slot == null) return null;

        // Create appointment
        String apptId = "APP" + (appointmentCounter++);
        Appointment appt = new Appointment(apptId, patient, doctor, date, slot);
        appointments.add(appt);
        doctor.removeSlot(slot);

        // Auto-generate invoice (Billing System)
        String invId = "INV" + (invoiceCounter++);
        Invoice invoice = new Invoice(invId, doctor.getConsultationFee(),
                "Consultation with Dr. " + doctor.getName(), date);
        patient.addInvoice(invoice);

        return appt;
    }

    // ═══════════════════════════════════════════════════════
    //  STATISTICS
    // ═══════════════════════════════════════════════════════

    public int getDoctorCount()      { return getAllDoctors().size(); }
    public int getPatientCount()     { return getAllPatients().size(); }
    public int getAppointmentCount() { return appointments.size(); }

    // ═══════════════════════════════════════════════════════
    //  SEED DATA (demo accounts for testing)
    // ═══════════════════════════════════════════════════════

    private void seedData() {
        // Admin
        users.add(new Admin("admin", "System Administrator", "admin123"));

        // Doctors
        Doctor d1 = new Doctor("doc1", "Sarah Smith",  "pass", "Cardiology",  150.0);
        d1.addSlot("09:00 AM"); d1.addSlot("10:00 AM");
        d1.addSlot("11:00 AM"); d1.addSlot("02:00 PM");

        Doctor d2 = new Doctor("doc2", "Ahmed Hassan", "pass", "Neurology",   200.0);
        d2.addSlot("10:00 AM"); d2.addSlot("01:00 PM"); d2.addSlot("03:00 PM");

        Doctor d3 = new Doctor("doc3", "Maria Garcia", "pass", "Pediatrics",  120.0);
        d3.addSlot("09:00 AM"); d3.addSlot("11:00 AM"); d3.addSlot("04:00 PM");

        users.add(d1); users.add(d2); users.add(d3);

        // Patients
        Patient p1 = new Patient("pat1", "John Doe",    "pass", 35, "Male",   "O+");
        p1.setMedicalHistory("Mild asthma. Allergic to penicillin.");

        Patient p2 = new Patient("pat2", "Jane Wilson",  "pass", 28, "Female", "A-");
        p2.setMedicalHistory("No known allergies. Regular checkups.");

        users.add(p1); users.add(p2);
    }
}
