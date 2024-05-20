package org.example.dietitan360.Controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.dietitan360.Modules.Nutritionist;

import java.io.IOException;

/**
 * Controlador para la vista de inicio de sesión.
 */
public class LogInController {

    @FXML
    private Button logInButton;

    @FXML
    private PasswordField passwordPaswordField;

    @FXML
    private Button signInButton;

    @FXML
    private TextField userEmailTextField;

    private static String loggedInNutritionistId; // Variable para almacenar el ID del Nutritionist logueado

    /**
     * Obtiene el ID del nutricionista que ha iniciado sesión.
     *
     * @return El ID del nutricionista logueado.
     */
    public static String getLoggedInNutritionistId() {
        return loggedInNutritionistId;
    }


    @FXML
    public void initialize() {

    }

    /**
     * Autentica al usuario utilizando el correo electrónico proporcionado.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void authenticateUser(ActionEvent event) {
        String email = userEmailTextField.getText();

        // URL of your API endpoint
        String url = "http://localhost:8080/ApiDietitan360/nutritionistByEmail/" + email;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                if (!jsonResponse.isEmpty()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Nutritionist nutritionist = objectMapper.readValue(jsonResponse, Nutritionist.class);

                    if (nutritionist != null) {
                            // Successful login
                        loggedInNutritionistId = nutritionist.getId(); // Guardar el ID del Nutritionist
                        System.out.println("Login successful for email: " + email);
                        System.out.println(loggedInNutritionistId);
                        handleLogInButtonAction();
                    } else {
                        // User not found
                        System.out.println("User with email " + email + " not found.");
                        showAlert("Error", "User not found.");
                    }

                } else {
                    // Handle empty response
                    System.out.println("Empty response from the server.");
                    showAlert("Error", "Empty response from the server.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
            System.out.println("Login failed: " + e.getMessage());
            showAlert("Error", "Login failed: " + e.getMessage());
        }
    }

    /**
     * Muestra una alerta con el título y el contenido especificados.
     *
     * @param title   El título de la alerta.
     * @param content El contenido de la alerta.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Maneja el evento de clic en el botón de inicio de sesión.
     */
    private void handleLogInButtonAction() {
        try {
            // Cargar la nueva vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/dietitan360/principal-view.fxml"));
            Parent root = loader.load();

            // Obtener el escenario actual
            Stage stage = (Stage) logInButton.getScene().getWindow();

            // Crear una nueva escena con la nueva vista
            Scene scene = new Scene(root);

            // Establecer la nueva escena en el escenario
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de clic en el botón de registro.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void handleSignInButtonAction(ActionEvent event) {
        try {
            // Cargar la nueva vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/dietitan360/SignIn-view.fxml"));
            Parent root = loader.load();

            // Obtener el escenario actual
            Stage stage = (Stage) signInButton.getScene().getWindow();

            // Crear una nueva escena con la nueva vista
            Scene scene = new Scene(root);

            // Establecer la nueva escena en el escenario
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
