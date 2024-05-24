package newsaggregator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import newsaggregator.notification.ErrorNotification;
import newsaggregator.display.DragAndDropWindow;

import java.io.IOException;

public class Starter extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage.setTitle("News Aggregator");
            new DragAndDropWindow().displayStage(root, stage);
        } catch (IOException e) {
            new ErrorNotification().showMessage("Could not load login.fxml file or something went wrong");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}