package eternal.mangement;

import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import eternal.user.User;

@Named
@ApplicationScoped
public class UserHandler {
    
    public Set<User> getAllRegisteredUsers() {
        return null;
    }
    
    public Optional<User> find(String username) {
        return null;
    }
}
