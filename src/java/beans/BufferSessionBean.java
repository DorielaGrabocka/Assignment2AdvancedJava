/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Doriela
 */
@ManagedBean
@SessionScoped
public class BufferSessionBean {

    private int bookIDFromIndexToDetails;

    public BufferSessionBean() {
    }

    public int getBookIDFromIndexToDetails() {
        return bookIDFromIndexToDetails;
    }

    public String saveBookIDFromIndexToDetails(int bookIDFromIndexToDetails) {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap();

        setBookIDFromIndexToDetails(Integer.parseInt(params.get("bookID")));
        return "bookDetails.xhtml?faces-redirect=true";
    }

    public void setBookIDFromIndexToDetails(int bookIDFromIndexToDetails) {
        this.bookIDFromIndexToDetails = bookIDFromIndexToDetails;
    }
}
