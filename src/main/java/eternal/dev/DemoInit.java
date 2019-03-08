package eternal.dev;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.mangement.UserHandler;
import eternal.mangement.UserRoleHandler;
import eternal.persistence.UserRoleAccessObject;
import eternal.user.UserRole;

@Named
@ApplicationScoped
public class DemoInit {
    
    @Inject
    UserRoleHandler userRoleHandler;
    
    @Inject
    UserRoleAccessObject userRoleDOA;
    
    @Inject
    UserHandler userHandler;

    @PostConstruct
    public void init() {
        
        createIfNotExists(UserRole.ADMIN);
        createIfNotExists(UserRole.ANONYMOUS);
        createIfNotExists(UserRole.NORMAL_USER);
        
        createIfNotExists("[SYS_ADMIN]", "admin", userRoleHandler.findRole(UserRole.ADMIN.getName()).orElse(UserRole.ADMIN));
        createIfNotExists("Test01", "123", userRoleHandler.findRole(UserRole.NORMAL_USER.getName()).orElse(UserRole.NORMAL_USER));
    }
    
    private void createIfNotExists(UserRole role) {
        if(!userRoleDOA.getRole(role.getName()).isPresent()) {
            userRoleDOA.storeRole(role);
        }
    }
    
    private void createIfNotExists(String user, String pw, UserRole role) {
        if(!userHandler.find(user).isPresent()) {            
            userHandler.createNewUser(user, pw, "user@eternal-demo.com", role);
        }
    }
    
    public void createDemoGame() {
        
    }
}
