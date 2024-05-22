package newsaggregator.notification;

import javafx.scene.control.Alert;

public class InformationNotification implements Notifiable {
    @Override
    public void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
