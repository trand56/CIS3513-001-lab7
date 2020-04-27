package temple.edu.bookshelf;

import java.io.Serializable;

public class Book implements Serializable {
    private int id;
    private String title;
    private String author;
    private String coverURL;
    private int duration;


    public Book(int id, String title, String author, String coverurl, int duration){
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverURL = coverurl;
        this.duration = duration;
    }

    public int getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getAuthor(){
        return this.author;
    }

    public String getCoverURL(){
        return this.coverURL;
    }

    public int getDuration(){ return this.duration; }

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

    public void setDuration(int duration){ this.duration = duration; }
}
