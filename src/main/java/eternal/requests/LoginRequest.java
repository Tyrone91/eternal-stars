package eternal.requests;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class LoginRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    
    @PostConstruct
    public void init() {
        System.out.println("LoginRequest: init");
    }
    
    public LoginRequest() {
        
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
