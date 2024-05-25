package newsaggregator.controller.dashboard.creator;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import newsaggregator.data.article.ArticleLink;
import newsaggregator.data.article.RecentReading;
import newsaggregator.data.article.TableData;
import newsaggregator.display.DragAndDropWindow;
import newsaggregator.notification.ErrorNotification;

import java.io.IOException;
import java.util.Stack;

public class SearchSectionCreator implements Creator {
    private ObservableList<TableData> dataList;
    private TextField searchBar;
    private TableView<TableData> searchTable;
    private TableColumn<TableData, String> searchTableAuthor;
    private TableColumn<TableData, String> searchTableDate;
    private TableColumn<TableData, String> searchTableTags;
    private TableColumn<TableData, String> searchTableTitle;
    private TableColumn<TableData, String> searchTableType;
    private TableColumn<TableData, ArticleLink> searchTableLink;

    public SearchSectionCreator(ObservableList<TableData> dataList, TextField searchBar, TableView<TableData> searchTable, TableColumn<TableData, String> searchTableAuthor, TableColumn<TableData, String> searchTableDate, TableColumn<TableData, String> searchTableTags, TableColumn<TableData, String> searchTableTitle, TableColumn<TableData, String> searchTableType, TableColumn<TableData, ArticleLink> searchTableLink) {
        this.dataList = dataList;
        this.searchBar = searchBar;
        this.searchTable = searchTable;
        this.searchTableAuthor = searchTableAuthor;
        this.searchTableDate = searchTableDate;
        this.searchTableTags = searchTableTags;
        this.searchTableTitle = searchTableTitle;
        this.searchTableType = searchTableType;
        this.searchTableLink = searchTableLink;
    }

    @SuppressWarnings("null")
    private void articleSearch() {
        FilteredList<TableData> filter = new FilteredList<>(dataList, e -> true);
        searchBar.textProperty().addListener((observableValue, oldValue, newValue) -> filter.setPredicate(tableData -> {
            if (newValue == null && newValue.isEmpty()) return true;
            String searchKey = newValue.toLowerCase();
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
    public void create() {
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
                        Stack<TableData> recentReadingList= RecentReading.getRecentList();
                        recentReadingList.push(row.getItem());
                        new RecentReading().deleteDuplicated(recentReadingList);
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("/newsaggregator/article-information.fxml"));
                            new DragAndDropWindow().displayStage(root, new Stage());
                        } catch (IOException e) {
                            new ErrorNotification().showMessage("Could not load article-information.fxml or something went wrong");
                        }

                    }
                }
            });
            return row;
        });
        articleSearch();
    }
}
