package eternal.dev;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import eternal.game.control.GameAccountCreator;
import eternal.mangement.UserHandler;
import eternal.mangement.UserRoleHandler;
import eternal.persistence.UserRoleAccessObject;
import eternal.session.InteractionHandler;
import eternal.user.User;
import eternal.user.UserRole;


@ApplicationScoped
public class DemoInit {
    
    @Inject
    UserRoleHandler userRoleHandler;
    
    @Inject
    UserRoleAccessObject userRoleDOA;
    
    @Inject
    InteractionHandler interactionHandler;
    
    @Inject
    private GameAccountCreator accountCreator;
    
    @Inject
    UserHandler userHandler;

    @PostConstruct
    public void init() {
        
        createIfNotExists(UserRole.ADMIN);
        createIfNotExists(UserRole.ANONYMOUS);
        createIfNotExists(UserRole.NORMAL_USER);
        
        createIfNotExists("[SYS_ADMIN]", "admin", userRoleHandler.findRole(UserRole.ADMIN.getName()).orElse(UserRole.ADMIN));
        createIfNotExists("Test01", "123", userRoleHandler.findRole(UserRole.NORMAL_USER.getName()).orElse(UserRole.NORMAL_USER));
        
        //insertRandomUser("DEMO_ACCOUNT_PW_123", 23);
    }
    
    private void insertRandomUser(String name, int count) {
        for(int i = 0; i < count; ++i) {
            String userName = name + "_" + i;
            Optional<User> user = createIfNotExists(userName,"123", userRoleHandler.findRole(UserRole.NORMAL_USER.getName()).orElse(UserRole.NORMAL_USER));
            if(user.isPresent()) {
                accountCreator.createGameAccount(user.get(), userName);
            }
        }
    }
    
    private void createIfNotExists(UserRole role) {
        if(!userRoleDOA.getRole(role.getName()).isPresent()) {
            userRoleDOA.storeRole(role);
        }
    }
    
    private Optional<User> createIfNotExists(String user, String pw, UserRole role) {
        if(!userHandler.find(user).isPresent()) {            
            return userHandler.createNewUser(user, pw, "user@eternal-demo.com", role);
        }
        return Optional.empty();
    }

    public void createDemoGame() {
        
    }
}
