/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import databaseConnection.EntityManagerProvider;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import models.Review;
import models.ReviewPK;

/**
 *
 * @author Doriela
 */
public class ReviewDAO implements BaseDao<Review>{
 
    @Override
    public EntityManager getEntityManager() {
        return EntityManagerProvider.getEntityManager();
    }

    @Override
    public void insert(Review r) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(r);
        transaction.commit();
    }

    @Override
    public void delete(Review r) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        if(!em.contains(r)){
            r = em.merge(r);
        }
        em.remove(r);
        transaction.commit();
    }
    
    public void removeReview(ReviewPK reviewPK){
        String query ="DELETE FROM Review r WHERE r.reviewPK=:reviewPK";
        getEntityManager().createQuery(query)
                .setParameter("reviewPK", reviewPK);
    }

    public Review getByIds(int userId, int bookID) {
        String query = "SELECT r FROM Review r "
                + "WHERE r.reviewPK.userID=:userID AND r.reviewPK.bookID=:bookID";
        return getEntityManager().createQuery(query, Review.class)
                .setParameter("userID", userId)
                .setParameter("bookID", bookID)
                .getSingleResult();
    }
    
    public Review getById(ReviewPK reviewPK){
        String query = "SELECT r FROM Review r WHERE r.reviewPK=:reviewPK";
        return getEntityManager().createQuery(query, Review.class)
                .setParameter("reviewPK", reviewPK)
                .getSingleResult();
    }

    @Override
    public List<Review> getAll() {
        return getEntityManager().createNamedQuery("Review.findAll")
                .getResultList();
    }
    
    public List<Review> getBookReviews(int id){
        String query = "SELECT r "
                + "FROM Review r WHERE r.reviewPK.bookID=:bookId";
        
        return getEntityManager().createQuery(query, Review.class)
                .setParameter("bookId", id)
                .getResultList();
    }
    
    public List<Review> getUserReviews(int id){
        String query = "SELECT r "
                + "FROM Review r WHERE r.reviewPK.userID=:userId";
        
        return getEntityManager().createQuery(query, Review.class)
                .setParameter("userId", id)
                .getResultList();
    }
    
    public boolean reviewExists(int userID, int bookID) {
        String query = "SELECT r FROM Review r "
                + "WHERE r.reviewPK.userID=:userID AND r.reviewPK.bookID=:bookID";
        try {
            Review foundReview = getEntityManager().createQuery(query, Review.class)
                    .setParameter("userID", userID)
                    .setParameter("bookID", bookID)
                    .getSingleResult();

            return foundReview != null;
        } catch (NoResultException e) {
            return false;
        }
    }
    
}
