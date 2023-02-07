package com.kerosilas.analogclock.model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

import java.time.LocalTime;
import java.util.TimeZone;

public class ClockPane {

    private final VBox vBox;
    private final Pane pane;
    private final Rotate hourRotate;
    private final Rotate minuteRotate;
    private final Rotate secondRotate;

    private final int CIRCLE_RADIUS = 140;

    private final TimeZone tz;

    public ClockPane(TimeZone tz) {
        this.tz = tz;

        pane = new Pane();
        pane.setMaxSize(CIRCLE_RADIUS*2,CIRCLE_RADIUS*2);

        vBox = new VBox();
        vBox.setPrefSize(390,390);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(8);

        hourRotate = new Rotate(0,CIRCLE_RADIUS,CIRCLE_RADIUS);
        minuteRotate = new Rotate(0,CIRCLE_RADIUS,CIRCLE_RADIUS);
        secondRotate = new Rotate(0,CIRCLE_RADIUS,CIRCLE_RADIUS);

        Label label = new Label();
        label.setText(tz.getID());

        vBox.getChildren().addAll(createClockFace(), label);
    }

    public VBox getVBox() {
        return vBox;
    }

    public void updateHands() {
        LocalTime time = LocalTime.now(tz.toZoneId());

        int hour = time.getHour() % 12;
        int minute = time.getMinute();
        int second = time.getSecond();

        hourRotate.setAngle(hour * (360F / 12F) + (minute / 60F) * (360F / 12F));
        minuteRotate.setAngle(minute * (360F / 60F));
        secondRotate.setAngle(second * (360F / 60F));
    }

    private Line createHourHand() {
        Line hand = new Line(CIRCLE_RADIUS,60,CIRCLE_RADIUS,CIRCLE_RADIUS);
        hand.setStroke(Color.BLACK);
        hand.setStrokeWidth(10);
        hand.getTransforms().add(hourRotate);
        return hand;
    }

    private Line createMinuteHand() {
        Line hand = new Line(CIRCLE_RADIUS,15,CIRCLE_RADIUS,CIRCLE_RADIUS);
        hand.setStroke(Color.BLACK);
        hand.setStrokeWidth(5);
        hand.getTransforms().add(minuteRotate);
        return hand;
    }

    private Line createSecondHand() {
        Line hand = new Line(CIRCLE_RADIUS,10,CIRCLE_RADIUS,CIRCLE_RADIUS);
        hand.setStroke(Color.RED);
        hand.setStrokeWidth(2);
        hand.getTransforms().add(secondRotate);
        return hand;
    }

    private Pane createClockFace() {
        Circle circle = new Circle(CIRCLE_RADIUS);
        circle.setStrokeWidth(5);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);
        circle.setLayoutX(CIRCLE_RADIUS);
        circle.setLayoutY(CIRCLE_RADIUS);
        pane.getChildren().add(circle);

        for (int i = 0; i < 12; i++) {
            Line hourTick = new Line(10,CIRCLE_RADIUS,35,CIRCLE_RADIUS);
            hourTick.setStroke(Color.BLACK);
            hourTick.setStrokeWidth(10);
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
            minuteTick.setStrokeWidth(5);
            Rotate rotate = new Rotate();
            rotate.setAngle((360F / 60F) * i);
            rotate.setPivotX(CIRCLE_RADIUS);
            rotate.setPivotY(CIRCLE_RADIUS);
            minuteTick.getTransforms().add(rotate);
            pane.getChildren().add(minuteTick);
        }

        Circle pin = new Circle(8);
        pin.setFill(Color.BLACK);
        pin.setLayoutX(CIRCLE_RADIUS);
        pin.setLayoutY(CIRCLE_RADIUS);

        pane.getChildren().addAll(createHourHand(), createMinuteHand(), pin, createSecondHand());

        return pane;
    }
}
