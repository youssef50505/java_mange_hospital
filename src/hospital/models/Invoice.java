package hospital.models;

/**
 * Represents a billing invoice for a consultation.
 *
 * OOP Concepts:
 *   - Encapsulation: fields are private, state change only through pay().
 */
public class Invoice {

    private final String  id;
    private final double  amount;
    private final String  description;
    private final String  date;
    private boolean paid;

    public Invoice(String id, double amount, String description, String date) {
        this.id          = id;
        this.amount      = amount;
        this.description = description;
        this.date        = date;
        this.paid        = false;
    }

    // ── Getters ──────────────────────────────────────────
    public String  getId()          { return id; }
    public double  getAmount()      { return amount; }
    public String  getDescription() { return description; }
    public String  getDate()        { return date; }
    public boolean isPaid()         { return paid; }

    // ── State Transition ─────────────────────────────────
    public void pay() { this.paid = true; }

    @Override
    public String toString() {
        return "INV-" + id
             + " | $" + String.format("%.2f", amount)
             + " | " + description
             + " | " + date
             + " | " + (paid ? "PAID" : "UNPAID");
    }
}
