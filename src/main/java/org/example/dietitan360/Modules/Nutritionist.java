package org.example.dietitan360.Modules;

/**
 * Clase que representa un Nutricionista o usuario
 */
public class Nutritionist {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String licenseNumber;
    private String password;

    public Nutritionist(String id, String name, String lastName, String email, String licenseNumber, String password) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.licenseNumber = licenseNumber;
        this.password = password;
    }
    public Nutritionist(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
