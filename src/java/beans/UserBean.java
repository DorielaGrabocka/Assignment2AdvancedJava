/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import DAO.UserDAO;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import models.User;

/**
 *
 * @author Doriela
 */
@ManagedBean
@ViewScoped
public class UserBean {

    @ManagedProperty(value = "#{loginBean}")
    LoginBean loginBean;

    private int currentUserId;
    private UserDAO userDAO;
    private List<User> listOfUsers;
    private String id;
    private boolean editing;
    private String name;
    private String surname;
    private String email;
    private String userType;
    private String message;
    private User user;
    private String deleteMessage;
    private String searchMessage = "Hello";

    //used for filters
    private String searchName;
    private String searchSurname;
    private String searchEmail;
    private String searchType;

    public UserBean() {
        userDAO = new UserDAO();
        allUsers();
    }

    public void init() {

    }

    public String getSearchMessage() {
        return searchMessage;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchSurname() {
        return searchSurname;
    }

    public void setSearchSurname(String searchSurname) {
        this.searchSurname = searchSurname;
    }

    public String getSearchEmail() {
        return searchEmail;
    }

    public void setSearchEmail(String searchEmail) {
        this.searchEmail = searchEmail;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void allUsers() {
        listOfUsers = userDAO.getAll().stream()
                .collect(Collectors.toList());
        searchMessage = "Showing " + listOfUsers.size() + " records";

    }

    public List<User> getListOfUsers() {
        return listOfUsers;
    }

    public void setListOfUsers(List<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    public void setUser(User u) {
        this.user = u;
    }

    public User getUser() {
        return user;
    }

    public String getDeleteMessage() {
        return deleteMessage;
    }

    public void setDeleteMessage(String deleteMessage) {
        this.deleteMessage = deleteMessage;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void fillData() {
        clear();
        editing = true;
        Map<String, String> parameters = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();
        int id = Integer.parseInt(parameters.get("userId"));

        User u = userDAO.getById(id);
        name = u.getName();
        surname = u.getSurname();
        email = u.getEmail();
        userType = u.getUserType();
        setUser(u);
    }

    public void save() {
        try {
            if (editing) {
                User u = userDAO.getById(user.getId());
                u.setName(name);
                u.setSurname(surname);
                u.setEmail(email);
                u.setUserType(userType);
                userDAO.update(u);//update user
                message = "User updated succesfully!";
                clear();
            } else {
                try {
                    //check if user is probably deleted
                    User u1 = userDAO.getAllUsersStatusInsensitive()
                            .stream()
                            .filter(u -> u.getEmail().equals(email))
                            .filter(u -> u.getUserType().equals(userType))
                            .collect(Collectors.toList())
                            .get(0);

                    //editing mode here
                    u1.setName(name);
                    u1.setSurname(surname);
                    u1.setEmail(email);
                    u1.setStatus('N');
                    u1.setUserType(userType);
                    u1.setPassword("user");
                    userDAO.update(u1);
                    message = "User added succesfully!";

                } catch (Exception e) {
                    //user with that email does not exist or that status does not exist
                    //normal insertion procedure goes here
                    User u = new User();
                    u.setName(name);
                    u.setSurname(surname);
                    u.setEmail(email);
                    u.setPassword(encrypt("user"));
                    u.setStatus('N');//set user status to NOT DELETED
                    u.setUserType(userType);
                    userDAO.insert(u);//add user
                    message = "User added succesfully! Check users table";
                }
            }
            allUsers();//update the list
        } catch (Exception e) {
            message = "An error has occured! New Email might already be taken!";
        }
    }

    /**
     * Utility method to clear the view after an insert or delete.
     */
    public void clear() {
        name = "";
        surname = "";
        email = "";
        deleteMessage = "";
        if (!editing) {
            message = "";
        }
        editing = false;
    }

    private String encrypt(String pass) {
        return Base64.getEncoder().encodeToString(pass.getBytes());
    }

    /**
     * Method to mark a user as deleted in a database.
     *
     */
    public void delete() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();
        String sID = params.get("userId");
        int idD = Integer.parseInt(sID);
        User proccessedUser = userDAO.getById(idD);
        proccessedUser.setStatus('D');
        userDAO.update(proccessedUser);
        deleteMessage = "User deleted succesfully";
        allUsers();
    }

    /**
     * Method to search for a specific user
     */
    public void searchUsers() throws Exception {
        listOfUsers = userDAO.filterUsers(searchName, searchSurname,
                searchEmail, searchType)
                .stream()
                .collect(Collectors.toList());
        searchMessage = "Showing " + listOfUsers.size() + " record(s)";
    }
}
