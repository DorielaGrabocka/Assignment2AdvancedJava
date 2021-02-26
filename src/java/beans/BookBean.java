/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import DAO.BookDAO;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import models.Book;

/**
 *
 * @author Oli
 */
@ManagedBean(name = "bookBean")
@ViewScoped
public class BookBean {

    private int id;
    private String title;
    private String author;
    private String publishingHouse;
    private String publicationYear;
    private String genre;
    private Date dateAdded;
    private String synopsis;
    private String status;
    private BookDAO bookDao = new BookDAO();
    private String outputMessage;
    private Book updatedBook = new Book();
    private String deleteMessage;
    private boolean editing;
    private List<Book> listOfBooks;

    //For searching
    private String searchTitle;
    private String searchAuthor;
    private String searchMax;
    private String searchMin;
    private String searchGenre;
    private String searchMessage;

    public BookBean() {
        listOfBooks = new ArrayList<>();
        getAll();
    }

    public void upsert() {
        try {
            if (editing) {//we are editing
                //message="Inside editing";
                populateModel(updatedBook);
                bookDao.update(updatedBook);
                outputMessage = "Book updated succesfully!";
                clear();
            } else {//we are inserting
                Book potentialBook = bookDao.bookExists(title, publicationYear);
                if (potentialBook == null) {//check if title and year match
                    long millis = System.currentTimeMillis();
                    dateAdded = new Date(millis);
                    status = "N";
                    Book book = new Book(title, author, publishingHouse,
                            publicationYear, genre, dateAdded, synopsis, status);
                    bookDao.insert(book);
                    outputMessage = "Book added succesfully! Check books' table.";
                } else if (potentialBook.getStatus().equals("N")) {//book found in the db-not lazy deleted
                    outputMessage = "Book already exists!";
                } else {//book found in DB and lazy deleted
                    potentialBook.setStatus("N");
                    bookDao.update(potentialBook);//change the status of the book
                    outputMessage = "Book added succesfully! Check books' table.";
                }
            }
        } catch (Exception e) {
            outputMessage = "An error has occured! Book might already exist!";
        }
        getAll();
        deleteMessage = "";
    }

    public void getAll() {
        listOfBooks = bookDao.getAll()
                .stream()
                .filter(b -> (b.getStatus().equals("N")))
                .collect(Collectors.toList());

    }

    public void fillData() throws IOException, ParseException {
        clear();
        editing = true;
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        this.id = Integer.parseInt(params.get("bookId"));
        updatedBook = bookDao.getById(id);
        populateFields(updatedBook);
    }

    /**
     * Utility method to clear the view after an insert or delete.
     */
    public void clear() {
        title = "";
        author = "";
        publishingHouse = "";
        publicationYear = "";
        genre = "";
        synopsis = "";
        deleteMessage = "";
        outputMessage = "";
        editing = false;
    }

    public void populateModel(Book book) {
        book.setId(this.id);
        book.setTitle(this.title);
        book.setAuthor(this.author);
        book.setGenre(this.genre);
        book.setPublishingHouse(this.publishingHouse);
        book.setPublicationYear(this.publicationYear);
        book.setSynopsis(this.synopsis);
        book.setStatus("N");
    }

    public void populateFields(Book book) throws ParseException {
        title = book.getTitle();
        author = book.getAuthor();
        publishingHouse = book.getPublishingHouse();
        publicationYear = book.getPublicationYear();
        genre = book.getGenre();
        synopsis = book.getSynopsis();
    }

    public void delete() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        this.id = Integer.parseInt(params.get("bookId"));
        try {
            Book book = bookDao.getById(id);
            book.setStatus("D");
            bookDao.update(book);
            deleteMessage = "Book deleted succesfully!";
        } catch (Exception e) {
            deleteMessage = "Error: Book could not be deleted!";
        }
        getAll();//set the list of Books
        outputMessage = "";
    }

    public void searchBooks() throws ParseException {
        int min;
        int max;
        if (searchMin.equals("")) {
            min = 0;
        } else {
            min = Integer.parseInt(searchMin);
        }
        if (searchMax.equals("")) {
            max = 0;
        } else {
            max = Integer.parseInt(searchMax);
        }
        
        if(min > max){
            searchMessage = "Please put minimum less than maximum!";
        }
                
        listOfBooks = bookDao.filterBooks(searchTitle, searchAuthor,
                min, max, searchGenre)
                .stream()
                .collect(Collectors.toList());
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

    public void setAuthor(String Author) {
        this.author = Author;
    }

    public String getPublishingHouse() {
        return publishingHouse;
    }

    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public void setOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeleteMessage() {
        return deleteMessage;
    }

    public void setDeleteMessage(String deleteMessage) {
        this.deleteMessage = deleteMessage;
    }

    public List<Book> getListOfBooks() {
        return listOfBooks;
    }

    public void setListOfBooks(List<Book> listOfBooks) {
        this.listOfBooks = listOfBooks;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public String getSearchAuthor() {
        return searchAuthor;
    }

    public void setSearchAuthor(String searchAuthor) {
        this.searchAuthor = searchAuthor;
    }

    public String getSearchMax() {
        return searchMax;
    }

    public void setSearchMax(String searchMax) {
        this.searchMax = searchMax;
    }

    public String getSearchMin() {
        return searchMin;
    }

    public void setSearchMin(String searchMin) {
        this.searchMin = searchMin;
    }

    public String getSearchGenre() {
        return searchGenre;
    }

    public void setSearchGenre(String searchGenre) {
        this.searchGenre = searchGenre;
    }

    public String getSearchMessage() {
        return searchMessage;
    }

    public void setSearchMessage(String searchMessage) {
        this.searchMessage = searchMessage;
    }

}
