package eternal.game.environment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import eternal.core.Game;
import eternal.core.GameLoop.Updatable;

@Entity
public class Planet implements Updatable {
    
    @Id
    @GeneratedValue
    private int planetId;
    
    private String gameAccountId;
    
    //private Resources planetResources;
    
    private String planetName;
    
    private int planetPosition;
    
    private int sectorId;
    
    public int getPlanetId() {
        return planetId;
    }

    public void setPlanetId(int id) {
        this.planetId = id;
    }

    public String getOwnerId() {
        return gameAccountId;
    }

    public void setOwner(String ownerId) {
        this.gameAccountId = ownerId;
    }
    /*
    public Resources getPlanetResources() {
        return planetResources;
    }

    public void setPlanetResources(Resources planetResources) {
        this.planetResources = planetResources;
    }
    */

    public String getName() {
        return planetName;
    }

    public void setName(String name) {
        this.planetName = name;
    }

    public int getPosition() {
        return planetPosition;
    }

    public void setPosition(int position) {
        this.planetPosition = position;
    }

    public int getSector() {
        return sectorId;
    }

    public void setSector(int sector) {
        this.sectorId = sector;
    }

    @Override
    public void update(Game g) {
        
    }

}
