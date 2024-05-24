package newsaggregator.controller.dashboard.creator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import newsaggregator.controller.dashboard.PromptButtonCell;
import newsaggregator.data.ArticleLink;
import newsaggregator.data.TableData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsSectionCreator implements Creator {
    private final String[] options = {"Latest", "Oldest"};
    private final String[] years = {"2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017"};
    private final String[] tags = {"Cryptocurrency", "Crypto", "Blockchain", "News","Bitcoin","Top Stories"};
    private final double articlesPerPage;
    private ComboBox<String> sortOption;
    private ComboBox<String> sortYear;
    private ComboBox<String> sortTag;
    private ObservableList<TableData> dataList;
    private Pagination newsPages;
    private ObservableList<TableData> afterSortDataList = dataList;

    public NewsSectionCreator(double articlesPerPage, ComboBox<String> sortOption, ComboBox<String> sortYear, ComboBox<String> sortTag, ObservableList<TableData> dataList, Pagination newsPages, ObservableList<TableData> afterSortDataList) {
        this.articlesPerPage = articlesPerPage;
        this.sortOption = sortOption;
        this.sortYear = sortYear;
        this.sortTag = sortTag;
        this.dataList = dataList;
        this.newsPages = newsPages;
        this.afterSortDataList = afterSortDataList;
    }

    private void chooseOptions(String[] options, ComboBox<String> comboBox, String promptText) {
        List<String> listData = new ArrayList<>();
        Collections.addAll(listData, options);
        ObservableList<String> list = FXCollections.observableArrayList(listData);
        comboBox.setItems(list);
        comboBox.setButtonCell(new PromptButtonCell<>(promptText));
    }
    private void initSortCategory() {
        chooseOptions(options, sortOption, "Latest");
        chooseOptions(years, sortYear, "Year");
        chooseOptions(tags, sortTag, "Tag");
    }
    public void showAllArticles(ObservableList<TableData> list) {
        newsPages.setPageCount((int) Math.ceil(list.size() * 1.0 / articlesPerPage));
        newsPages.setPageFactory(this::createPage);
        afterSortDataList = dataList;
    }
    private Parent createPage(int pageIndex) {
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
    public void sort() {
        FilteredList<TableData> filter = new FilteredList<>(dataList, e -> true);
        String year = sortYear.getValue();
        String tag = sortTag.getValue();
        String option = sortOption.getValue();
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
    @Override
    public void create() {
        initSortCategory();
        showAllArticles(dataList);
    }
}
