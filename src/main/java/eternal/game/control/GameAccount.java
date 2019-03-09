package eternal.game.control;

import javax.persistence.Entity;
import javax.persistence.Id;

import eternal.core.GameContext;
import eternal.user.User;

/**
 * A {@link GameAccount} only exists in the context of a normal user.
 * The admin as an example would be user who is not suppose to play the game so he would not have a {@link GameAccount}.
 * The most users will have a {@link GameAccount}.
 * The {@link GameAccount} augments the normal user-account with the game relevant information like homeplanet and displayname.
 */
@Entity
public class GameAccount {
    
    @Id
    private String ownerId;
    
    private String displayName;
    
    private int homePlanetId;

    public String getId() {
        return ownerId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOwnerId() {
        return getOwnerId();
    }

    public void setOwnerId(User owner) {
        this.ownerId = owner.getUsername();
    }

    public int getHomePlanetId() {
        return homePlanetId;
    }

    public void setHomePlanetId(int homePlanetId) {
        this.homePlanetId = homePlanetId;
    }
    
    public void onload(GameContext context) {
            
    }
}
