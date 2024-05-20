package org.example.dietitan360;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/***
 * @Author: Nasser Moisés Martín Pérez & María A.Martín De La Cruz
 * @Date: 14/03/2024
 * @From:IES Ana Luisa Benítez
 */

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("LogIn-view.fxml"));
        Scene scene = new Scene(fxmlLoader.
                load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}