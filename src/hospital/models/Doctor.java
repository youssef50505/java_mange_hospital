package hospital.models;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends User {
    private String specialization;
    private List<String> availableSlots;

    public Doctor(String id, String name, String password, String specialization) {
        super(id, name, password);
        this.specialization = specialization;
        this.availableSlots = new ArrayList<>();
    }

    public String getSpecialization() { return specialization; }
    public List<String> getAvailableSlots() { return availableSlots; }
    
    public void addSlot(String slot) {
        availableSlots.add(slot);
    }
    
    public void removeSlot(String slot) {
        availableSlots.remove(slot);
    }

    @Override
    public String getRole() {
        return "Doctor";
    }

    @Override
    public String toString() {
        return name + " (" + specialization + ")";
    }
}
