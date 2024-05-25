package newsaggregator.controller.dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import newsaggregator.controller.dashboard.creator.*;
import newsaggregator.controller.dashboard.trend.TrendFinder;
import newsaggregator.data.article.ArticleLink;
import newsaggregator.data.user.User;
import newsaggregator.data.user.UserDataHandler;
import newsaggregator.data.article.JsonDataGetter;
import newsaggregator.display.DragAndDropWindow;
import newsaggregator.data.article.RecentReading;
import newsaggregator.data.article.TableData;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DashboardController implements Initializable {
    @FXML
    private Button aboutBtn;

    @FXML
    private Hyperlink aboutDocument;

    @FXML
    private Hyperlink aboutSrc;

    @FXML
    private Hyperlink aboutTutorial;

    @FXML
    private AnchorPane aboutView;

    @FXML
    private AnchorPane displayArticle;

    @FXML
    private FontAwesomeIcon displayArticleImage;

    @FXML
    private WebView displayArticleWebview;

    @FXML
    private Button homeBtn;

    @FXML
    private ListView<TitledPane> homeRecentArticles;

    @FXML
    private Label homeTodayReading;

    @FXML
    private Label homeTotalArticles;

    @FXML
    private AnchorPane homeView;

    @FXML
    private Button logoutBtn;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private Button newsBtn;

    @FXML
    private Pagination newsPages;

    @FXML
    private AnchorPane newsView;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<String> sortOption;

    @FXML
    private ComboBox<String> sortYear;

    @FXML
    private ComboBox<String> sortTag;


    @FXML
    private ScrollPane statisticSources;

    @FXML
    private Button statisticBtn;

    @FXML
    private Button statisticFindBtn;

    @FXML
    private TextField statisticSearch;

    @FXML
    private Label statisticTag;

    @FXML
    private LineChart<?, ?> statisticTrend;

    @FXML
    private AnchorPane statisticView;

    @FXML
    private TextField searchBar;

    @FXML
    private AnchorPane searchView;

    @FXML
    private TableView<TableData> searchTable;

    @FXML
    private TableColumn<TableData, String> searchTableAuthor;

    @FXML
    private TableColumn<TableData, String> searchTableDate;

    @FXML
    private TableColumn<TableData, String> searchTableTags;

    @FXML
    private TableColumn<TableData, String> searchTableTitle;

    @FXML
    private TableColumn<TableData, String> searchTableType;

    @FXML
    private TableColumn<TableData, ArticleLink> searchTableLink;

    @FXML
    private Button clearBtn;

    @FXML
    private Button findBtn;

    @FXML
    private Label username;

    String filePath = "src/main/resources/newsaggregator/data.json";
    ObservableList<TableData> dataList = new JsonDataGetter().getData(filePath);
    ObservableList<TableData> afterSortDataList = dataList;
    ObservableList<TitledPane> recentArticles = FXCollections.observableArrayList();
    private final double articlesPerPage = Math.ceil(dataList.size() * 1.0 / 200);
    NewsSectionCreator newsSectionCreator = null;
    StatisticSectionCreator statisticSectionCreator = null;
    TrendFinder finder = new TrendFinder(filePath);

    private WebEngine engine;

    public DashboardController() throws IOException, ParseException {
    }

    public void minimize() {
        Stage stage = (Stage) mainForm.getScene().getWindow();
        stage.setIconified(true);
    }

    public void close() {
        User user = RecentReading.getUser();
        user.setRecentReadings(new RecentReading().setRecentReadingFromUser());
        UserDataHandler.getUsers().add(user);
        new UserDataHandler().updateUserState();
        new UserDataHandler().writeUsersToFile();
        System.exit(0);
    }

    public void logout() throws IOException {
        logoutBtn.getScene().getWindow().hide();
        User user = RecentReading.getUser();
        user.setRecentReadings(new RecentReading().setRecentReadingFromUser());
        UserDataHandler.getUsers().add(user);
        new UserDataHandler().updateUserState();
        new UserDataHandler().writeUsersToFile();
        Parent root = FXMLLoader.load(getClass().getResource("/newsaggregator/login.fxml"));
        new DragAndDropWindow().displayStage(root, new Stage());
    }

    // Navigation bar
    private void setSwitchState(boolean home, boolean news, boolean statistic, boolean search, boolean about) {
        homeView.setVisible(home);
        newsView.setVisible(news);
        statisticView.setVisible(statistic);
        searchView.setVisible(search);
        aboutView.setVisible(about);
    }

    public void switchTab(ActionEvent event) {
        if (event.getSource() == homeBtn) {
            setSwitchState(true, false, false, false, false);
            showRecentReading();
        } else if (event.getSource() == newsBtn) {
            setSwitchState(false, true, false, false, false);
            newsSectionCreator.showAllArticles(dataList);
        } else if (event.getSource() == statisticBtn) {
            setSwitchState(false, false, true, false, false);
        } else if (event.getSource() == searchBtn) {
            setSwitchState(false, false, false, true, false);
        } else if (event.getSource() == aboutBtn) {
            setSwitchState(false, false, false, false, true);
        }
    }

    // Home section
    public void showRecentReading() {
        new HomeSectionCreator(homeRecentArticles, homeTodayReading, homeTotalArticles, recentArticles, dataList, displayArticleImage, displayArticleWebview, engine).create();
    }

    // News Section
    public void clearAllOptions() {
        sortOption.getSelectionModel().clearSelection();
        sortYear.getSelectionModel().clearSelection();
        sortTag.getSelectionModel().clearSelection();
    }

    public void showListAfterSort() {
        newsSectionCreator.sort();
    }

    // Statistics Section
    public void showStatistics() {
        statisticSectionCreator = new StatisticSectionCreator(statisticTag, statisticSources, statisticTrend, statisticSearch, finder);
        statisticSectionCreator.create();
    }

    public void searchKeyword() {
        String keyword = statisticSearch.getText();
        statisticSectionCreator.showTrend(keyword);
    }

    // Search Section
    public void showSearchResults() {
        new SearchSectionCreator(dataList, searchBar, searchTable, searchTableAuthor, searchTableDate, searchTableTags, searchTableTitle, searchTableType, searchTableLink).create();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(RecentReading.getUser().getUsername());
        engine = displayArticleWebview.getEngine();
        newsSectionCreator = new NewsSectionCreator(articlesPerPage, sortOption, sortYear, sortTag, dataList, newsPages, afterSortDataList);
        newsSectionCreator.create();
        showSearchResults();
        showRecentReading();
        showStatistics();
        new AboutSectionCreator(aboutDocument, aboutSrc, aboutTutorial).create();
    }

}