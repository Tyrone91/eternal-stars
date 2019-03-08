package eternal.actions;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.mangement.UserHandler;
import eternal.user.User;
import eternal.user.UserRight;

@Named
@SessionScoped
public class DeleteAnyUserAction extends AbstractAction<Boolean, User> {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private UserHandler userHandler;

    @Override
    public UserRight getNeededRight() {
        return UserRight.USER_MANAGMENT_DELETE_USER;
    }

    @Override
    protected Boolean action(User initiator, User... args) {
        final User target = args[0];
        return userHandler.deleteUser(target.getUsername());
    }
    
}
