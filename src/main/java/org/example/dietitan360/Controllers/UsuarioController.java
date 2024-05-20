package org.example.dietitan360.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.dietitan360.Modules.Nutritionist;

import java.io.IOException;

/**
 * Controlador para la vista de perfil del usuario.
 */
public class UsuarioController {

    @FXML
    private Label backLabel;

    @FXML
    private Button deleteProfileButton;

    @FXML
    private Button modifyProfileButton;

    @FXML
    private PasswordField passwordPaswordField;

    @FXML
    private TextField userEmailTextField;

    @FXML
    private TextField userLastNameTextField;

    @FXML
    private TextField userLicenseNumberTextField;

    @FXML
    private TextField userNameTextField;

    /**
     * Inicializa el controlador.
     * Carga los datos del usuario.
     */
    public void initialize() {
        loadUserData();
    }

    /**
     * Carga los datos del usuario desde el servidor.
     */
    private void loadUserData() {
        String userId = LogInController.getLoggedInNutritionistId();

        // URL of your API endpoint to fetch Nutritionist data by ID
        String url = "http://localhost:8080/ApiDietitan360/findNutritionists/" + userId;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                Nutritionist nutritionist = objectMapper.readValue(jsonResponse, Nutritionist.class);

                if (nutritionist != null) {
                    // Set data to UI components
                    userNameTextField.setText(nutritionist.getName());
                    userLastNameTextField.setText(nutritionist.getLastName());
                    userEmailTextField.setText(nutritionist.getEmail());
                    userLicenseNumberTextField.setText(nutritionist.getLicenseNumber());
                    // Assuming you don't want to show password in UI
                    // passwordPaswordField.setText(nutritionist.getPassword());
                } else {
                    // Handle case where Nutritionist is not found
                    System.out.println("Nutritionist not found.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
            System.out.println("Failed to load user data: " + e.getMessage());
        }
    }

    /**
     * Actualiza el perfil del usuario con los datos introducidos.
     *
     * @param event El evento de acción que desencadena la actualización.
     */
    @FXML
    private void updateProfile(ActionEvent event) {
        String userId = LogInController.getLoggedInNutritionistId();
        String newName = userNameTextField.getText();
        String newLastName = userLastNameTextField.getText();
        String newEmail = userEmailTextField.getText();
        String newLicenseNumber = userLicenseNumberTextField.getText();
        // Assuming you have a method in your NutritionistService to update user data
        Nutritionist updatedNutritionist = new Nutritionist(userId, newName, newLastName, newEmail, newLicenseNumber, "");

        // URL of your API endpoint to update Nutritionist data
        String url = "http://localhost:8080/ApiDietitan360/updateNutritionists";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(url);
            StringEntity params = new StringEntity(new ObjectMapper().writeValueAsString(updatedNutritionist));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Handle response as needed
                //showAlert("User updated", "User updated successfully.");
                System.out.println("Nutritionist data updated successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
            System.out.println("Failed to update user data: " + e.getMessage());
        }
    }

    /**
     * Elimina el perfil del usuario cuando se hace clic en el botón de eliminar perfil.
     *
     * @param event El evento de acción que desencadena la eliminación.
     */
    @FXML
    private void deleteProfile(ActionEvent event) {
        String userId = LogInController.getLoggedInNutritionistId();

        // URL of your API endpoint to delete Nutritionist
        String url = "http://localhost:8080/ApiDietitan360/deleteNutritionists/" + userId;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(url);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Handle response as needed
                //showAlert("User deleted", "User deleted successfully.");
                System.out.println("Nutritionist data deleted successfully.");
                Platform.exit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
            System.out.println("Failed to delete user data: " + e.getMessage());
        }
    }

    /**
     * Maneja el evento de clic en el backLabel.
     * Carga la vista principal.
     *
     * @param event El evento de mouse que desencadena la acción.
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
