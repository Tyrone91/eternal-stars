package eternal.actions;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.control.UniverseHandler;
import eternal.game.environment.Universe;
import eternal.user.User;
import eternal.user.UserRight;

/**
 * Returns a list of all universes.
 */
@Named
@SessionScoped
public class ViewUniversesAction extends AbstractAction<List<Universe>, Void> {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private UniverseHandler handler;
    
    @Override
    public UserRight getNeededRight() {
        return UserRight.UNIVERSE_MANAGMENT_VIEW;
    }

    @Override
    protected List<Universe> action(User user, Void... args) {
        return handler.getUniverseList();
    }

}
