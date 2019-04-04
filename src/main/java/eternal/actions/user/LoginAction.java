package eternal.actions.user;

import java.util.Optional;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.AbstractAction;
import eternal.mangement.LoginHandler;
import eternal.requests.LoginRequest;
import eternal.session.SessionContext;
import eternal.user.User;
import eternal.user.UserRight;

/**
 * Allows the login to the application.
 */
@Named
@SessionScoped
public class LoginAction extends AbstractAction<User, LoginRequest> {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private LoginHandler loginHandler;
    
    @Inject
    private SessionContext sessionContext;
    
    @Override
    public UserRight getNeededRight() {
        return UserRight.LOGIN;
    }

    @Override
    public User action(User user, LoginRequest... args) {
        LoginRequest request = args[0];
        Optional<User> res = loginHandler.login(request);
        if(res.isPresent()) {
            sessionContext.setUser(res.get());
        }
        return res.orElse(null);
    }

}
