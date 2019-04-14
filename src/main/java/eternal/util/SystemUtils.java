package eternal.util;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.mangement.UserHandler;
import eternal.mangement.UserRoleHandler;
import eternal.persistence.data.MessageTO;
import eternal.user.User;
import eternal.user.UserRole;

@Named
@ApplicationScoped
public class SystemUtils {

    private User system;
    
    @Inject
    private UserRoleHandler userRoleHandler;

    @Inject
    private UserHandler userHandler;
    
    @PostConstruct
    private void setup() {
        this.system = createSystemUser();
    }
    
    public boolean isSystemMessage(MessageTO message) {
        return system.getUsername().equals(message.getSender().getUsername());
    }
    
    public User getSystemUser() {
        return this.system;
    }
    
    private User createSystemUser() {
        final User user = new User();
        user.setEmail("");
        user.setUserName("[SYSTEM]");
        user.setPassword("I_THINK_THE_MERGE_FROM_JPA_WILL_INSERT_THIS_USER_IN_THE_DATABASE. THAT_IS_NOT_SOOO_BAD_BUT_WOULD_ALLOW_A_LOGIN_WITH_THIS_USER_SO_WE_NEED_A_GOOD_PASSWORD"); //TODO: I should reconsider my future as a programmer.
        
        UserRole role = userRoleHandler.findRole(UserRole.ADMIN.getName()).orElse(UserRole.ADMIN); // If the role could not be loaded from DB take the default one.
        user.addRole(role);
        
        return createOrGet(user).get();
    }
    
    private Optional<User> createOrGet(User user) {
        final Optional<User> res = userHandler.find(user.getUsername());
        if(!res.isPresent()) {            
            return userHandler.createNewUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getRoles().toArray(new UserRole[0]));
        }
        return res;
    }
}
