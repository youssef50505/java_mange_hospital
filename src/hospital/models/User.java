package hospital.models;

/**
 * Abstract base class for all system users.
 *
 * OOP Concepts:
 *   - Abstraction: getRole() is abstract — forces subclasses to provide an identity.
 *   - Encapsulation: Fields are protected/private, accessed only through getters/setters.
 *   - Inheritance: Admin, Doctor, Patient all extend this class.
 */
public abstract class User {

    protected String id;
    protected String name;
    protected String email;
    protected String phone;
    private   String password;

    public User(String id, String name, String email, String phone, String password) {
        this.id       = id;
        this.name     = name;
        this.email    = email;
        this.phone    = phone;
        this.password = password;
    }

    // ── Getters ──────────────────────────────────────────
    public String getId()    { return id; }
    public String getName()  { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    // ── Setters ──────────────────────────────────────────
    public void setName(String name)   { this.name  = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }

    // ── Authentication ───────────────────────────────────
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    // ── Abstract — each subclass defines its own role ────
    public abstract String getRole();

    @Override
    public String toString() {
        return "[" + getRole() + "] " + name + " (ID: " + id + ")";
    }
}
