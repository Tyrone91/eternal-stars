package eternal.actions.user;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.AbstractAction;
import eternal.session.SessionContext;
import eternal.user.User;
import eternal.user.UserRight;

/**
 * Allows the logout out of the application,
 */
@Named
@SessionScoped
public class LogoutAction extends AbstractAction<Void, Void>{
    
    @Inject
    private SessionContext context;
    
    private static final long serialVersionUID = 1L;

    @Override
    public UserRight getNeededRight() {
        return  UserRight.LOGIN;
    }

    @Override
    protected Void action(User user, Void... args) {
        context.setUser(null);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return null;
    }
    
    
}
