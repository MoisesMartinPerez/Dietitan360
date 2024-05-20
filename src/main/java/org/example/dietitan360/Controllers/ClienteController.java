package org.example.dietitan360.Controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.dietitan360.Modules.Client;

import java.io.IOException;
import java.util.List;

/**
 * Controlador para la gestión de clientes.
 */
public class ClienteController {

    @FXML
    private TextField ageTextField;

    @FXML
    private Label backLabel;

    @FXML
    private Button dropClientButton;

    @FXML
    private TextField heightTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private Button modifyClientButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button newClientButton;

    @FXML
    private TextField weightTextField;

    @FXML
    private ListView<Client> clientsListView;
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        clientsListView.setItems(clientsList);
        loadClientsByNutritionist();
    }

    /**
     * Carga los clientes asociados al nutricionista logueado.
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
                    showAlert("Error", "Failed to load clients: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load clients: " + e.getMessage());
        }
    }

    /**
     * Muestra una alerta con el título y el mensaje especificados.
     *
     * @param title   El título de la alerta.
     * @param message El mensaje de la alerta.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /**
     * Maneja el evento de agregar un nuevo cliente.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void addNewClient(ActionEvent event) {
        String name = nameTextField.getText();
        String lastname = lastNameTextField.getText();
        Double height = Double.parseDouble(heightTextField.getText());
        Double weight = Double.parseDouble(weightTextField.getText());
        Integer age = Integer.parseInt(ageTextField.getText());
        String idNutritionist = LogInController.getLoggedInNutritionistId();

        // Construir el objeto Client
        Client newClient = new Client();
        newClient.setName(name);
        newClient.setLastname(lastname);
        newClient.setHeight(height);
        newClient.setWeight(weight);
        newClient.setAge(age);
        newClient.setIdNutritionist(idNutritionist);

        String url = "http://localhost:8080/ApiClients360/addClient";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(url);
            postRequest.setHeader("Content-Type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            StringEntity entity = new StringEntity(objectMapper.writeValueAsString(newClient));
            postRequest.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                if (response.getStatusLine().getStatusCode() == 201) { // 201 es el código para CREATED
                    System.out.println("Client added successfully!");
                    showAlert("Success", "Client added successfully!");

                    // Añadir el nuevo cliente directamente a la lista observable
                    clientsList.add(newClient);
                } else {
                    System.out.println("Failed to add client: " + response.getStatusLine().getReasonPhrase());
                    showAlert("Error", "Failed to add client: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add client: " + e.getMessage());
        }
    }

    /**
     * Elimina el cliente seleccionado de la lista de clientes.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void deleteSelectedClient(ActionEvent event) {
        Client selectedClient = clientsListView.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert("Error", "No client selected!");
            return;
        }

        String clientId = selectedClient.getId();
        String url = "http://localhost:8080/ApiClients360/deleteClient/" + clientId;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete deleteRequest = new HttpDelete(url);
            deleteRequest.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(deleteRequest)) {
                if (response.getStatusLine().getStatusCode() == 200) { // 200 es el código para OK
                    System.out.println("Client deleted successfully!");
                    showAlert("Success", "Client deleted successfully!");

                    // Eliminar el cliente de la lista observable
                    clientsList.remove(selectedClient);
                } else {
                    System.out.println("Failed to delete client: " + response.getStatusLine().getReasonPhrase());
                    showAlert("Error", "Failed to delete client: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete client: " + e.getMessage());
        }
    }

    /**
     * Modifica el cliente seleccionado con los nuevos datos ingresados.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void modifySelectedClient(ActionEvent event) {
        Client selectedClient = clientsListView.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert("Error", "No client selected!");
            return;
        }

        String clientId = selectedClient.getId();
        String name = nameTextField.getText();
        String lastname = lastNameTextField.getText();
        Double height = Double.parseDouble(heightTextField.getText());
        Double weight = Double.parseDouble(weightTextField.getText());
        Integer age = Integer.parseInt(ageTextField.getText());
        String idNutritionist = selectedClient.getIdNutritionist(); // Mantén el mismo idNutritionist

        // Construir el objeto Client actualizado
        Client updatedClient = new Client();
        updatedClient.setId(clientId);
        updatedClient.setName(name);
        updatedClient.setLastname(lastname);
        updatedClient.setHeight(height);
        updatedClient.setWeight(weight);
        updatedClient.setAge(age);
        updatedClient.setIdNutritionist(idNutritionist);

        String url = "http://localhost:8080/ApiClients360/updateClient/" + clientId;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut putRequest = new HttpPut(url);
            putRequest.setHeader("Content-Type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            StringEntity entity = new StringEntity(objectMapper.writeValueAsString(updatedClient));
            putRequest.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(putRequest)) {
                if (response.getStatusLine().getStatusCode() == 200) { // 200 es el código para OK
                    System.out.println("Client updated successfully!");
                    showAlert("Success", "Client updated successfully!");

                    // Actualizar el cliente en la lista observable
                    int selectedIndex = clientsListView.getSelectionModel().getSelectedIndex();
                    clientsList.set(selectedIndex, updatedClient);
                } else {
                    System.out.println("Failed to update client: " + response.getStatusLine().getReasonPhrase());
                    showAlert("Error", "Failed to update client: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update client: " + e.getMessage());
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
