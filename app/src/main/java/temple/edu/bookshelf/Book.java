package temple.edu.bookshelf;

public class Book {
    private int id;
    private String title;
    private String author;
    private String coverURL;

    public Book(int id, String title, String author, String coverurl){
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverURL = coverurl;
    }

    public int getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String author(){
        return this.author;
    }

    public String getCoverURL(){
        return this.coverURL;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setCoverURL(String coverUrl){
        this.coverURL = coverUrl;
    }
}
