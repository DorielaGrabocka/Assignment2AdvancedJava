/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import databaseConnection.EntityManagerProvider;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import models.Book;

/**
 *
 * @author Doriela
 */
public class BookDAO implements BaseDao<Book> {

    @Override
    public EntityManager getEntityManager() {
        return EntityManagerProvider.getEntityManager();
    }

    @Override
    public void insert(Book b) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction transcation = em.getTransaction();

        transcation.begin();
        em.persist(b);
        transcation.commit();
    }

    @Override
    public void delete(Book b) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction transcation = em.getTransaction();
        boolean exists = bookExists(b.getTitle(), b.getPublicationYear()) != null;
        if (!exists) {
            transcation.begin();
            em.remove(b);
            transcation.commit();
        }
    }

    public void update(Book updatedBook) throws Exception {
        EntityManager em = getEntityManager();
        Book original = em.find(Book.class, updatedBook.getId());
        updateBook(original, updatedBook);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.merge(original);
        transaction.commit();
    }

    /**
     * Method that gets the book with the original values and the book with new
     * values It updates the updatable parameters(title, author, genre,
     * publication year, publishing house, synopsis, status) of the book.
     *
     * @param original - the book with unchanged data
     * @param updated -book with new parameters.
     */
    private void updateBook(Book original, Book updated) {
        original.setTitle(updated.getTitle());
        original.setAuthor(updated.getAuthor());
        original.setGenre(updated.getGenre());
        original.setPublicationYear(updated.getPublicationYear());
        original.setPublishingHouse(updated.getPublishingHouse());
        original.setSynopsis(updated.getSynopsis());
        original.setStatus(updated.getStatus());
    }

    /**
     * Method to get a book by the id.
     *
     * @param id- is the primary key of the book on the table in the database.
     * @return the corresponding book.
     */
    public Book getById(int id) {
        return getEntityManager().find(Book.class, id);
    }

    @Override
    public List<Book> getAll() {
        return getEntityManager().createNamedQuery("Book.findAll")
                .getResultList();
    }

    /**
     * Method to get the last five added books to the database
     *
     * @return the list of books
     */
    public List<Book> getLastFive() {
        String query = "SELECT b FROM Book b WHERE b.status!='D' ORDER BY b.dateAdded DESC";
        return getEntityManager().createQuery(query, Book.class)
                .setMaxResults(5)
                .getResultList();
    }

    /**
     * Method to get the top five rated books.
     *
     * @return the list of books
     */
    public List<Book> getTopFive() {
        String query = "SELECT b "
                + "FROM Book b JOIN Review r on b.id=r.reviewPK.bookID "
                + "WHERE b.status!='D' "
                + "GROUP By b.id "
                + "ORDER BY AVG(r.rating) DESC ";

        return getEntityManager().createQuery(query, Book.class)
                .setMaxResults(5)
                .getResultList();
    }

    /**
     * Method to get the average rating of a book
     *
     * @param id - is the primary key of the book in the table
     * @return the average rating of the book or 0 if no rating exists
     */
    public double getAverageRating(int id) {
        String query = "SELECT r.rating FROM Review r WHERE r.reviewPK.bookID = :bookID";

        return getEntityManager().createQuery(query, Integer.class)
                .setParameter("bookID", id)
                .getResultList()
                .stream()
                .mapToInt(r -> r)
                .average()
                .orElse(0);
    }

    /**Method to check if a book exists or not, provided the title and publication year.
     * @param title - is the title of the book we are searching for
     * @param publicationYear - is the publication year of the book.
     * @return the book if found or null.
     */
    public Book bookExists(String title, String publicationYear) {
        String query = "SELECT b FROM Book b WHERE b.title=:title "
                + "AND b.publicationYear=:publicationYear";
        try {
            Book foundBook = getEntityManager().createQuery(query, Book.class)
                    .setParameter("title", title)
                    .setParameter("publicationYear", publicationYear)
                    .getSingleResult();

            return foundBook;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Method to get books that have average rating greater than specified value
     *
     * @param averageRating is the lower bound of the rating that we are
     * searching for
     * @return the list of books
     */
    public List<Book> getAllBookWithAverageRatingGreaterThan(int averageRating) {
        String query = "SELECT b "
                + "FROM Book b join Review r ON b.id=r.reviewPK.bookID "
                + "WHERE b.status!='D'"
                + "GROUP BY r.reviewPK.bookID "
                + "HAVING avg(r.rating)>:value";

        return getEntityManager().createQuery(query)
                .setParameter("value", averageRating)
                .getResultList();
    }

    /**Method to filter books based on a number of attributes like author, title,
     * minimum and maximum average ratings and genre. Used by the web service as well.
     * @param title - the title of the book
     * @param author - the author of the book
     * @param min - the minimum average
     * @param max - the maximum average
     * @param genre - the genre of the book
     * @return the list of books that comply with the specified criteria.
     */
    public List<Book> filterBooks(String title, String author,
            int min, int max, String genre){
        List<Book> filteredBooks = 
                filterBooksByAverageRatingRange(min, max);
        if(isValueSet(title))
            filteredBooks = filteredBooks.stream()
                    .filter(b->b.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .collect(Collectors.toList());
        if(isValueSet(author))
            filteredBooks = filteredBooks.stream()
                    .filter(b->b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                    .collect(Collectors.toList());
        if(isValueSet(genre))
            filteredBooks = filteredBooks.stream()
                    .filter(b->b.getGenre().toLowerCase().contains(genre.toLowerCase()))
                    .collect(Collectors.toList());
        
        return filteredBooks;
    
    }
        
    /**Method to check if the value has been provided by the user or not.
     * @param value is the value to be checked.
     * @return the condition fulfillment.
     */
    public boolean isValueSet(String value){
        return value!=null && !value.equals("");
    }
    
    /**Method to filter boos by range of average rating.
     * @param minAverageRating - is the minimum average rating
     * @param maxAverageRating - is the maximum average rating
     * @return the list of books that comply with the specified criteria.
     */
    private List<Book> filterBooksByAverageRatingRange(int minAverageRating, int maxAverageRating) {
        BookDAO bookDAO = new BookDAO();
        List<Book> listOfBooks = bookDAO.getAll();
        if(minAverageRating!=0 && maxAverageRating!=0){
            return listOfBooks.stream()
                    .filter(b->bookDAO.getAverageRating(b.getId())>=minAverageRating)
                    .filter(b->bookDAO.getAverageRating(b.getId())<=maxAverageRating)
                    .collect(Collectors.toList());
        }
        else if(minAverageRating!=0){
            return listOfBooks.stream()
                    .filter(b->bookDAO.getAverageRating(b.getId())>=minAverageRating)
                    .collect(Collectors.toList());
        }
        else if(maxAverageRating!=0){
            return listOfBooks.stream()
                    .filter(b->bookDAO.getAverageRating(b.getId())<=maxAverageRating)
                    .filter(b->bookDAO.getAverageRating(b.getId())>0)
                    .collect(Collectors.toList());
        }
        return listOfBooks;
    }
    

}
