package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "employees")
public class Employee {

    @Id
    private String email;
    private String name;
    private String password;
    private Birthdate birthdate;
    private List<String> roles;

    public Employee() {}

    public Employee(String email, String name, String password, Birthdate birthdate, List<String> roles) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.birthdate = birthdate;
        this.roles = roles;
    }

    // Getters & Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Birthdate getBirthdate() { return birthdate; }
    public void setBirthdate(Birthdate birthdate) { this.birthdate = birthdate; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}