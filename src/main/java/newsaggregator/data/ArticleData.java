package newsaggregator.data;

public class ArticleData {
    private String url;
    private String web_url;
    private String type;
    private String description;
    private String title;
    private String content;
    private String create_date;
    private String tag;
    private String author;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAuthor() {
        return author;
    }

    public ArticleData(String url, String web_url, String type, String description, String title, String content, String create_date, String tag, String author) {
        this.url = url;
        this.web_url = web_url;
        this.type = type;
        this.description = description;
        this.title = title;
        this.content = content;
        this.create_date = create_date;
        this.tag = tag;
        this.author = author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Data{" +
                "url='" + url + '\'' +
                ", web_url='" + web_url + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", content=" + content +
                ", create_date='" + create_date + '\'' +
                ", tag='" + tag + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
