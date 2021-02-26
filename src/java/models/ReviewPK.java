/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import DAO.ReviewDAO;
import DAO.UserDAO;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Doriela
 */
@Embeddable
public class ReviewPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "UserID")
    private int userID;
    @Basic(optional = false)
    @Column(name = "BookID")
    private int bookID;

    public ReviewPK() {
    }

    public ReviewPK(int userID, int bookID) {
        this.userID = userID;
        this.bookID = bookID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userID;
        hash += (int) bookID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReviewPK)) {
            return false;
        }
        ReviewPK other = (ReviewPK) object;
        if (this.userID != other.userID) {
            return false;
        }
        if (this.bookID != other.bookID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.ReviewPK[ userID=" + userID + ", bookID=" + bookID + " ]";
    }
    
}
