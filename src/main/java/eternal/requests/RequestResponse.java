package eternal.requests;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import eternal.util.Strings;

@Named
@RequestScoped
public class RequestResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String message = "";
    
    private boolean isBad = true;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isBad() {
        return isBad;
    }
    
    public void setBad(boolean bad) {
        this.isBad = bad;
    }
    
    public boolean isDisplayed() {
        return Strings.exists(message);
    }

}
