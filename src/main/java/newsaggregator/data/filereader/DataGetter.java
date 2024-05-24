package newsaggregator.data.filereader;

import javafx.collections.ObservableList;
import newsaggregator.data.TableData;

public interface DataGetter {
    ObservableList<TableData> getData(String file);
}
