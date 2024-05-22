module com.example.myperfectproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires fontawesomefx;
    requires com.google.gson;
    requires java.desktop;
    requires javafx.web;
    requires json.simple;


    opens newsaggregator to javafx.fxml,com.google.gson;
    exports newsaggregator;
    exports newsaggregator.data;
    opens newsaggregator.data to com.google.gson, javafx.fxml;
    exports newsaggregator.controller;
    opens newsaggregator.controller to com.google.gson, javafx.fxml;
    exports newsaggregator.datagetter;
    opens newsaggregator.datagetter to com.google.gson, javafx.fxml;
    exports newsaggregator.notification;
    opens newsaggregator.notification to com.google.gson, javafx.fxml;
    exports newsaggregator.display;
    opens newsaggregator.display to com.google.gson, javafx.fxml;
}