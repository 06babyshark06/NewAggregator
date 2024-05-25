package newsaggregator.data.user;

import newsaggregator.data.article.TableData;

import java.util.List;

public class User {
    private String username;
    private String password;
    private List<TableData> recentReadings;

    public User() {
    }

    public User(String username, String password, List<TableData> recentReadings) {
        this.username = username;
        this.password = password;
        this.recentReadings = recentReadings;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TableData> getRecentReadings() {
        return recentReadings;
    }

    public void setRecentReadings(List<TableData> recentReadings) {
        this.recentReadings = recentReadings;
    }
}
