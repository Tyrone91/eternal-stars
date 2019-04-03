package eternal.util;

import eternal.user.User;

public class GameAccountNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final User owner;

    public GameAccountNotFoundException(User owner) {
        super(String.format("Account of user '%s' not found", owner.getUsername()));
        this.owner = owner;
    }
    
    public User getGameAccountOwner() {
        return this.owner;
    }
}
