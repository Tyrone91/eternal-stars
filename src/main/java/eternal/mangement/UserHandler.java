package eternal.mangement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.persistence.UserDataAccessObject;
import eternal.user.User;
import eternal.user.UserRole;
import eternal.util.ExceptionHandler;

@Named
@ApplicationScoped
public class UserHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ExceptionHandler exceptionHandler;
    
    @Inject
    private UserDataAccessObject userDAO;
    
    public Set<User> getAllRegisteredUsers() {
        return userDAO.fetchAllUsers();
    }
    
    public List<User> getAllRegisteredUsersAsList() {
        return new ArrayList<>(userDAO.fetchAllUsers());
    }
    
    public Optional<User> find(String username) {
        return userDAO.loadUser(username);
    }
    
    public Optional<User> createNewUser(String username, String passwrod, String email, UserRole...roles ) {
        try {
            if(find(username).isPresent()) {
                return Optional.empty();
            }
            
            final User u = new User();
            u.setUserName(username);
            u.setPassword(passwrod);
            u.setEmail(email);
            u.setRoles( new HashSet<>(Arrays.asList(roles)));
            
            if(!userDAO.storeUser(u)) {
                return Optional.empty();
            }
            
            return Optional.of(u);
        } catch(Exception e) {
            exceptionHandler.handleException(e);
        }
        return Optional.empty();
    }
    
    public void updateUser(User u) {
        userDAO.updateUser(u);
    }
    
    public boolean deleteUser(User u) {
        return deleteUser(u.getUsername());
    }
    
    public boolean deleteUser(String username) {
        try {
            return userDAO.deleteUser(username);
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }
 
}
