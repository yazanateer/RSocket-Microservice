package model.boundary;

public class EmployeeLoginInputBoundary {
    private String email;
    private String password;

    public EmployeeLoginInputBoundary() {}

    public EmployeeLoginInputBoundary(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters & Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}