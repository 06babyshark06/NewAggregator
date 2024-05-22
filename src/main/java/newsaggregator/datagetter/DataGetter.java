package newsaggregator.datagetter;

import javafx.collections.ObservableList;
import newsaggregator.data.TableData;

public interface DataGetter {
    ObservableList<TableData> getData(String file);
}
