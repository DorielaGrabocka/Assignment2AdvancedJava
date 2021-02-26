
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import DAO.BookDAO;
import DAO.ReviewDAO;
import DAO.UserDAO;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import models.Book;
import models.Review;
import models.ReviewPK;
import models.User;

/**
 *
 * @author Oli
 */
@ManagedBean(name="bookDetailsBean")
@ViewScoped
public class BookDetailsBean implements Serializable{
     
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;
    
    @ManagedProperty(value="#{bufferSessionBean}")
    private BufferSessionBean bufferSessionBean;
    
    private int bookID;
    private Book book;
    private final BookDAO bookDao = new BookDAO();
    private final ReviewDAO reviewDao = new ReviewDAO();
    private final UserDAO userDao = new UserDAO();
    private double averageRating;
    private String outputText = "";
    private String comment;
    private int rating;

    public BufferSessionBean getBufferSessionBean() {
        return bufferSessionBean;
    }

    public void setBufferSessionBean(BufferSessionBean bufferSessionBean) {
        this.bufferSessionBean = bufferSessionBean;
    }
    
    public BookDetailsBean(){
    }
    
    public void init(){
        bookID = bufferSessionBean.getBookIDFromIndexToDetails();
        book = bookDao.getById(bookID);
        averageRating = bookDao.getAverageRating(bookID);
    }
    
    public List<Review> getReviews(){
       return reviewDao.getBookReviews(bookID);
    }
    
    public String getFullName(int userId){
        User user = userDao.getById(userId);
        return user.getName() + " " + user.getSurname();
    }
    
    public void removeReview(int userID, int bookID) throws Exception{
        if(loginBean.getUser()==null){//user not logged in
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("login.xhtml?faces-redirect=true");
        }
        else{//user logged in
            Review review = reviewDao.getByIds(userID, bookID);
            reviewDao.delete(review);
            outputText="Review successfully removed!";
        }
        
        averageRating = bookDao.getAverageRating(bookID);
   }
    
    public void addReview() throws Exception{
        
        if(loginBean.getUser()==null){//user not logged in
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("login.xhtml?faces-redirect=true");
        }
        else{//user logged in
            boolean exists = reviewDao.reviewExists(loginBean.getUser().getId(), bookID);
            if (!exists) {
                ReviewPK reviewPK = new ReviewPK(loginBean.getUser().getId(), bookID);
                Review review = new Review(reviewPK, rating, comment);
                reviewDao.insert(review);
                outputText = "Review added succesfully!";
                averageRating = bookDao.getAverageRating(bookID);
            } else {
                outputText = "Review already exists!";
            }
        }
        
    }
    
    public String goBack(){
        if(loginBean.getUser()==null) return "indexSimple?faces-redirect=true";
        return "indexStandard?faces-redirect=true";
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
        outputText+=rating+"XXXX";
    }
    
    public User getCurrentUser() {
        return loginBean.getUser();
    }
    
    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookId) {
        this.bookID = bookId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageReview) {
        this.averageRating = averageReview;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getOutputText() {
        return outputText;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}