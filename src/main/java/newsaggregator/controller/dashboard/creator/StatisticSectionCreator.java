package newsaggregator.controller.dashboard.creator;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import newsaggregator.controller.dashboard.trend.TrendFinder;
import newsaggregator.controller.dashboard.trend.pairdata.Pair;
import newsaggregator.controller.dashboard.trend.pairdata.PairArray;
import newsaggregator.notification.ErrorNotification;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class StatisticSectionCreator implements Creator{
    private Label statisticTag;
    private ScrollPane statisticSources;
    private LineChart<?, ?> statisticTrend;
    private TextField statisticSearch;
    private TrendFinder finder;

    public StatisticSectionCreator(Label statisticTag, ScrollPane statisticSources, LineChart<?, ?> statisticTrend, TextField statisticSearch, TrendFinder finder) {
        this.statisticTag = statisticTag;
        this.statisticSources = statisticSources;
        this.statisticTrend = statisticTrend;
        this.statisticSearch = statisticSearch;
        this.finder = finder;
    }

    private void showMostPopular() {
        try {
            statisticTag.setText(statisticTag.getText() + finder.findMostTrending(5).propertyList());
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorNotification().showMessage("Something went wrong");
        }
    }

    private void showSources() {
        try {
            PairArray list = finder.extractedWeb();
            VBox box = new VBox();
            for (Pair pair : list) {
                box.getChildren().add(new Label(pair.toString()));
            }
            statisticSources.setContent(box);
        } catch (IOException | ParseException e) {
            new ErrorNotification().showMessage("Something went wrong");
        }
    }
    public void showTrend(String keyword) {
        if (keyword.isEmpty()) return;
        XYChart.Series series = new XYChart.Series();
        series.setName(keyword);
        PairArray list=finder.trendOverTime(keyword,3);
        statisticTrend.setTitle("Trend of "+keyword);
        for (int i=list.size()-1; i>=0; i--) {
            series.getData().add(new XYChart.Data(list.get(i).getProperty(),list.get(i).getValue()));
        }
        statisticTrend.getData().clear();
        statisticTrend.getData().add(series);
    }
    @Override
    public void create() {
        showMostPopular();
        showSources();
        showTrend("Bitcoin");
    }
}
