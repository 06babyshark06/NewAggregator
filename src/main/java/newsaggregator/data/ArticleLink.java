package newsaggregator.data;

import javafx.scene.control.Hyperlink;
import newsaggregator.notification.ErrorNotification;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Stack;

public class ArticleLink extends Hyperlink {

    public ArticleLink(String s, TableData tableData) {
        super("Here");
        this.setOnAction(actionEvent -> {
            try {
                Stack<TableData> list = new RecentReading().getRecentList();
                Desktop.getDesktop().browse(new URI(s));
                list.add(tableData);
                new RecentReading().deleteDuplicated(list);
            } catch (IOException | URISyntaxException e) {
                new ErrorNotification().showMessage("Url may be invalid or something went wrong with your internet connection, browser");
            } catch (RuntimeException e) {
                new ErrorNotification().showMessage("Something went wrong");
            }
        });
    }
}
