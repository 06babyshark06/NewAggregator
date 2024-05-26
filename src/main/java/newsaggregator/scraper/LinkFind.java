package newsaggregator.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class LinkFind {
	private String homePageURL;
    private String outputFilePath = "src/main/resources/newsaggregator/url.txt";
	public LinkFind(String homePageURL) {
		this.homePageURL = homePageURL;
	}
    
    public void saveURLsToFile() throws IOException {
        // Tạo kết nối viết với file Url.txt 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (String Url : findURLs(homePageURL)) {
                writer.write(Url);
                writer.newLine(); // Xuống dòng
            }
            writer.close();
        }

    }

    private Set<String> findURLs(String homepageURL) throws IOException {
        Set<String> URLs = new HashSet<>();
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        // Đẩy vào homepageURL
        stack.push(homepageURL);

        // Tìm kiếm Url bằng DFS
        while (!stack.isEmpty() && URLs.size() <= 100) {
            // Lấy Url đầu trong stack làm Url mục tiêu
            String currentURL = stack.pop();

            // Kiểm tra Url mục tiêu đã xét chưa
            if (!visited.contains(currentURL)) {
                visited.add(currentURL);

                // Giới hạn route của Url (<<<<<<<<<<<< Mày sửa chỗ này >>>>>>>>>>>>>>)
                if (currentURL.equals(this.homePageURL) || (currentURL.startsWith(this.homePageURL) && currentURL.length() >= 60)) { 
                	if (currentURL!=homepageURL){
                        URLs.add(currentURL); // Add the URL to the set of blockchain URLs
                    }
                    // Tạo kết nối HTTP đến Url mục tiêu
                    URL url = new URL(currentURL);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    int code = connection.getResponseCode();
                    if (code>299) continue;
                    else{ 
                        Document doc = Jsoup.connect(currentURL).get();

                        // Lấy các phần tử chứa tag <a href="">
                        Elements links = doc.select("a[href]");
    
                        for (Element link : links) {
                            // Kiểm tra điều kiện của Url (<<<<<<<<<<<< Mày thêm bộ lọc chỗ này >>>>>>>>>>>>>>)
                            // Lấy Root Url của phần tử
                            String absUrl = link.absUrl("href");
                           if (absUrl.contains("/page")||absUrl.contains("/privacy")||absUrl.contains("/contact")
                           ||absUrl.contains("/about")||absUrl.contains("/cookie")||absUrl.contains("/tag")
                           ||absUrl.contains("/auth")||absUrl.contains("/adv")||absUrl.contains("/accessibility")
                           ||absUrl.contains("/policy")||absUrl.contains("#")) continue;
                            // Đẩy Root Url vào stack
                           else stack.push(absUrl);
                        }
                    }
                }
            }
        }

        return URLs;
    }
}
