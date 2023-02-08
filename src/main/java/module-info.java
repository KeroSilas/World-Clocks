module com.kerosilas.worldclock {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens com.kerosilas.worldclock to javafx.fxml;
    exports com.kerosilas.worldclock;
    exports com.kerosilas.worldclock.controller;
    opens com.kerosilas.worldclock.controller to javafx.fxml;
}