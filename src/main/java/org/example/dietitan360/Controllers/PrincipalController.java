package org.example.dietitan360.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador para la vista principal de la aplicación.
 */
public class PrincipalController{

    @FXML
    private Button logOutButton;

    @FXML
    private Button myAppointmentsButton;

    @FXML
    private Button myClientsButton;

    @FXML
    private Button myProfileButton;

    @FXML
    private Button myRecipesButton;

    /**
     * Maneja el evento de clic en el botón "Mis Citas".
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void handleMyAppointmentsButtonAction(ActionEvent event) {
        try {
            // Cargar la nueva vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/dietitan360/cita-view.fxml"));
            Parent root = loader.load();

            // Obtener el escenario actual
            Stage stage = (Stage) myAppointmentsButton.getScene().getWindow();

            // Crear una nueva escena con la nueva vista
            Scene scene = new Scene(root);

            // Establecer la nueva escena en el escenario
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de clic en el botón "Mis Clientes".
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void handleMyClientsButtonAction(ActionEvent event) {
        try {
            // Cargar la nueva vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/dietitan360/cliente-view.fxml"));
            Parent root = loader.load();

            // Obtener el escenario actual
            Stage stage = (Stage) myClientsButton.getScene().getWindow();

            // Crear una nueva escena con la nueva vista
            Scene scene = new Scene(root);

            // Establecer la nueva escena en el escenario
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de clic en el botón "Mi Perfil".
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void handleMyProfileButtonAction(ActionEvent event) {
        try {
            // Cargar la nueva vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/dietitan360/usuario-view.fxml"));
            Parent root = loader.load();

            // Obtener el escenario actual
            Stage stage = (Stage) myProfileButton.getScene().getWindow();

            // Crear una nueva escena con la nueva vista
            Scene scene = new Scene(root);

            // Establecer la nueva escena en el escenario
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de clic en el botón "Mis Recetas".
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void handleMyRecipesButtonButtonAction(ActionEvent event) {
        try {
            // Cargar la nueva vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/dietitan360/receta-view.fxml"));
            Parent root = loader.load();

            // Obtener el escenario actual
            Stage stage = (Stage) myRecipesButton.getScene().getWindow();

            // Crear una nueva escena con la nueva vista
            Scene scene = new Scene(root);

            // Establecer la nueva escena en el escenario
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de clic en el botón "Cerrar Sesión".
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void handleLogOutButtonAction(ActionEvent event) {
        // Cerrar la aplicación
        Platform.exit();
    }

}
