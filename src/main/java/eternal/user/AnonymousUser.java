package eternal.user;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.mangement.UserRoleHandler;

@Named
@SessionScoped
public class AnonymousUser extends User {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private UserRoleHandler userRoleHandler;
    
    public AnonymousUser() {
    }
    
    @PostConstruct
    public void init() {
        setEmail("");
        setUserName("Anonymous");
        setPassword("");
        
        UserRole role = userRoleHandler.findRole(UserRole.ANONYMOUS.getName()).orElse(UserRole.ANONYMOUS); // If the role could not be loaded from DB take the default one.
        addRole(role);
        
    }

}
