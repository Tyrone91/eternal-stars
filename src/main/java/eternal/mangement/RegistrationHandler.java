package eternal.mangement;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.requests.RegistrationRequest;
import eternal.user.User;
import eternal.user.UserRole;
import eternal.util.Strings;

@Named
@ApplicationScoped
public class RegistrationHandler {
    
    @Inject
    private UserHandler userHandler;
    
    public RegistrationHandler() {}
    
    private boolean allInformationProvided(RegistrationRequest request) {
        return Strings.exists(
                request.getEmail(),
                request.getPassword(),
                request.getPasswordConfirmation(),
                request.getUsername()
                );
        
    }
    
    public Optional<User> register(RegistrationRequest request) {
        if(!allInformationProvided(request)) {
            request.setResponse("Not all information provided");
            return Optional.empty();
        }
        
        if(!request.getPassword().equals(request.getPasswordConfirmation()) ) {
            request.setResponse("Passwords must be equal");
            return Optional.empty();
        }
        
        Optional<User> user = userHandler.createNewUser(request.getUsername(), request.getPassword(), request.getEmail(), UserRole.NORMAL_USER);
        if(!user.isPresent()) {
            request.setResponse("Couldn't create new user");
            return Optional.empty();
        }
        
        request.setResponse("Created new user with role: " + (
                user.get().getRoles().stream().map(UserRole::getName).collect(Collectors.joining(","))
                ));
        //request.setResponse("Everything successful. You can now login");
        return user;
    }
    
    
}
