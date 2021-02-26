/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;


import javax.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author Doriela
 */

public interface BaseDao<T> {
    EntityManager getEntityManager();
    
    /**Method to insert a new object in the database
    * @param t is the object to  be inserted
    * @throws Exception to be caught on the class where it is called.
    */
    void insert(T t) throws Exception;
    
    /**Method to delete an object from the database
    * @param t is the object to  be deleted
    * @throws Exception to be caught on the class where it is called.
    */
    void delete(T t) throws Exception;
    
    /**Method to get all entities of a certain type from the database.
     *@return List of entities T.
     */
    List<T> getAll();
}
