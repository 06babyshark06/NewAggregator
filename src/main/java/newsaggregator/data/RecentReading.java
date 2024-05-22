package newsaggregator.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RecentReading {
    private static Stack<TableData> recentList = new Stack<>();
    private static User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        RecentReading.user = user;
    }

    public Stack<TableData> getRecentList() {
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
        List<ArticleData> list = user.getRecentReadings();
        for (ArticleData data : list) {
            TableData tableData = new TableData(data);
            recent.add(tableData);
        }
        recentList = recent;
        RecentReading.user = user;
    }

    public List<ArticleData> setRecentReadingFromUser() {
        List<ArticleData> list = new ArrayList<>();
        for (ArticleData article : recentList) {
            list.add(new ArticleData(article.getUrl(), article.getWeb_url(), article.getType(), article.getDescription(), article.getTitle(), article.getContent(), article.getCreate_date(), article.getTag(), article.getAuthor()));
        }
        return list;
    }
}
