package com.kerosilas.worldclock.controller;

import com.kerosilas.worldclock.model.ClockPane;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.util.*;

public class WorldClockController {

    private final ObservableList<String> tzList = FXCollections.observableArrayList();

    @FXML private MFXButton addButton;
    @FXML private MFXFilterComboBox<String> comboBox;
    @FXML private MFXCheckbox selectAllCheckbox;
    @FXML private FlowPane flowPane;

    @FXML void handleAdd() {
        ClockPane clockPane = new ClockPane(TimeZone.getTimeZone(comboBox.getSelectedItem()));
        clockPane.getVBox().setTranslateY(-5);
        flowPane.getChildren().add(clockPane.getVBox());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> clockPane.updateHands()),
                new KeyFrame(Duration.seconds(0.5))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        //Animations for adding clock
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(150), clockPane.getVBox());
        translateTransition.setByY(5);
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(false);
        translateTransition.play();
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(150), clockPane.getVBox());
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(false);
        fadeTransition.play();
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

        //Animations for removing clocks
        for (VBox vBox : toDelete) {
            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(150), vBox);
            translateTransition.setByY(-5);
            translateTransition.setCycleCount(1);
            translateTransition.setAutoReverse(false);
            translateTransition.play();
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(150), vBox);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setCycleCount(1);
            fadeTransition.setAutoReverse(false);
            fadeTransition.play();

            //Removes clocks from flowpane after animation is finished
            fadeTransition.setOnFinished(e -> flowPane.getChildren().remove(vBox));
        }

        selectAllCheckbox.setSelected(false);
    }

    @FXML void handleReset() {
        //Loads default clocks after animation is finished
        flowPane.getChildren().clear();
        flowPane.setTranslateY(-5);
        defaultClocks();

        //Animations for loading default clocks
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(150), flowPane);
        translateTransition.setByY(5);
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(false);
        translateTransition.play();
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(150), flowPane);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(false);
        fadeTransition.play();

        selectAllCheckbox.setSelected(false);
    }

    public void initialize() {
        //Setup combobox with all available timezones
        String[] id = TimeZone.getAvailableIDs();
        for (String s : id) {
            tzList.add(TimeZone.getTimeZone(s).getID());
        }
        comboBox.setItems(tzList);

        //Enable add button once a selection has been made in combobox
        comboBox.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (!Objects.equals(oldValue, newValue) && comboBox.getSelectedItem() != null) {
                addButton.setDisable(false);
            }
        });

        //Load a default arrangement of timezone clocks: CEST, JST, EST, PST
        defaultClocks();

        //Setup select all checkbox
        selectAllCheckbox.selectedProperty().addListener((ov, oldValue, newValue) -> {
            if (!Objects.equals(oldValue, newValue)) {
                for (int i = 0; i < flowPane.getChildren().size(); i++) {
                    VBox vBox = (VBox) flowPane.getChildren().get(i);
                    HBox hBox = (HBox) vBox.getChildren().get(0);
                    MFXCheckbox checkBox = (MFXCheckbox) hBox.getChildren().get(0);
                    checkBox.setSelected(newValue);
                }
            }
        });
    }

    private void defaultClocks() {
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
                }),
                new KeyFrame(Duration.seconds(0.5))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}