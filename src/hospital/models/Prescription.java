package hospital.models;
import java.util.List;

public class Prescription {
    private Doctor doctor;
    private String date;
    private List<String> medicines;

    public Prescription(Doctor doctor, String date, List<String> medicines) {
        this.doctor = doctor;
        this.date = date;
        this.medicines = medicines;
    }

    @Override
    public String toString() {
        return "Date: " + date + " | Dr. " + doctor.getName() + " | Meds: " + String.join(", ", medicines);
    }
}
