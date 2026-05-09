package hospital.models;

public class Invoice {
    private String id;
    private double amount;
    private String description;
    private boolean isPaid;

    public Invoice(String id, double amount, String description) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.isPaid = false;
    }

    public void pay() { this.isPaid = true; }
    
    @Override
    public String toString() {
        return "Invoice " + id + ": $" + amount + " for " + description + " | Paid: " + isPaid;
    }
}
