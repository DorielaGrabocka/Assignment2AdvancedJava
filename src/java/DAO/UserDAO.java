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
import javax.persistence.TypedQuery;
import models.User;

/**
 *
 * @author Doriela
 */
public class UserDAO implements BaseDao<User> {

    @Override
    public void insert(User user) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(user);
        transaction.commit();
    }

    @Override
    public void delete(User user) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(user);
        transaction.commit();
    }

    public void update(User userUpdated) {
        EntityManager em = getEntityManager();
        String query = "UPDATE User u SET u.name=:name, u.surname=:surname,"
                + "u.email=:email, u.userType=:type, u.status=:status WHERE u.id=:id";
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createQuery(query)
                .setParameter("name", userUpdated.getName())
                .setParameter("surname", userUpdated.getSurname())
                .setParameter("email", userUpdated.getEmail())
                .setParameter("status", userUpdated.getStatus())
                .setParameter("type", userUpdated.getUserType())
                .setParameter("id", userUpdated.getId())
                .executeUpdate();
        transaction.commit();
    }

    @Override
    public EntityManager getEntityManager() {
        return EntityManagerProvider.getEntityManager();
    }

    /**
     * Method that find and returns a user by a specific id.
     *
     * @param id- is the corresponding primary key of the user in the table
     * @return the corresponding User object.
     */
    public User getById(int id) {
        EntityManager em = getEntityManager();
        User user = (User) em.createNamedQuery("User.findById", User.class)
                .setParameter("id", id)
                .getSingleResult();
        return user;
    }

    /**
     * Method that find and returns a user by a specific id.
     *
     * @param email- is the corresponding user email
     * @return the corresponding User object.
     * @throws Exception
     */
    public User getUserByEmail(String email) throws Exception {
        EntityManager em = getEntityManager();
        User user = (User) em.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email)
                .getSingleResult();
        return user;
    }

    /**
     * Method to return all users in the database no matter of their
     * status(present or lazily deleted)
     *
     * @return a list of users
     */
    public List<User> getAllUsersStatusInsensitive() {
        String query = "SELECT u FROM User u";
        return getEntityManager().createQuery(query)
                .getResultList();
    }

    /**
     * Method to return all the users in the database.
     *
     * @return a list of users
     */
    @Override
    public List<User> getAll() {
        return getEntityManager().createNamedQuery("User.findAll", User.class)
                .getResultList();
    }

    public List<User> filterUsers(String name, String surname,
            String email, String type) {
        String query = "SELECT u FROM User u WHERE u.status !='D' ";
        if (name != null) {
            query += " AND LOWER(u.name) LIKE LOWER(:name)";
        }
        if (surname != null && !surname.equals("")) {
            query += " AND LOWER(u.surname) LIKE LOWER(:surname)";
        }
        if (email != null && !email.equals("")) {
            query += " AND LOWER(u.email) LIKE LOWER(:email)";
        }
        if (type != null && !type.equals("")) {
            query += " AND u.userType= :type";
        }

        TypedQuery<User> tQuery = getEntityManager().createQuery(query, User.class);
        if (name != null) {
            tQuery.setParameter("name", name + "%");
        }
        if (surname != null && !surname.equals("")) {
            tQuery.setParameter("surname", surname + "%");
        }
        if (email != null && !email.equals("")) {
            tQuery.setParameter("email", email + "%");
        }
        if (!type.equals("")) {
            tQuery.setParameter("type", type);
        }

        return tQuery.getResultList();

    }
}
