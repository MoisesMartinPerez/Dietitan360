package org.example.dietitan360.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.dietitan360.Modules.Appointment;
import org.example.dietitan360.Modules.Client;

import java.io.IOException;

/**
 * Controlador para la gestión de citas.
 */
public class CitaController {

    @FXML
    private TextField appointmentDateTextField;

    @FXML
    private TextField appointmentTimeTextField;

    @FXML
    private ListView<Appointment> appointmentsListView;

    @FXML
    private Label backLabel;

    @FXML
    private Button cancelAppointmentButton;

    @FXML
    private ListView<Client> clientsListView;

    @FXML
    private Button modifyAppointmentButton;

    @FXML
    private Button newAppointmentButton;

    // Lista observable para almacenar las citas
    private ObservableList<Appointment> appointmentsList= FXCollections.observableArrayList();;

    // Lista observable para almacenar los clientes
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();

    /**
     * Inicializa el controlador.
     */
    @FXML
    public void initialize() {
        clientsListView.setItems(clientsList);
        appointmentsListView.setItems(appointmentsList);

        loadClientsByNutritionist();
        loadAppointmentsByNutritionist();
    }

    /**
     * Carga las citas del nutricionista actual.
     */
    private void loadAppointmentsByNutritionist() {
        String idNutritionist = LogInController.getLoggedInNutritionistId();
        String url = "http://localhost:8080/ApiAppointment360/nutritionistAppointments/" + idNutritionist;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(url);
            getRequest.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
                if (response.getStatusLine().getStatusCode() == 200) { // 200 es el código para OK
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    Appointment[] appointmentsArray = objectMapper.readValue(jsonResponse, Appointment[].class);

                    appointmentsList.clear();
                    appointmentsList.addAll(appointmentsArray);
                    System.out.println("Appointments loaded successfully!");
                } else {
                    System.out.println("Failed to load appointments: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error, Failed to load appointments: " + e.getMessage());
        }
    }

    /**
     * Carga los clientes del nutricionista actual.
     */
    private void loadClientsByNutritionist() {
        String idNutritionist = LogInController.getLoggedInNutritionistId();
        String url = "http://localhost:8080/ApiClients360/nutritionistClients/" + idNutritionist;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(url);
            getRequest.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
                if (response.getStatusLine().getStatusCode() == 200) { // 200 es el código para OK
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    Client[] clientsArray = objectMapper.readValue(jsonResponse, Client[].class);

                    clientsList.clear();
                    clientsList.addAll(clientsArray);
                    System.out.println("Clients loaded successfully!");
                } else {
                    System.out.println("Failed to load clients: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea una nueva cita.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void createNewAppointment(ActionEvent event) {
        // Obtener la fecha y hora del TextField
        String date = appointmentDateTextField.getText();
        String time = appointmentTimeTextField.getText();

        // Obtener el ID del nutricionista
        String nutritionistId = LogInController.getLoggedInNutritionistId();

        // Obtener el cliente seleccionado en clientsListView
        Client selectedClient = clientsListView.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            // Manejar el caso en que no se haya seleccionado ningún cliente
            System.out.println("No se ha seleccionado ningún cliente.");
            return;
        }
        String clientId = selectedClient.getId();

        // Crear un nuevo Appointment con los datos obtenidos
        Appointment newAppointment = new Appointment(null, date, time, clientId, nutritionistId);

        // Enviar la solicitud HTTP para crear el nuevo Appointment
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost("http://localhost:8080/ApiAppointment360/addAppointment");
            StringEntity params = new StringEntity(new ObjectMapper().writeValueAsString(newAppointment));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 201) { // 201 es el código para CREATED
                    System.out.println("Nuevo Appointment creado exitosamente!");
                    // Actualizar la lista de citas después de agregar una nueva cita
                    loadAppointmentsByNutritionist();
                } else {
                    System.out.println("Error al crear el nuevo Appointment: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al crear el nuevo Appointment: " + e.getMessage());
        }
    }

    /**
     * Elimina la cita seleccionada.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void deleteSelectedAppointment(ActionEvent event) {
        // Obtener el Appointment seleccionado en appointmentsListView
        Appointment selectedAppointment = appointmentsListView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            // Manejar el caso en que no se haya seleccionado ningún Appointment
            System.out.println("No se ha seleccionado ninguna cita.");
            return;
        }
        String appointmentId = selectedAppointment.getId();

        // Enviar la solicitud HTTP para eliminar el Appointment seleccionado
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete("http://localhost:8080/ApiAppointment360/deleteAppointment/" + appointmentId);
            request.setHeader("content-type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) { // 200 es el código para OK
                    System.out.println("Cita eliminada exitosamente!");
                    // Actualizar la lista de citas después de eliminar la cita seleccionada
                    loadAppointmentsByNutritionist();
                } else {
                    System.out.println("Error al eliminar la cita: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar la cita: " + e.getMessage());
        }
    }

    /**
     * Modifica la cita seleccionada.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void modifySelectedAppointment(ActionEvent event) {
        // Obtener el Appointment seleccionado en appointmentsListView
        Appointment selectedAppointment = appointmentsListView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            // Manejar el caso en que no se haya seleccionado ningún Appointment
            System.out.println("No se ha seleccionado ninguna cita.");
            return;
        }

        // Obtener la fecha y hora del TextField
        String date = appointmentDateTextField.getText();
        String time = appointmentTimeTextField.getText();

        // Obtener el ID del cliente seleccionado en clientsListView
        Client selectedClient = clientsListView.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            // Manejar el caso en que no se haya seleccionado ningún cliente
            System.out.println("No se ha seleccionado ningún cliente.");
            return;
        }
        String clientId = selectedClient.getId();

        // Modificar el Appointment seleccionado con los nuevos valores
        selectedAppointment.setDate(date);
        selectedAppointment.setTime(time);
        selectedAppointment.setIdClient(clientId);

        // Enviar la solicitud HTTP para actualizar el Appointment modificado
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut("http://localhost:8080/ApiAppointment360/updateAppointment/" + selectedAppointment.getId());
            StringEntity params = new StringEntity(new ObjectMapper().writeValueAsString(selectedAppointment));
            request.setHeader("content-type", "application/json");
            request.setEntity(params);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) { // 200 es el código para OK
                    System.out.println("Cita modificada exitosamente!");
                    // Actualizar la lista de citas después de modificar la cita seleccionada
                    loadAppointmentsByNutritionist();
                } else {
                    System.out.println("Error al modificar la cita: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al modificar la cita: " + e.getMessage());
        }
    }


    /**
     * Maneja el evento de clic en la etiqueta de retroceso.
     *
     * @param event El evento de ratón que desencadena este método.
     */
    @FXML
    private void handleBackLabelClick(MouseEvent event) {
        try {
            // Cargar la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/dietitan360/principal-view.fxml"));
            Parent root = loader.load();

            // Obtener el escenario actual
            Stage stage = (Stage) backLabel.getScene().getWindow();

            // Crear una nueva escena con la vista de login
            Scene scene = new Scene(root);

            // Establecer la nueva escena en el escenario
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
