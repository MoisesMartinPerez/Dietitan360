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
import org.example.dietitan360.Modules.Ingredient;
import org.example.dietitan360.Modules.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.dietitan360.Controllers.LogInController.getLoggedInNutritionistId;

/**
 * Controlador para gestionar las recetas en la aplicación.
 */
public class RecetaController {

    @FXML
    private Button addIngredientButton;

    @FXML
    private Button addStepButton;

    @FXML
    private Label backLabel;

    @FXML
    private Button createRecipeButton;

    @FXML
    private Button deleteRecipeButton;

    @FXML
    private TextField ingredientAmountTextField;

    @FXML
    private TextField ingredientNameTextField;

    @FXML
    private TextField ingredientUnitTextField;

    @FXML
    private ListView<Ingredient> ingredientsListView;

    @FXML
    private Button modifyIngredientButton;

    @FXML
    private Button modifyStepButton;

    @FXML
    private TextField recipeNameTextField;

    @FXML
    private TextField recipeStepTextField;

    @FXML
    private ListView<Recipe> recipesListView;

    @FXML
    private ListView<String> stepsListView;

    private ObservableList<Recipe> recipesList = FXCollections.observableArrayList();
    private ObservableList<Ingredient> ingredientsList = FXCollections.observableArrayList();
    private ObservableList<String> stepsList = FXCollections.observableArrayList();

    /**
     * Inicializa el controlador y carga las recetas del nutricionista.
     */
    @FXML
    public void initialize() {
        recipesListView.setItems(recipesList);
        ingredientsListView.setItems(ingredientsList);
        stepsListView.setItems(stepsList);

        loadRecipesByNutritionist();

        recipesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ingredientsList.clear();
                ingredientsList.addAll(newValue.getIngredients());

                stepsList.clear();
                stepsList.addAll(newValue.getSteps());
            }
        });
    }

    /**
     * Crea una nueva receta con los datos ingresados en los campos de texto.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void createNewRecipe(ActionEvent event) {
        // Obtener los valores del TextField
        String recipeName = recipeNameTextField.getText();
        String ingredientName = ingredientNameTextField.getText();
        String ingredientUnit = ingredientUnitTextField.getText();
        double ingredientAmount;
        try {
            ingredientAmount = Double.parseDouble(ingredientAmountTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Cantidad de ingrediente no válida");
            return;
        }
        String step = recipeStepTextField.getText();

        // Obtener el ID del nutricionista
        String nutritionistId = LogInController.getLoggedInNutritionistId();

        // Crear el objeto Ingredient
        Ingredient newIngredient = new Ingredient(ingredientName, ingredientAmount, ingredientUnit);

        // Crear las listas de ingredientes y pasos
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(newIngredient);

        List<String> steps = new ArrayList<>();
        steps.add(step);

        // Crear el objeto Recipe con los datos obtenidos
        Recipe newRecipe = new Recipe(recipeName, ingredients, steps, nutritionistId);

        // Verificar que el nutritionistId se ha asignado correctamente
        System.out.println("Nutritionist ID: " + newRecipe.getNutritionistId());

        // Enviar la solicitud HTTP para crear el nuevo Recipe
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost("http://localhost:8080/ApiRecipe360/addRecipe");
            StringEntity params = new StringEntity(new ObjectMapper().writeValueAsString(newRecipe));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 201) { // 201 es el código para CREATED
                    System.out.println("Nueva receta creada exitosamente!");
                    // Agregar la nueva receta a la lista observable
                    recipesList.add(newRecipe);
                } else {
                    System.out.println("Error al crear la nueva receta: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al crear la nueva receta: " + e.getMessage());
        }
    }

    /**
     * Elimina la receta seleccionada.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void deleteSelectedRecipe(ActionEvent event) {
        // Obtener la receta seleccionada
        Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();
        if (selectedRecipe == null) {
            System.out.println("No se ha seleccionado ninguna receta.");
            return;
        }

        // Enviar la solicitud HTTP DELETE para eliminar la receta
        String url = "http://localhost:8080/ApiRecipe360/deleteRecipe/" + selectedRecipe.getId();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete deleteRequest = new HttpDelete(url);
            deleteRequest.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(deleteRequest)) {
                if (response.getStatusLine().getStatusCode() == 200) { // 200 es el código para OK
                    System.out.println("Receta eliminada exitosamente!");
                    // Eliminar la receta de la lista observable
                    recipesListView.getItems().remove(selectedRecipe);
                    // Limpiar las listas de ingredientes y pasos
                    ingredientsListView.getItems().clear();
                    stepsListView.getItems().clear();
                } else {
                    System.out.println("Error al eliminar la receta: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar la receta: " + e.getMessage());
        }
    }

    /**
     * Añade un ingrediente a la receta seleccionada.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void addIngredientToSelectedRecipe(ActionEvent event) {
        // Obtener la receta seleccionada
        Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();
        if (selectedRecipe == null) {
            System.out.println("No se ha seleccionado ninguna receta.");
            return;
        }

        // Obtener los valores de los TextFields
        String ingredientName = ingredientNameTextField.getText();
        String ingredientUnit = ingredientUnitTextField.getText();
        double ingredientAmount;
        try {
            ingredientAmount = Double.parseDouble(ingredientAmountTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Cantidad de ingrediente no válida");
            return;
        }

        // Verificar que los campos no estén vacíos
        if (ingredientName.isEmpty() || ingredientUnit.isEmpty()) {
            System.out.println("Nombre o unidad del ingrediente no puede estar vacío");
            return;
        }

        // Crear el objeto Ingredient
        Ingredient newIngredient = new Ingredient(ingredientName, ingredientAmount, ingredientUnit);

        // Añadir el nuevo ingrediente a la receta seleccionada
        selectedRecipe.getIngredients().add(newIngredient);

        // Enviar la solicitud HTTP PUT para actualizar la receta
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut("http://localhost:8080/ApiRecipe360/updateRecipe/" + selectedRecipe.getId());
            StringEntity params = new StringEntity(new ObjectMapper().writeValueAsString(selectedRecipe));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) { // 200 es el código para OK
                    System.out.println("Ingrediente añadido exitosamente!");
                    // Actualizar la lista de ingredientes en la vista
                    ingredientsListView.getItems().add(newIngredient);
                } else {
                    System.out.println("Error al añadir el ingrediente: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al añadir el ingrediente: " + e.getMessage());
        }
    }

    /**
     * Modifica el ingrediente seleccionado.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void modifySelectedIngredient(ActionEvent event) {
        // Obtener el ingrediente seleccionado
        Ingredient selectedIngredient = ingredientsListView.getSelectionModel().getSelectedItem();
        if (selectedIngredient == null) {
            System.out.println("No se ha seleccionado ningún ingrediente.");
            return;
        }

        // Obtener los valores de los TextFields
        String ingredientName = ingredientNameTextField.getText();
        String ingredientUnit = ingredientUnitTextField.getText();
        double ingredientAmount;
        try {
            ingredientAmount = Double.parseDouble(ingredientAmountTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Cantidad de ingrediente no válida");
            return;
        }

        // Verificar que los campos no estén vacíos
        if (ingredientName.isEmpty() || ingredientUnit.isEmpty()) {
            System.out.println("Nombre o unidad del ingrediente no puede estar vacío");
            return;
        }

        // Actualizar los valores del ingrediente seleccionado
        selectedIngredient.setName(ingredientName);
        selectedIngredient.setAmount(ingredientAmount);
        selectedIngredient.setUnit(ingredientUnit);

        // Obtener la receta seleccionada
        Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();

        // Enviar la solicitud HTTP PUT para actualizar la receta
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut("http://localhost:8080/ApiRecipe360/updateRecipe/" + selectedRecipe.getId());
            StringEntity params = new StringEntity(new ObjectMapper().writeValueAsString(selectedRecipe));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) { // 200 es el código para OK
                    System.out.println("Ingrediente modificado exitosamente!");
                    // Actualizar la lista de ingredientes en la vista
                    ingredientsListView.refresh();
                } else {
                    System.out.println("Error al modificar el ingrediente: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al modificar el ingrediente: " + e.getMessage());
        }
    }

    /**
     * Añade un nuevo paso a la receta seleccionada.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void addStepToSelectedRecipe(ActionEvent event) {
        // Obtener la receta seleccionada
        Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();
        if (selectedRecipe == null) {
            System.out.println("No se ha seleccionado ninguna receta.");
            return;
        }

        // Obtener el valor del TextField
        String step = recipeStepTextField.getText();

        // Verificar que el campo no esté vacío
        if (step.isEmpty()) {
            System.out.println("El campo de paso no puede estar vacío");
            return;
        }

        // Añadir el nuevo paso a la receta seleccionada
        selectedRecipe.getSteps().add(step);

        // Enviar la solicitud HTTP PUT para actualizar la receta
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut("http://localhost:8080/ApiRecipe360/updateRecipe/" + selectedRecipe.getId());
            StringEntity params = new StringEntity(new ObjectMapper().writeValueAsString(selectedRecipe));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) { // 200 es el código para OK
                    System.out.println("Paso añadido exitosamente!");
                    // Actualizar la lista de pasos en la vista
                    stepsListView.getItems().add(step);
                } else {
                    System.out.println("Error al añadir el paso: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al añadir el paso: " + e.getMessage());
        }
    }

    /**
     * Modifica el paso seleccionado de la receta.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void modifySelectedStep(ActionEvent event) {
        // Obtener el índice del paso seleccionado
        int selectedStepIndex = stepsListView.getSelectionModel().getSelectedIndex();
        if (selectedStepIndex == -1) {
            System.out.println("No se ha seleccionado ningún paso.");
            return;
        }

        // Obtener el valor del TextField
        String step = recipeStepTextField.getText();

        // Verificar que el campo no esté vacío
        if (step.isEmpty()) {
            System.out.println("El campo de paso no puede estar vacío");
            return;
        }

        // Obtener la receta seleccionada
        Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();
        if (selectedRecipe == null) {
            System.out.println("No se ha seleccionado ninguna receta.");
            return;
        }

        // Actualizar el paso seleccionado en la receta
        selectedRecipe.getSteps().set(selectedStepIndex, step);

        // Enviar la solicitud HTTP PUT para actualizar la receta
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut("http://localhost:8080/ApiRecipe360/updateRecipe/" + selectedRecipe.getId());
            StringEntity params = new StringEntity(new ObjectMapper().writeValueAsString(selectedRecipe));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) { // 200 es el código para OK
                    System.out.println("Paso modificado exitosamente!");
                    // Actualizar la lista de pasos en la vista
                    stepsListView.getItems().set(selectedStepIndex, step);
                } else {
                    System.out.println("Error al modificar el paso: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al modificar el paso: " + e.getMessage());
        }
    }

    /**
     * Carga las recetas asociadas al nutricionista actualmente autenticado.
     */
    private void loadRecipesByNutritionist() {
        String nutritionistId = LogInController.getLoggedInNutritionistId();
        String url = "http://localhost:8080/ApiRecipe360/nutritionistRecipes/" + nutritionistId;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(url);
            getRequest.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    Recipe[] recipesArray = objectMapper.readValue(jsonResponse, Recipe[].class);

                    recipesList.clear();
                    recipesList.addAll(recipesArray);
                    System.out.println("Recetas cargadas exitosamente!");
                } else {
                    System.out.println("Error al cargar las recetas: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar las recetas: " + e.getMessage());
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
