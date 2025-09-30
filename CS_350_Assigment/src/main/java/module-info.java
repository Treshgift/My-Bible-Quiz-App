module com.example.cs_350_assigment {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;

    requires com.dlsc.formsfx;
    requires java.desktop;
    requires com.google.gson;

    opens com.example.cs_350_assigment to javafx.fxml;
    exports com.example.cs_350_assigment;

}