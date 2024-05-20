package org.example.dietitan360.Modules;

/**
 * Clase que representa una cita.
 */
public class Appointment {
    private String id;
    private String date;
    private String time;
    private String idClient;
    private String idNutritionist;

    // Constructor sin argumentos
    public Appointment() {
    }

    // Constructor con todos los argumentos
    public Appointment(String id, String date, String time, String idClient, String idNutritionist) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.idClient = idClient;
        this.idNutritionist = idNutritionist;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdNutritionist() {
        return idNutritionist;
    }

    public void setIdNutritionist(String idNutritionist) {
        this.idNutritionist = idNutritionist;
    }

    @Override
    public String toString() {
        return String.format("Appointment ID: %s\nDate: %s\nTime: %s\nClient ID: %s\nNutritionist ID: %s",
                id, date, time, idClient, idNutritionist);
    }

}
