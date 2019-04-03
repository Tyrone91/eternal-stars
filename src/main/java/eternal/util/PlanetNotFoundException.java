package eternal.util;

import eternal.game.control.GameAccount;

public class PlanetNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final int planetId;
    private final GameAccount account;

    public PlanetNotFoundException(int planetId, GameAccount owner) {
        super(String.format("No planet with id '%s' of user '%s' not found", planetId, owner.getOwnerId() ));
        this.planetId = planetId;
        this.account = owner;
    }
    
    public int getPlanetId() {
        return this.planetId;
    }
    
    public GameAccount getOwnerAccount() {
        return this.account;
    }
}
