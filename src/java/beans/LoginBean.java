/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import DAO.UserDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import models.User;

/**
 *
 * @author user
 */
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private String email;
    private String password;
    private User user;
    private String outputMessage = "";

    public LoginBean() {
    }

    /**
     * Method that performs the login.
     *
     * @throws IOException
     */
    public void login() throws IOException {
        outputMessage = "";
        boolean isUserAuthenticated = authenticateUser();

        if (!isUserAuthenticated) {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("login.xhtml");
        } else {
            outputMessage = "";
            if ("standard".equals(user.getUserType())) {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("indexStandard.xhtml");
            } else {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("indexAdmin.xhtml");
            }
        }
    }

    /**
     * Method to logout the current user.
     *
     * @throws IOException
     */
    public void logOut() throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("", new FacesMessage("You have been succesfully logged out!"));
        context.getExternalContext().invalidateSession();
        context.getExternalContext().redirect("login.xhtml");
    }

    /**
     * Method used to authenticate in a login attempt.
     */
    private boolean authenticateUser() {

        UserDAO userDAO = new UserDAO();

        try {
            User potentialUser = userDAO.getUserByEmail(email);
            if (potentialUser == null) {
                email = "";
                outputMessage = "Incorrect email.";
                return false;
            }

            if (decryptPassword(potentialUser.getPassword()).equals(password)) {
                user = potentialUser;
                return true;
            } else {
                outputMessage = "Incorrect password.";
                return false;
            }
        } catch (Exception e) {
            email = "";
            outputMessage = "Incorrect email. Exception occured";
            return false;
        }
    }

    /**
     * Method to check if a user is logged in or not. If yes then, go to the
     * specific page, else redirect to the login page.
     *
     * @param type- the type of user logged in
     * @return the page were it will land
     */
    public String isUserLoggedIn(String type) {
        email = "";
        try {
            if (user == null || !type.equals(user.getUserType())) {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("login.xhtml");
            }
        } catch (IOException e) {
            return "login.xhtml";
        }
        return "";
    }

    /**
     * Method used to go back to the index page after a user or an admin has
     * gone to another page.
     *
     * @param userType - is the type of the logged in user.
     * @return a String that can be a landing page in case of an exception.
     */
    public String navigate(String userType) {

        if (userType.equals("standard")) {
            return "indexStandard.xhtml";
        } else {
            return "indexAdmin.xhtml";
        }
    }

    /**
     * Method to check is a user is logged in or not. It is used only from the
     * profile.xhtml view.
     *
     * @return a string that will be the landing page in case of an excpetion.
     */
    public String isUserLoggedIn() {
        email = "";
        try {
            if (user == null) {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("login.xhtml");
            }
        } catch (IOException e) {
            return "login.xhtml";
        }
        return "";
    }

    /**
     * ----------------Getters and setters start here--------------------------
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public User getUser() {
        return user;
    }
    
    /**
     * ----------------Getters and setters end here--------------------------
     */
    /**
     * Method used to decrypt the password from the database.
     *
     * @param password is the string to be decrypted.
     * @return is the decrypted string.
     */
    public String decryptPassword(String password) {
        return new String(Base64.getDecoder().decode(password.getBytes()));
    }
}
