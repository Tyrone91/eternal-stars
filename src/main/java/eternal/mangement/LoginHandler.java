package eternal.mangement;

import java.util.Date;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.requests.LoginRequest;
import eternal.user.User;

@Named
@ApplicationScoped
public class LoginHandler {
    
    @Inject
    private UserHandler userHandler;
    
    public LoginHandler() {
        
    }
    
    public Optional<User> login(LoginRequest loginRequest) {
        System.out.println("Loginrequest");
        
        Optional<User> res = userHandler.find(loginRequest.getUsername());
        if(!res.isPresent() ) {
            loginRequest.setResponse("User not found");
            return Optional.empty();
        }
        
        if(!res.get().getPassword().equals(loginRequest.getPassword())) {
            loginRequest.setResponse("Wrong password");
            return Optional.empty();
        }
        
        loginRequest.setResponse("Login successful");
        res.get().setLastLogin(new Date());
        userHandler.updateUser(res.get());
        
        return res;
    }
}
