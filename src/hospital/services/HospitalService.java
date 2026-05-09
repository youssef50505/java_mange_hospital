package hospital.services;

import hospital.models.*;
import java.util.ArrayList;
import java.util.List;

// Singleton Service acting as an in-memory database
public class HospitalService {
    private static HospitalService instance;
    
    private List<User> users;
    private List<Appointment> appointments;
    private int idCounter = 1;

    private HospitalService() {
        users = new ArrayList<>();
        appointments = new ArrayList<>();
        
        // Seed some data for the GUI presentation
        users.add(new Admin("admin", "System Administrator", "admin123"));
        
        Doctor d1 = new Doctor("doc1", "Dr. Sarah Smith", "pass", "Cardiology");
        d1.addSlot("10:00 AM");
        d1.addSlot("11:00 AM");
        users.add(d1);
        
        Patient p1 = new Patient("pat1", "John Doe", "pass");
        p1.setMedicalHistory("Mild asthma. Allergies to penicillin.");
        users.add(p1);
    }

    public static HospitalService getInstance() {
        if (instance == null) {
            instance = new HospitalService();
        }
        return instance;
    }

    public User login(String id, String password) {
        return users.stream()
                .filter(u -> u.getId().equals(id) && u.authenticate(password))
                .findFirst()
                .orElse(null);
    }

    public void addDoctor(Doctor doctor) { users.add(doctor); }
    public void addPatient(Patient patient) { users.add(patient); }
    
    public List<Doctor> getAllDoctors() {
        List<Doctor> docs = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Doctor) docs.add((Doctor) u);
        }
        return docs;
    }
    
    public List<Patient> getAllPatients() {
        List<Patient> pats = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Patient) pats.add((Patient) u);
        }
        return pats;
    }

    public Appointment bookAppointment(Patient patient, Doctor doctor, String slot) {
        Appointment appt = new Appointment("APP" + (idCounter++), patient, doctor, slot);
        appointments.add(appt);
        doctor.removeSlot(slot);
        return appt;
    }

    public List<Appointment> getAppointmentsForUser(User user) {
        List<Appointment> userAppts = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getDoctor().equals(user) || a.getPatient().equals(user)) {
                userAppts.add(a);
            }
        }
        return userAppts;
    }
}
