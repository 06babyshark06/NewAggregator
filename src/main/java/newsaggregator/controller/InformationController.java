package newsaggregator.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import newsaggregator.data.article.RecentReading;
import newsaggregator.data.article.TableData;

import java.net.URL;
import java.util.ResourceBundle;

public class InformationController implements Initializable {
    @FXML
    private TextField author;

    @FXML
    private TextField createDate;

    @FXML
    private TextArea description;

    @FXML
    private TextField homePage;

    @FXML
    private TextField link;

    @FXML
    private TextField tags;

    @FXML
    private Label title;

    @FXML
    private TextField type;

    @FXML
    private Button close;

    public void close() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    public void displayAllInformation() {
        TableData data = RecentReading.getRecentList().peek();
        author.setText(data.getAuthor());
        createDate.setText(data.getCreate_date());
        description.setText(data.getDescription());
        homePage.setText(data.getWeb_url());
        link.setText(data.getUrl());
        tags.setText(data.getTag());
        title.setText(data.getTitle());
        type.setText(data.getType());
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayAllInformation();
    }
}
