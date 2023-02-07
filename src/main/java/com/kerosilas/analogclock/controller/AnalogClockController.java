package com.kerosilas.analogclock.controller;

import com.kerosilas.analogclock.model.ClockPane;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

import java.util.Objects;
import java.util.TimeZone;

public class AnalogClockController {

    private final ObservableList<String> tzList = FXCollections.observableArrayList();

    @FXML
    private MFXButton addButton, removeButton;

    @FXML
    private MFXFilterComboBox<String> comboBox;

    @FXML
    private FlowPane flowPane;

    @FXML
    void handleAdd(ActionEvent event) {
        ClockPane clockPane = new ClockPane(TimeZone.getTimeZone(comboBox.getSelectedItem()));
        flowPane.getChildren().add(clockPane.getVBox());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> clockPane.updateHands()),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    void handleRemove(ActionEvent event) {
        //not implemented yet
    }

    public void initialize() {
        String[] id = TimeZone.getAvailableIDs();
        for (String s : id) {
            tzList.add(TimeZone.getTimeZone(s).getID());
        }
        comboBox.setItems(tzList);

        TimeZone tz = TimeZone.getTimeZone("Europe/Copenhagen");
        ClockPane clockPane = new ClockPane(tz);
        flowPane.getChildren().add(clockPane.getVBox());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> clockPane.updateHands()),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        comboBox.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (!Objects.equals(oldValue, newValue) && comboBox.getSelectedItem() != null) {
                addButton.setDisable(false);
            }
        });
    }
}