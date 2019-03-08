package eternal.actions;

import java.io.Serializable;
import java.util.Optional;

import javax.inject.Inject;

import eternal.requests.ErrorResponse;
import eternal.user.User;
import eternal.user.UserInteraction;
import eternal.util.ExceptionHandler;

public abstract class AbstractAction<R,T> implements UserInteraction<R,T>, Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private ErrorResponse errorResponse;
    
    @Inject
    private ExceptionHandler exceptionHandler;

    @Override
    public boolean isAllowedToPerform(User user) {
        return user.hasRight(getNeededRight());
    }
    
    @Override
    public Optional<R> performAction(User user, @SuppressWarnings("unchecked") T... args) {
        try {
            if(!isAllowedToPerform(user)) {
                error(getClass().getSimpleName() + ": Not allowed.");
                return Optional.empty();
            }
            return Optional.ofNullable(action(user, args));
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            error("An unexpected error occured");
            return Optional.empty();
        }
    }
    
    protected abstract R action(User user, @SuppressWarnings("unchecked") T... args);
    
    @SuppressWarnings("unchecked")
    protected <TT> TT cast(Object o) {
        return (TT)(o);
    }
    
    protected void error(String message) {
        errorResponse.setMessage(message);
    }
}
