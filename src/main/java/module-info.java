module com.kerosilas.analogclock {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens com.kerosilas.analogclock to javafx.fxml;
    exports com.kerosilas.analogclock;
    exports com.kerosilas.analogclock.controller;
    opens com.kerosilas.analogclock.controller to javafx.fxml;
}