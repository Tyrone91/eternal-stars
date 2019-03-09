package eternal.actions;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.mangement.UserHandler;
import eternal.user.User;
import eternal.user.UserRight;

/**
 * Returns a list of all users.
 */
@Named
@SessionScoped
public class GetUserListAction extends AbstractAction<List<User>, Void> {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private UserHandler userHandler;

    @Override
    public UserRight getNeededRight() {
        return UserRight.USER_MANAGMENT_VIEW_ALL_USERS;
    }

    @Override
    protected List<User> action(User user, Void... args) {
        return userHandler.getAllRegisteredUsersAsList();
    }
    
}
