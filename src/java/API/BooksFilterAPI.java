/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import DAO.BookDAO;
import java.util.List;
import java.util.stream.Collectors;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import models.Book;

/**
 *
 * @author Doriela
 */
@WebService(serviceName = "BooksFilterAPI")
public class BooksFilterAPI {

    BookDAO bookDAO = new BookDAO();
     @WebMethod(operationName = "filterBooks")
    public List<BookData> filterBooks(@WebParam(name="title")String title,
            @WebParam(name="author") String author,
            @WebParam(name="genre")String genre,
            @WebParam(name="minAverageRating")int minAverageRating, 
            @WebParam(name="maxAverageRating")int maxAverageRating)
    {
        
        if(minAverageRating==0 && maxAverageRating==0 && !bookDAO.isValueSet(title) && !bookDAO.isValueSet(author)
                && !bookDAO.isValueSet(genre))//nothing is selected
            return null;
        
        return bookDAO.filterBooks(title, author, maxAverageRating, 
                maxAverageRating, genre)
                .stream()
                .map(this::toBookData)
                .collect(Collectors.toList());
    }
    
    /**Method to convert a book object to a BookData object.
     *@param b - is the book that we will convert
     *@return a BookData object that will contain only the neccessary information 
     */
    private BookData toBookData(Book b){
        double rating = bookDAO.getAverageRating(b.getId());
        BookData bookData = new BookData();
        bookData.setId(b.getId());
        bookData.setTitle(b.getTitle());
        bookData.setAuthor(b.getAuthor());
        bookData.setBookRating(rating);
        return bookData;
    }
}
