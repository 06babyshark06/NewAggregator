package newsaggregator.data.article;

import javafx.collections.ObservableList;

public interface DataGetter {
    ObservableList<TableData> getData(String file);
}
