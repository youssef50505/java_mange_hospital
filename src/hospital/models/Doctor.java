package hospital.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Doctor — views patients, prescribes medicines, manages availability.
 *
 * OOP Concepts:
 *   - Inheritance: extends User.
 *   - Polymorphism: overrides getRole() and toString().
 *   - Encapsulation: availableSlots returned as unmodifiable list (defensive copy).
 */
public class Doctor extends User {

    private String specialization;
    private double consultationFee;
    private final List<String> availableSlots;

    public Doctor(String id, String name, String password,
                  String specialization, double consultationFee) {
        super(id, name, id + "@medcore.com", "N/A", password);
        this.specialization  = specialization;
        this.consultationFee = consultationFee;
        this.availableSlots  = new ArrayList<>();
    }

    // ── Getters ──────────────────────────────────────────
    public String getSpecialization()  { return specialization; }
    public double getConsultationFee() { return consultationFee; }

    /** Returns an unmodifiable view — callers cannot corrupt internal state. */
    public List<String> getAvailableSlots() {
        return Collections.unmodifiableList(availableSlots);
    }

    // ── Slot Management ──────────────────────────────────
    public void addSlot(String slot) {
        if (slot != null && !slot.isEmpty() && !availableSlots.contains(slot)) {
            availableSlots.add(slot);
        }
    }

    public void removeSlot(String slot) {
        availableSlots.remove(slot);
    }

    public boolean hasAvailableSlots() {
        return !availableSlots.isEmpty();
    }

    // ── Overrides ────────────────────────────────────────
    @Override
    public String getRole() {
        return "Doctor";
    }

    @Override
    public String toString() {
        return "Dr. " + name + " — " + specialization;
    }
}
