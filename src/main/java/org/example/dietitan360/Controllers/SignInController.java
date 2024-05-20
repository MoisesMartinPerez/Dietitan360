package org.example.dietitan360.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dietitan360.Modules.Nutritionist;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

/**
 * Controlador para la vista de registro de un nuevo nutricionista.
 */
public class SignInController {

    @FXML
    private Label backLabel;

    @FXML
    private PasswordField passwordPaswordField;

    @FXML
    private Button signInButton;

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
     * Añade el evento de mouse clic al backLabel.
     */
    @FXML
    public void initialize() {
        // Añadir el evento de mouse clic al backLabel
        backLabel.setOnMouseClicked(event -> handleBackLabelClick());
    }

    /**
     * Registra un nuevo nutricionista cuando se hace clic en el botón de registro.
     *
     * @param event El evento de acción que desencadena el registro.
     */
    @FXML
    private void registerNutritionist(ActionEvent event) {
        String name = userNameTextField.getText();
        String lastName = userLastNameTextField.getText();
        String email = userEmailTextField.getText();
        String licenseNumber = userLicenseNumberTextField.getText();
        String password = passwordPaswordField.getText();

        Nutritionist nutritionist = new Nutritionist(null, name, lastName, email, licenseNumber, password);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://localhost:8080/ApiDietitan360/addNutritionists");
            httpPost.setHeader("Content-Type", "application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(nutritionist);

            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                if (response.getStatusLine().getStatusCode() == 201) {
                    System.out.println("Nutritionist registered successfully!");
                    handleBackLabelClick();
                } else {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    System.err.println("Failed to register nutritionist: " + responseBody);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de clic en el backLabel.
     * Carga la vista de inicio de sesión.
     */
    @FXML
    private void handleBackLabelClick() {
        try {
            // Cargar la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/dietitan360/LogIn-view.fxml"));
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
