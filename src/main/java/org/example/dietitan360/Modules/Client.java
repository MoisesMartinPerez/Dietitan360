package org.example.dietitan360.Modules;

/**
 * Clase que representa un cliente
 * */
public class Client {
    private String id;
    private String name;
    private String lastname;
    private Double height;
    private Double weight;
    private Integer age;
    private String idNutritionist;

    public Client(String id, String name, String lastname, Double height, Double weight, Integer age, String idNutritionist) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.idNutritionist = idNutritionist;
    }

    public Client(){}

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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIdNutritionist() {
        return idNutritionist;
    }

    public void setIdNutritionist(String idNutritionist) {
        this.idNutritionist = idNutritionist;
    }

    @Override
    public String toString() {
        return String.format("%s %s (Age: %d, Height: %.2f m, Weight: %.2f kg)", name, lastname, age, height, weight);
    }
}