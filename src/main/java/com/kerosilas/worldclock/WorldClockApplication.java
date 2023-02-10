package com.kerosilas.worldclock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class WorldClockApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WorldClockApplication.class.getResource("WorldClock.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("World Clocks");
        stage.getIcons().add(new Image("file:src/main/resources/com/kerosilas/worldclock/clock.png"));
        stage.setMinWidth(422);
        stage.setMinHeight(530);
        stage.setWidth(812);
        stage.setHeight(920);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}