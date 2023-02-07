package com.kerosilas.analogclock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AnalogClockApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AnalogClockApplication.class.getResource("AnalogClock.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Analog Clock");
        stage.setMinWidth(420);
        stage.setMinHeight(480);
        stage.setWidth(810);
        stage.setHeight(870);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}