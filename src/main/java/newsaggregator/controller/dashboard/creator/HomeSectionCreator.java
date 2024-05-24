package newsaggregator.controller.dashboard.creator;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import newsaggregator.data.RecentReading;
import newsaggregator.data.TableData;
import newsaggregator.notification.ErrorNotification;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Stack;

public class HomeSectionCreator implements Creator {
    private ListView<TitledPane> homeRecentArticles;
    private Label homeTodayReading;
    private Label homeTotalArticles;
    private ObservableList<TitledPane> recentArticles;
    private ObservableList<TableData> dataList;
    private FontAwesomeIcon displayArticleImage;
    private WebView displayArticleWebview;
    private WebEngine engine;

    public HomeSectionCreator(ListView<TitledPane> homeRecentArticles, Label homeTodayReading, Label homeTotalArticles, ObservableList<TitledPane> recentArticles, ObservableList<TableData> dataList, FontAwesomeIcon displayArticleImage, WebView displayArticleWebview, WebEngine engine) {
        this.homeRecentArticles = homeRecentArticles;
        this.homeTodayReading = homeTodayReading;
        this.homeTotalArticles = homeTotalArticles;
        this.recentArticles = recentArticles;
        this.dataList = dataList;
        this.displayArticleImage = displayArticleImage;
        this.displayArticleWebview = displayArticleWebview;
        this.engine = engine;
    }

    private void createPane(Label title, TextArea description, TableData article) {
        TitledPane pane = new TitledPane("", description);
        pane.setGraphic(title);
        title.setOnMouseClicked(event -> {
            if (event.getClickCount()==2) {
                try {
                    Desktop.getDesktop().browse(new URI(article.getUrl()));
                }
                catch (URISyntaxException | IOException e) {
                    new ErrorNotification().showMessage("Something went wrong while loading the article");
                }
            }
            displayArticleImage.setVisible(false);
            displayArticleWebview.setVisible(true);
            engine.load(article.getUrl());
        });
        pane.setExpanded(false);
        recentArticles.add(pane);
    }

    @Override
    public void create() {
        recentArticles = FXCollections.observableArrayList();
        Stack<TableData> recentReadingList = new RecentReading().getRecentList();
        for (int i = recentReadingList.size() - 1; i >= 0; i--) {
            TableData article = recentReadingList.get(i);
            TextArea description = new TextArea(article.getDescription());
            description.setWrapText(true);
            description.setPrefSize(1.0, 1.0);
            description.setEditable(false);
            Label title = new Label(article.getTitle());
            createPane(title, description, article);
        }
        homeRecentArticles.getItems().clear();
        homeRecentArticles.getItems().addAll(recentArticles);
        homeTodayReading.setText(String.valueOf(recentReadingList.size()));
        homeTotalArticles.setText(String.valueOf(dataList.size()));
    }
}
