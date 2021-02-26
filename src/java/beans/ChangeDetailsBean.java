/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import DAO.UserDAO;
import java.util.Base64;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import models.User;

/**
 *
 * @author Doriela
 */
@ManagedBean
@RequestScoped
public class ChangeDetailsBean {

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
    private String messagePassword;

    private String messageDetails;
    private String email;
    private String surname;
    private String name;

    public ChangeDetailsBean() {
    }

    public User getCurrentUser() {
        return loginBean.getUser();
    }

    public String getName() {
        return getCurrentUser().getName();
    }

    public String getEmail() {
        return getCurrentUser().getEmail();
    }

    public String getSurname() {
        return getCurrentUser().getSurname();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCurrentPassowrd(String currentPassowrd) {
        this.currentPassword = currentPassowrd;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getCurrentPassowrd() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getMessagePassword() {
        return messagePassword;
    }

    public String getMessageDetails() {
        return messageDetails;
    }

    /**
     * Method used to update the details of a user like name, surname and email.
     */
    public void updateProfile() {
        UserDAO userDAO = new UserDAO();
        try {
            getCurrentUser().setName(name);//change name
            getCurrentUser().setSurname(surname);//change surname
            getCurrentUser().setEmail(email);//change email
            userDAO.update(getCurrentUser());//update
            messageDetails = "User Data updated successfully!";
        } catch (Exception e) {
            messageDetails = "This email is already taken! "
                    + "Try another one or simply go back without making any changes.";
        }
    }

    /**
     * Method to update the password of the user
     */
    public void updatePassword() {
        if (!doPasswordsMatch(encryptPassword(currentPassword),
                getCurrentUser().getPassword())) {
            messagePassword = "Incorrect current password";
        } else if (doPasswordsMatch(currentPassword, newPassword)) {
            messagePassword = "New password cannot be the same as old password!";
        } else if (!doPasswordsMatch(newPassword, confirmNewPassword)) {
            messagePassword = "New and Confirmed passwords do not match";
        } else {
            //set password now
            UserDAO userDAO = new UserDAO();
            getCurrentUser().setPassword(encryptPassword(newPassword));
            userDAO.update(getCurrentUser());
            messagePassword = "Password changed succesfully";
        }
    }

    /**
     * Utility method to encrypt the user password before saving it to the database.
     */
    private String encryptPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    /**
     * Utility method to check if two password match or not.
     */
    private boolean doPasswordsMatch(String pass1, String pass2) {
        return pass1.equals(pass2);
    }

}
