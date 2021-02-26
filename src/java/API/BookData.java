/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

/**
 *
 * @author Doriela
 */
public class BookData {
    private int id;//the book id
    private String title;//book's title
    private String author; //book's author
    private double bookRating; //book's rating

    public BookData() {
    }

    public BookData(int id, String title, String author, double bookRating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.bookRating = bookRating;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getBookRating() {
        return bookRating;
    }

    public void setBookRating(double bookRating) {
        this.bookRating = bookRating;
    }
    
    
}
