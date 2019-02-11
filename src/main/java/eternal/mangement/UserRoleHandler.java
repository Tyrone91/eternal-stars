package eternal.mangement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.persistence.UserRoleAccessObject;
import eternal.user.UserRight;
import eternal.user.UserRole;
import eternal.util.ExceptionHandler;

@Named
@ApplicationScoped
public class UserRoleHandler {
    
    @Inject
    private UserRoleAccessObject userRoleDOA;
    
    @Inject
    private ExceptionHandler exceptionHandler;
    
    private List<UserRole> roles;
    
    @PostConstruct
    public void init() {
        roles = loadRoles();
    }
    
    private List<UserRole> loadRoles() {
        return new ArrayList<>(userRoleDOA.getAllRoles());
    }
    
    public List<UserRole> getRolesAsList() {
        return roles;
    }
    
    public Optional<UserRole> findRole(String roleName) {
        return userRoleDOA.getRole(roleName);
    }
    
    public Optional<UserRole> createRole(String roleName, String description, UserRight...rights ) {
        try {
            if(findRole(roleName).isPresent()) {
                return Optional.empty();
            }
            
            UserRole role = new UserRole();
            role.setDescription(description);
            role.setName(roleName);
            role.setRights( new HashSet<>(Arrays.asList(rights)));
            
            userRoleDOA.storeRole(role);
            
            return Optional.of(role);
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return Optional.empty();
        }
    }
}
