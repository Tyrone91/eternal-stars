package eternal.user;

import java.util.Optional;

public interface UserInteraction<R,T> {
    
    public UserRight getNeededRight();
    public boolean isAllowedToPerform(User user);
    public Optional<R> performAction(User user, @SuppressWarnings("unchecked") T... args);
}
