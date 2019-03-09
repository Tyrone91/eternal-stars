package eternal.actions;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.mangement.RegistrationHandler;
import eternal.requests.RegistrationRequest;
import eternal.user.User;
import eternal.user.UserRight;

/**
 * Allows the registration of a new user.
 */
@Named
@SessionScoped
public class RegistrationAction extends AbstractAction<User, RegistrationRequest> {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private RegistrationHandler handler;

    @Override
    public UserRight getNeededRight() {
        return UserRight.REGISTER;
    }

    @Override
    public User action(User user, RegistrationRequest... args) {
        RegistrationRequest request = args[0];
        return handler.register(request).orElse(null);
    }

}
