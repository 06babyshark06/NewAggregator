package newsaggregator.datagetter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import newsaggregator.notification.ErrorNotification;
import newsaggregator.data.ArticleData;
import newsaggregator.data.TableData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JsonDataGetter implements DataGetter {
    @Override
    public ObservableList<TableData> getData(String file) {
        ObservableList<TableData> listTableData = FXCollections.observableArrayList();
        try {
            Object obj = new JSONParser().parse(new FileReader(file));
            JSONArray ja = (JSONArray) obj;
            for (Object o : ja) {
                JSONObject json_object = (JSONObject) o;
                String url = (String) json_object.get("url");
                String web_url = (String) json_object.get("web_url");
                String type = (String) json_object.get("type");
                String description = (String) json_object.get("description");
                String title = (String) json_object.get("title");
                String content = (String) json_object.get("content");
                String create_date = (String) json_object.get("create_date");
                String tag = (String) json_object.get("tag");
                String author = (String) json_object.get("author");

                description = description.replace("'", "’").replace("&", "and");
                title = title.replace("'", "’").replace("&", "and");
                content = content.replace("'", "’").replace("&", "and");

                ArticleData data = new ArticleData(url, web_url, type, description, title, content, create_date, tag, author);
                TableData tableData = new TableData(data);
                listTableData.add(tableData);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            new ErrorNotification().showMessage("Could not find data file or something went wrong");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            new ErrorNotification().showMessage("Something went wrong when reading data file");
        }
        return listTableData;
    }
}
