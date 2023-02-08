package com.kerosilas.worldclock.controller;

import com.kerosilas.worldclock.model.ClockPane;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class WorldClockController {

    private final ObservableList<String> tzList = FXCollections.observableArrayList();

    @FXML private MFXButton addButton;
    @FXML private MFXFilterComboBox<String> comboBox;
    @FXML private FlowPane flowPane;
    @FXML private Label currentTime;

    @FXML void handleAdd() {
        ClockPane clockPane = new ClockPane(TimeZone.getTimeZone(comboBox.getSelectedItem()));
        flowPane.getChildren().add(clockPane.getVBox());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> clockPane.updateHands()),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML void handleRemove() {
        List<VBox> toDelete = new ArrayList<>();
        for (int i = 0; i < flowPane.getChildren().size(); i++) {
            VBox vBox = (VBox) flowPane.getChildren().get(i);
            HBox hBox = (HBox) vBox.getChildren().get(0);
            MFXCheckbox checkBox = (MFXCheckbox) hBox.getChildren().get(0);

            if (checkBox.isSelected())
                toDelete.add(vBox);
        }
        flowPane.getChildren().removeAll(toDelete);
    }

    public void initialize() {
        String[] id = TimeZone.getAvailableIDs();
        for (String s : id) {
            tzList.add(TimeZone.getTimeZone(s).getID());
        }
        comboBox.setItems(tzList);
        comboBox.getMFXContextMenu().setHeight(1000);

        ClockPane clockPane1 = new ClockPane(TimeZone.getTimeZone("Europe/Copenhagen"));
        ClockPane clockPane2 = new ClockPane(TimeZone.getTimeZone("Asia/Tokyo"));
        ClockPane clockPane3 = new ClockPane(TimeZone.getTimeZone("US/Eastern"));
        ClockPane clockPane4 = new ClockPane(TimeZone.getTimeZone("US/Pacific"));
        flowPane.getChildren().addAll(clockPane1.getVBox(), clockPane2.getVBox(), clockPane3.getVBox(), clockPane4.getVBox());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    clockPane1.updateHands();
                    clockPane2.updateHands();
                    clockPane3.updateHands();
                    clockPane4.updateHands();
                    currentTime.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")).toUpperCase());
                }),
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