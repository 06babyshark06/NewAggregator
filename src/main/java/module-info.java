module com.example.myperfectproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires fontawesomefx;
    requires com.google.gson;
    requires java.desktop;
    requires javafx.web;
    requires json.simple;
    requires org.jsoup;


    opens newsaggregator to javafx.fxml,com.google.gson;
    exports newsaggregator;
    exports newsaggregator.controller;
    opens newsaggregator.controller to com.google.gson, javafx.fxml;
    exports newsaggregator.data.user;
    opens newsaggregator.data.user to com.google.gson, javafx.fxml;
    exports newsaggregator.notification;
    opens newsaggregator.notification to com.google.gson, javafx.fxml;
    exports newsaggregator.display;
    opens newsaggregator.display to com.google.gson, javafx.fxml;
    exports newsaggregator.controller.dashboard.creator;
    opens newsaggregator.controller.dashboard.creator to com.google.gson, javafx.fxml;
    exports newsaggregator.controller.dashboard;
    opens newsaggregator.controller.dashboard to com.google.gson, javafx.fxml;
    exports newsaggregator.data.article;
    opens newsaggregator.data.article to com.google.gson, javafx.fxml;
}