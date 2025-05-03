package models;

public class User {
    private String id;
    private String username;
    private String password;
    private String email;
    private String contactNumber;
    private String role;

    public User(String id, String username, String password, String email, String contactNumber, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.contactNumber = contactNumber;
        this.role = role;
    }

    public String toFileFormat() {
        return String.join(";", id, username, password, email, contactNumber, role);
    }

    public static User fromFileFormat(String line) {
        String[] parts = line.split(";");
        return new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
}
