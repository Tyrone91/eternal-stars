package eternal.core;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.mangement.RegistrationHandler;
import eternal.mangement.UserHandler;
import eternal.mangement.UserRoleHandler;
import eternal.persistence.UserRoleAccessObject;
import eternal.user.UserRole;

@Named
@ApplicationScoped
public class EternalStarApp implements Serializable {
    
    @Inject
    RegistrationHandler regstrationHandler;
    
    @Inject
    UserHandler userHandler;
    
    @Inject
    UserRoleHandler userRoleHandler;
    
    @Inject
    UserRoleAccessObject userRoleDOA;
    
    private static final long serialVersionUID = 1L;

    public EternalStarApp() {}
    
    private void insertDefaultUserAndRoles() {
        setIfNotExists(UserRole.ADMIN);
        setIfNotExists(UserRole.ANONYMOUS);
        setIfNotExists(UserRole.NORMAL_USER);
        
        System.out.println(userRoleDOA.getRole("ADMIN"));
        
        userHandler.deleteUser("[SYS_ADMIN]");
        userHandler.createNewUser("[SYS_ADMIN]", "admin", "admin@eternal-stars.com", UserRole.ADMIN);
    }
    
    private void setIfNotExists(UserRole role) {
        if(!userRoleDOA.getRole(role.getName()).isPresent()) {
            userRoleDOA.storeRole(role);
        }
    }
    
    @PostConstruct
    public void init() {
        System.out.println("Application: started");
        
        
        insertDefaultUserAndRoles();
    }
    
    public String getName() {
        return "Eternal Stars";
    }
    
    public String getVersion() {
        return "0.0.1";
    }
}
