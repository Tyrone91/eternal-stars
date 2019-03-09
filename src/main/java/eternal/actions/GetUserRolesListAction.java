package eternal.actions;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.mangement.UserRoleHandler;
import eternal.user.User;
import eternal.user.UserRight;
import eternal.user.UserRole;

/**
 * Returns a list of all roles.
 */
@Named
@SessionScoped
public class GetUserRolesListAction extends AbstractAction<List<UserRole>, Void> {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private UserRoleHandler handler;

    @Override
    public UserRight getNeededRight() {
        return UserRight.USER_MANAGMENT_VIEW_ALL_ROLES;
    }

    @Override
    protected List<UserRole> action(User user, Void... args) {
        return handler.getRolesAsList();
    }

}
