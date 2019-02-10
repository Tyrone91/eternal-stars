package eternal.mangement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.requests.LoginRequest;

@Named
@ApplicationScoped
public class LoginHandler {
    
    @Inject
    private UserHandler userHandler;
    
    public LoginHandler() {
        
    }
    
    public void login(LoginRequest loginRequest) {
        System.out.println("LoginHandler: login");
        
        
    }
}
