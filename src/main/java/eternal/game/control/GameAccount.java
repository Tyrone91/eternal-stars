package eternal.game.control;

import javax.persistence.Entity;
import javax.persistence.Id;

import eternal.core.GameContext;
import eternal.user.User;

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
