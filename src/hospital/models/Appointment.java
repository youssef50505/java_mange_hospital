package hospital.models;

public class Appointment {
    private String id;
    private Patient patient;
    private Doctor doctor;
    private String timeSlot;
    private String status;

    public Appointment(String id, Patient patient, Doctor doctor, String timeSlot) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.timeSlot = timeSlot;
        this.status = "Scheduled";
    }

    public String getId() { return id; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public String getTimeSlot() { return timeSlot; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return "Appt: " + id + " | " + doctor.getName() + " with " + patient.getName() + " @ " + timeSlot + " [" + status + "]";
    }
}
