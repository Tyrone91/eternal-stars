package eternal.session;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import eternal.user.AnonymousUser;
import eternal.user.User;

@Named
@SessionScoped
public class SessionContext implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final User ANOYMOUS_USER = new AnonymousUser();
    
    private User user;
    
    @PostConstruct
    public void init() {
        user = ANOYMOUS_USER;
    }
    
    public SessionContext() {}
    
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public boolean isLoggedIn() {
        return ANOYMOUS_USER != user;
    }

}
