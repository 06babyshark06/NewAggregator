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
    exports newsaggregator.data.filereader;
    opens newsaggregator.data.filereader to com.google.gson, javafx.fxml;
    exports newsaggregator.notification;
    opens newsaggregator.notification to com.google.gson, javafx.fxml;
    exports newsaggregator.display;
    opens newsaggregator.display to com.google.gson, javafx.fxml;
    exports newsaggregator.controller.dashboard.creator;
    opens newsaggregator.controller.dashboard.creator to com.google.gson, javafx.fxml;
    exports newsaggregator.controller.dashboard;
    opens newsaggregator.controller.dashboard to com.google.gson, javafx.fxml;
}