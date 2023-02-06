package com.kerosilas.analogclock.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AnalogClockController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}