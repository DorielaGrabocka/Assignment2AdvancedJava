/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Doriela
 */
@Entity
@Table(name = "books")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Book.findAll", query = "SELECT b FROM Book b WHERE b.status!='D'")
    , @NamedQuery(name = "Book.findById", query = "SELECT b FROM Book b WHERE b.id = :id AND b.status!='D'")
    , @NamedQuery(name = "Book.findByTitle", query = "SELECT b FROM Book b WHERE b.title = :title AND b.status!='D'")
    , @NamedQuery(name = "Book.findByAuthor", query = "SELECT b FROM Book b WHERE b.author = :author AND b.status!='D'")
    , @NamedQuery(name = "Book.findByPublishingHouse", query = "SELECT b FROM Book b WHERE b.publishingHouse = :publishingHouse AND b.status!='D'")
    , @NamedQuery(name = "Book.findByPublicationYear", query = "SELECT b FROM Book b WHERE b.publicationYear = :publicationYear AND b.status!='D'")
    , @NamedQuery(name = "Book.findByGenre", query = "SELECT b FROM Book b WHERE b.genre = :genre AND b.status!='D'")
    , @NamedQuery(name = "Book.findByDateAdded", query = "SELECT b FROM Book b WHERE b.dateAdded = :dateAdded AND b.status!='D'")
    , @NamedQuery(name = "Book.findBySynopopsis", query = "SELECT b FROM Book b WHERE b.synopsis = :synopopsis AND b.status!='D'")
    , @NamedQuery(name = "Book.findByStatus", query = "SELECT b FROM Book b WHERE b.status = :status AND b.status!='D'")})
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Title")
    private String title;
    @Basic(optional = false)
    @Column(name = "Author")
    private String author;
    @Basic(optional = false)
    @Column(name = "PublishingHouse")
    private String publishingHouse;
    @Basic(optional = false)
    @Column(name = "Publication_Year")
    private String publicationYear;
    @Basic(optional = false)
    @Column(name = "Genre")
    private String genre;
    @Basic(optional = false)
    @Column(name = "Date_Added")
    @Temporal(TemporalType.DATE)
    private Date dateAdded;
    @Column(name = "Synopsis")
    private String synopsis;
    @Basic(optional = false)
    @Column(name = "Status")
    private String status;
    
    
    public Book() {
    }

    public Book(Integer id) {
        this.id = id;
    }

    public Book(String title, String author, String publishingHouse, 
            String publicationYear, String genre, Date dateAdded, String synopsis, String status) {
        this.title = title;
        this.author = author;
        this.publishingHouse = publishingHouse;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.dateAdded = dateAdded;
        this.synopsis = synopsis;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getDateAdded() {
        return new SimpleDateFormat("dd-MM-yyyy")
                .format(dateAdded);
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }


    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopopsis) {
        this.synopsis = synopopsis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Book[ id=" + id + " ]";
    }
    
}
