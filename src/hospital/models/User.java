package hospital.models;

public abstract class User {
    protected String id;
    protected String name;
    protected String password;

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public boolean authenticate(String password) { return this.password.equals(password); }
    
    public abstract String getRole();
}
