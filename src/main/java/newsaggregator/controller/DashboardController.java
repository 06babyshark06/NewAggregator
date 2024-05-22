package newsaggregator.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import newsaggregator.data.ArticleLink;
import newsaggregator.data.User;
import newsaggregator.datagetter.UserDataHandler;
import newsaggregator.datagetter.JsonDataGetter;
import newsaggregator.display.DragAndDropWindow;
import newsaggregator.data.RecentReading;
import newsaggregator.data.TableData;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class DashboardController implements Initializable {
    @FXML
    private Button aboutBtn;

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
    private Button statisticBtn;

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

    ObservableList<TableData> dataList = new JsonDataGetter().getData("src/main/resources/newsaggregator/data.json");
    ObservableList<TableData> afterSortDataList = dataList;
    ObservableList<TitledPane> recentArticles = FXCollections.observableArrayList();

    private WebEngine engine;
    private final double articlesPerPage = Math.ceil(dataList.size() * 1.0 / 100);

    private final String[] options = {"Latest", "Oldest"};
    private final String[] years = {"2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017"};
    private final String[] tags = {"AI", "Crypto", "Blockchain", "News"};

    public void minimize() {
        Stage stage = (Stage) mainForm.getScene().getWindow();
        stage.setIconified(true);
    }

    public void close() {
        System.exit(0);
    }

    public void logout() throws IOException {
        logoutBtn.getScene().getWindow().hide();
        User user = new RecentReading().getUser();
        user.setRecentReadings(new RecentReading().setRecentReadingFromUser());
        UserDataHandler.users.add(user);
        new UserDataHandler().updateUserState();
        new UserDataHandler().writeObjectToFile();
        Parent root = FXMLLoader.load(getClass().getResource("/newsaggregator/login.fxml"));
        new DragAndDropWindow().displayStage(root, new Stage());
    }

    // Navigation bar
    public void switchTab(ActionEvent event) {
        if (event.getSource() == homeBtn) {
            homeView.setVisible(true);
            newsView.setVisible(false);
            statisticView.setVisible(false);
            searchView.setVisible(false);
            aboutView.setVisible(false);
            showRecentReading();
        } else if (event.getSource() == newsBtn) {
            homeView.setVisible(false);
            newsView.setVisible(true);
            statisticView.setVisible(false);
            searchView.setVisible(false);
            aboutView.setVisible(false);
            showAllArticles(dataList);
        } else if (event.getSource() == statisticBtn) {
            homeView.setVisible(false);
            newsView.setVisible(false);
            statisticView.setVisible(true);
            searchView.setVisible(false);
            aboutView.setVisible(false);
        } else if (event.getSource() == searchBtn) {
            homeView.setVisible(false);
            newsView.setVisible(false);
            statisticView.setVisible(false);
            searchView.setVisible(true);
            aboutView.setVisible(false);
        } else if (event.getSource() == aboutBtn) {
            homeView.setVisible(false);
            newsView.setVisible(false);
            statisticView.setVisible(false);
            searchView.setVisible(false);
            aboutView.setVisible(true);
        }
    }

    // Home section
    // Create a pane
    public void createPane(Label title, TextArea description, TableData article) {
        TitledPane pane = new TitledPane("", description);
        pane.setGraphic(title);
        title.setOnMouseClicked(event -> {
            displayArticleImage.setVisible(false);
            displayArticleWebview.setVisible(true);
            engine.load(article.getUrl());
        });
        pane.setExpanded(false);
        recentArticles.add(pane);
    }
    // Show all articles
    public void showRecentReading() {
        recentArticles = FXCollections.observableArrayList();
        Stack<TableData> recentReadingList= new RecentReading().getRecentList();
        for (int i= recentReadingList.size()-1;i>=0;i--) {
            TableData article = recentReadingList.get(i);
            TextArea description = new TextArea(article.getDescription());
            description.setWrapText(true);
            description.setPrefSize(1.0,1.0);
            description.setEditable(false);
            Label title = new Label(article.getTitle());
            createPane(title, description,article);
        }
        homeRecentArticles.getItems().clear();
        homeRecentArticles.getItems().addAll(recentArticles);
        homeTodayReading.setText(String.valueOf(recentReadingList.size()));
        homeTotalArticles.setText(String.valueOf(dataList.size()));
    }

    // News Section
    public void chooseOptions(String[] options, ComboBox<String> comboBox, String promptText) {
        List<String> listData = new ArrayList<>();
        Collections.addAll(listData, options);
        ObservableList<String> list = FXCollections.observableArrayList(listData);
        comboBox.setItems(list);
        comboBox.setButtonCell(new PromptButtonCell<>(promptText));
    }

    public void initSortCategory() {
        chooseOptions(options, sortOption, "Latest");
        chooseOptions(years, sortYear, "Year");
        chooseOptions(tags, sortTag, "Tag");
    }

    public void clearAllOptions() {
        sortOption.getSelectionModel().clearSelection();
        sortYear.getSelectionModel().clearSelection();
        sortTag.getSelectionModel().clearSelection();
    }

    public void showListAfterSort() {
        FilteredList<TableData> filter = new FilteredList<>(dataList, e -> true);
        String year = sortYear.getValue();
        System.out.println(year);
        String tag = sortTag.getValue();
        System.out.println(tag);
        String option = sortOption.getValue();
        System.out.println(option);
        filter.setPredicate(tableData -> {
            if (year == null || year.equals("Year")) return true;
            else return tableData.getCreate_date().toLowerCase().contains(year.toLowerCase());
        });
        FilteredList<TableData> lastFilter = new FilteredList<>(filter);
        lastFilter.setPredicate(tableData -> {
            if (tag == null || tag.equals("Tag")) return true;
            else return tableData.getTag().toLowerCase().contains(tag.toLowerCase());
        });
        afterSortDataList = FXCollections.observableArrayList(lastFilter);
        Collections.sort(afterSortDataList);
        if (option != null && option.equalsIgnoreCase("oldest")) {
            Collections.reverse(afterSortDataList);
        }
        showAllArticles(afterSortDataList);
    }

    public Parent createPage(int pageIndex) {
        int startIndex = (pageIndex) * 10;
        ScrollPane pane = new ScrollPane();
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPrefWidth(832.0);
        vBox.setAlignment(Pos.CENTER);
        for (int i = startIndex; i < Math.min(startIndex + 10, afterSortDataList.size()); i++) {
            TableData article = afterSortDataList.get(i);
            VBox box = new VBox();
            box.getStyleClass().add("box");
            box.setPadding(new Insets(10, 10, 10, 10));
            box.setSpacing(2);
            box.setPrefWidth(790.0);
            ArticleLink title = new ArticleLink(article.getUrl(), article);
            title.setText(article.getTitle());
            title.setText(article.getTitle());
            title.getStyleClass().add("box-hyperlink");
            box.getChildren().add(title);
            Label description = new Label(article.getDescription());
            description.setWrapText(true);
            description.setMaxWidth(832.0);
            box.getChildren().add(description);
            Label tags = new Label("#" + article.getTag());
            tags.getStyleClass().add("label-tag");
            box.getChildren().add(tags);
            vBox.getChildren().add(box);
        }
        pane.setContent(vBox);
        return pane;
    }

    public void showAllArticles(ObservableList<TableData> list) {
        newsPages.setPageCount((int) Math.ceil(list.size() * 1.0 / articlesPerPage));
        newsPages.setPageFactory(this::createPage);
        afterSortDataList = dataList;
    }

    // Search Tab from here
    // Show results on the table and open a new window for the chosen articles
    public void showSearchResults() {
        searchTableAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        searchTableDate.setCellValueFactory(new PropertyValueFactory<>("create_date"));
        searchTableTags.setCellValueFactory(new PropertyValueFactory<>("tag"));
        searchTableTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        searchTableType.setCellValueFactory(new PropertyValueFactory<>("type"));
        searchTableLink.setCellValueFactory(new PropertyValueFactory<>("link"));
        searchTable.setItems(dataList);
        searchTable.setRowFactory(searchTable -> {
            TableRow<TableData> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (mouseEvent.getClickCount() == 2) {
                        Stack<TableData> recentReadingList= new RecentReading().getRecentList();
                        recentReadingList.push(row.getItem());
                        new RecentReading().deleteDuplicated(recentReadingList);
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("/newsaggregator/article-information.fxml"));
                            new DragAndDropWindow().displayStage(root, new Stage());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
            return row;
        });
        articleSearch();
    }

    // Search function
    public void articleSearch() {
        FilteredList<TableData> filter = new FilteredList<>(dataList, e -> true);
        searchBar.textProperty().addListener((observableValue, s, t1) -> filter.setPredicate(tableData -> {
            if (t1 == null && t1.isEmpty()) return true;
            String searchKey = t1.toLowerCase();
            if (tableData.getTitle().toLowerCase().contains(searchKey)) return true;
            else if (tableData.getAuthor().toLowerCase().contains(searchKey)) return true;
            else if (tableData.getContent().toLowerCase().contains(searchKey)) return true;
            else if (tableData.getTag().toLowerCase().contains(searchKey)) return true;
            else if (tableData.getDescription().toLowerCase().contains(searchKey)) return true;
            else if (tableData.getType().toLowerCase().contains(searchKey)) return true;
            else return tableData.getCreate_date().toLowerCase().contains(searchKey);
        }));

        SortedList<TableData> sortedList = new SortedList<>(filter);
        sortedList.comparatorProperty().bind(searchTable.comparatorProperty());
        searchTable.setItems(sortedList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText("");
        engine = displayArticleWebview.getEngine();
        initSortCategory();
        showSearchResults();
        showRecentReading();
        showAllArticles(dataList);
    }

}