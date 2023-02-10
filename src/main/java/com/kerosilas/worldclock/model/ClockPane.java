package com.kerosilas.worldclock.model;

import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class ClockPane {

    private final VBox vBox;
    private final Pane pane;
    private final Label timeLabel;
    private final Label pmamLabel;
    private final Rotate hourRotate;
    private final Rotate minuteRotate;
    private final Rotate secondRotate;

    private final int CIRCLE_RADIUS = 140;

    private final TimeZone tz;

    public ClockPane(TimeZone tz) {
        this.tz = tz;

        //Setup pane that will hold clock face and hands
        pane = new Pane();
        pane.setMaxSize(CIRCLE_RADIUS*2,CIRCLE_RADIUS*2);

        //Setup rotate objects that will rotate hands
        hourRotate = new Rotate(0,CIRCLE_RADIUS,CIRCLE_RADIUS);
        minuteRotate = new Rotate(0,CIRCLE_RADIUS,CIRCLE_RADIUS);
        secondRotate = new Rotate(0,CIRCLE_RADIUS,CIRCLE_RADIUS);

        //Setup label that will say PM or AM inside of clock face
        pmamLabel = new Label();
        pmamLabel.setFont(Font.font("Roboto", FontWeight.BOLD, FontPosture.REGULAR, 14));
        pmamLabel.setLayoutX(CIRCLE_RADIUS-11);
        pmamLabel.setLayoutY(CIRCLE_RADIUS+82);

        MFXCheckbox checkBox = new MFXCheckbox();
        checkBox.setTranslateX(8);

        timeLabel = new Label();
        timeLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        timeLabel.setTranslateX(270);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(checkBox, timeLabel);

        Label tzLabel = new Label();
        tzLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        tzLabel.setText(tz.getDisplayName());

        vBox = new VBox();
        vBox.setPrefSize(390,390);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(16);
        vBox.getChildren().addAll(hBox, createClockFace(), tzLabel);

        addClickEvent();
    }

    public VBox getVBox() {
        return vBox;
    }

    //Keeps the time and corresponding hands updated
    //Gets run every second from a Timeline timer started in WorldClockController
    public void updateHands() {
        LocalTime time = LocalTime.now(tz.toZoneId());

        int hour = time.getHour() % 12;
        int minute = time.getMinute();
        int second = time.getSecond();

        timeLabel.setText(time.format(DateTimeFormatter.ofPattern("hh:mm a")).toUpperCase());
        pmamLabel.setText(time.format(DateTimeFormatter.ofPattern("a")).toUpperCase());

        hourRotate.setAngle(hour * (360F / 12F) + (minute / 60F) * (360F / 12F));
        minuteRotate.setAngle(minute * (360F / 60F));
        secondRotate.setAngle(second * (360F / 60F));
    }

    private Line createHourHand() {
        Line hand = new Line(CIRCLE_RADIUS,68,CIRCLE_RADIUS,CIRCLE_RADIUS+11);
        hand.setStroke(Color.BLACK);
        hand.setStrokeWidth(10);
        hand.getTransforms().add(hourRotate);
        return hand;
    }

    private Line createMinuteHand() {
        Line hand = new Line(CIRCLE_RADIUS,16,CIRCLE_RADIUS,CIRCLE_RADIUS+13);
        hand.setStroke(Color.BLACK);
        hand.setStrokeWidth(6);
        hand.getTransforms().add(minuteRotate);
        return hand;
    }

    private Line createSecondHand() {
        Line hand = new Line(CIRCLE_RADIUS,12,CIRCLE_RADIUS,CIRCLE_RADIUS+15);
        hand.setStroke(Color.RED);
        hand.setStrokeWidth(2);
        hand.getTransforms().add(secondRotate);
        return hand;
    }

    private Pane createClockFace() {
        Circle circle = new Circle(CIRCLE_RADIUS);
        circle.setStrokeWidth(4);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);
        circle.setLayoutX(CIRCLE_RADIUS);
        circle.setLayoutY(CIRCLE_RADIUS);
        pane.getChildren().add(circle);

        for (int i = 0; i < 12; i++) {
            Line hourTick = new Line(10,CIRCLE_RADIUS,30,CIRCLE_RADIUS);
            hourTick.setStroke(Color.BLACK);
            hourTick.setStrokeWidth(8);
            Rotate rotate = new Rotate();
            rotate.setAngle((360F / 12F) * i);
            rotate.setPivotX(CIRCLE_RADIUS);
            rotate.setPivotY(CIRCLE_RADIUS);
            hourTick.getTransforms().add(rotate);
            pane.getChildren().add(hourTick);
        }

        for (int i = 0; i < 60; i++) {
            Line minuteTick = new Line(8,CIRCLE_RADIUS,15,CIRCLE_RADIUS);
            minuteTick.setStroke(Color.BLACK);
            minuteTick.setStrokeWidth(4);
            Rotate rotate = new Rotate();
            rotate.setAngle((360F / 60F) * i);
            rotate.setPivotX(CIRCLE_RADIUS);
            rotate.setPivotY(CIRCLE_RADIUS);
            minuteTick.getTransforms().add(rotate);
            pane.getChildren().add(minuteTick);
        }

        Circle hourMinutePin = new Circle(9);
        hourMinutePin.setFill(Color.BLACK);
        hourMinutePin.setLayoutX(CIRCLE_RADIUS);
        hourMinutePin.setLayoutY(CIRCLE_RADIUS);

        Circle secondPin = new Circle(3);
        secondPin.setFill(Color.RED);
        secondPin.setLayoutX(CIRCLE_RADIUS);
        secondPin.setLayoutY(CIRCLE_RADIUS);

        pane.getChildren().addAll(pmamLabel, createHourHand(), createMinuteHand(), hourMinutePin, createSecondHand(), secondPin);

        return pane;
    }

    //Adds the ability to click on the VBox to select the checkbox
    //Also adds a small animation to the VBox when hovered or clicked
    private void addClickEvent() {
        vBox.setOnMouseEntered(event -> {
            vBox.setCursor(Cursor.HAND);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(75), vBox);
            scaleTransition.setByX(0.010);
            scaleTransition.setByY(0.010);
            scaleTransition.setCycleCount(1);
            scaleTransition.setAutoReverse(false);
            scaleTransition.play();

            //Forces scale to be correct when animation has finished
            //This was done because the animation was not always finishing at the correct scale when spamming the animation
            scaleTransition.setOnFinished(e -> {
                vBox.setScaleX(1+0.010);
                vBox.setScaleY(1+0.010);
            });
        });

        vBox.setOnMouseExited(event -> {
            vBox.setCursor(Cursor.DEFAULT);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(75), vBox);
            scaleTransition.setByX(-0.010);
            scaleTransition.setByY(-0.010);
            scaleTransition.setCycleCount(1);
            scaleTransition.setAutoReverse(false);
            scaleTransition.play();

            scaleTransition.setOnFinished(e -> {
                vBox.setScaleX(1);
                vBox.setScaleY(1);
            });
        });

        vBox.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(75), vBox);
                scaleTransition.setByX(-0.020);
                scaleTransition.setByY(-0.020);
                scaleTransition.setCycleCount(1);
                scaleTransition.setAutoReverse(false);
                scaleTransition.play();

                scaleTransition.setOnFinished(e -> {
                    vBox.setScaleX(1-0.010);
                    vBox.setScaleY(1-0.010);
                });
            }
        });

        vBox.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(75), vBox);
                scaleTransition.setByX(0.020);
                scaleTransition.setByY(0.020);
                scaleTransition.setCycleCount(1);
                scaleTransition.setAutoReverse(false);
                scaleTransition.play();

                scaleTransition.setOnFinished(e -> {
                    vBox.setScaleX(1+0.010);
                    vBox.setScaleY(1+0.010);
                });

                HBox hBox = (HBox) vBox.getChildren().get(0);
                MFXCheckbox checkBox = (MFXCheckbox) hBox.getChildren().get(0);
                checkBox.setSelected(!checkBox.isSelected());
            }
        });
    }
}
