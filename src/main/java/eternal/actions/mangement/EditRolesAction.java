package eternal.actions.mangement;

import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.AbstractAction;
import eternal.mangement.UserRoleHandler;
import eternal.requests.EditUserRoleRequest;
import eternal.requests.EditUserRoleRequest.RightWrapper;
import eternal.requests.EditUserRoleRequest.RoleWrapper;
import eternal.user.User;
import eternal.user.UserRight;
import eternal.user.UserRole;

/**
 * Allows the modification of the of the rights of a given role.
 */
@Named
@SessionScoped
public class EditRolesAction extends AbstractAction<Boolean, EditUserRoleRequest> {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    UserRoleHandler handler;
    

    @Override
    public UserRight getNeededRight() {
        return UserRight.USER_MANAGMENT_EDIT_ALL_ROLES;
    }

    @Override
    protected Boolean action(User user, EditUserRoleRequest... args) {
        EditUserRoleRequest request = args[0];
        
        boolean updated = true;
        for(RoleWrapper wrapper : request.getRoles() ) {
            
            UserRole role = wrapper.getRole();
            Set<UserRight> rights = wrapper.getRights().stream()
                    .filter(RightWrapper::isSelected)
                    .map(RightWrapper::getRight)
                    .collect(Collectors.toSet());
            
            role.setRights(rights);
            
            boolean res = handler.updateRole(role);
            updated = updated && res;
        }
        
        if(updated) {
            request.setResponse("Role successful updated");
        } else {
            request.setResponse("Not all roles (if any) could be updated");
        }
        
        return true;
    }

}
