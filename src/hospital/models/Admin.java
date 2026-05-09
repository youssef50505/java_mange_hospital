package hospital.models;

/**
 * Admin — manages doctors, patients, and system settings.
 *
 * OOP Concepts:
 *   - Inheritance: extends User.
 *   - Polymorphism: overrides getRole() to return "Admin".
 */
public class Admin extends User {

    public Admin(String id, String name, String password) {
        super(id, name, "admin@medcore.com", "N/A", password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }
}
