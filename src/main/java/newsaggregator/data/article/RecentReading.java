package newsaggregator.data.article;

import newsaggregator.data.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RecentReading {
    private static Stack<TableData> recentList;
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        RecentReading.user = user;
    }

    public static Stack<TableData> getRecentList() {
        return recentList;
    }

    public void deleteDuplicated(Stack<TableData> recentList) {
        for (int i = 0; i < recentList.size() - 1; i++) {
            if (recentList.get(i) == recentList.peek()) {
                recentList.remove(i);
                return;
            }
        }
    }

    public void getRecentReadingFromUser(User user) {
        Stack<TableData> recent = new Stack<>();
        List<TableData> list = user.getRecentReadings();
        for (TableData data : list) {
            recent.add(data);
        }
        recentList = recent;
        RecentReading.user = user;
    }

    public List<TableData> setRecentReadingFromUser() {
        List<TableData> list = new ArrayList<>();
        for (TableData article : recentList) {
            list.add(new TableData(article.getUrl(), article.getWeb_url(), article.getType(), article.getDescription(), article.getTitle(), article.getContent(), article.getCreate_date(), article.getTag(), article.getAuthor()));
        }
        return list;
    }
}
