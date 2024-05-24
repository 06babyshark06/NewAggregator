package newsaggregator.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import newsaggregator.data.User;
import newsaggregator.data.filereader.UserDataHandler;
import newsaggregator.notification.ErrorNotification;
import newsaggregator.display.DragAndDropWindow;
import newsaggregator.data.RecentReading;
import newsaggregator.notification.InformationNotification;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button close;

    @FXML
    private Button loginButton;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private PasswordField password;

    @FXML
    private Label signInUp;

    @FXML
    private StackPane stackForm;

    @FXML
    private Hyperlink switcher;

    @FXML
    private TextField username;

    List<User> users;
    boolean isSignedUp = false;

    private void signUp(String username, String password) {
        users.add(new User(username, password, new ArrayList<>()));
        new InformationNotification().showMessage("Sign Up Successfully");
        new UserDataHandler().writeUsersToFile();
        users = new UserDataHandler().readUsersFromFile();
    }

    private void login(User user) {
        new InformationNotification().showMessage("Welcome " + user.getUsername() + ", check your Internet connection to use all the feature available in this application");
        loginButton.getScene().getWindow().hide();
        try {
            new RecentReading().getRecentReadingFromUser(user);
            Parent root = FXMLLoader.load(getClass().getResource("/newsaggregator/dashboard.fxml"));
            new DragAndDropWindow().displayStage(root, new Stage());
        } catch (IOException e) {
            e.printStackTrace();
            new ErrorNotification().showMessage("Could not load dashboard.fxml or something went wrong");
        }
    }

    public void loginOrSignUp() {
        String usernameText = username.getText();
        String passwordText = password.getText();
        if (usernameText.isEmpty() || passwordText.isEmpty()) {
            new ErrorNotification().showMessage("Please fill all the fields");
        } else {
            for (User user : users) {
                if (user.getUsername().equals(usernameText)) {
                    if (!isSignedUp) {
                        if (user.getPassword().equals(passwordText)) login(user);
                        else new ErrorNotification().showMessage("Please check your username and password");
                    } else {
                        new ErrorNotification().showMessage("This username has already been registered");
                    }
                    return;
                }
            }
            if (isSignedUp) {
                signUp(usernameText, passwordText);
            } else new ErrorNotification().showMessage("Please check your username and password");
        }
    }

    private void switchState(String oldValue, String newValue) {
        isSignedUp = !isSignedUp;
        signInUp.setText(newValue);
        loginButton.setText(newValue);
        switcher.setText(oldValue);
    }

    public void switchToSignInUp() {
        if (isSignedUp) {
            switchState("Sign Up", "Login");
        } else {
            switchState("Login", "Sign Up");
        }
    }

    public void exit() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        users = new UserDataHandler().readUsersFromFile();
    }
}