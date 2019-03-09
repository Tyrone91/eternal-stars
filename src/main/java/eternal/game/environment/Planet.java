package eternal.game.environment;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import eternal.core.Game;
import eternal.core.GameContext;
import eternal.core.GameLoop.Updatable;
import eternal.game.Resources;
import eternal.game.buildable.Building;
import eternal.game.buildable.BuildingTemplate;

/**
 * The virtual physical home of an player in the universe.
 * The planets created resources and is the anchor for future fleets.
 */
@Entity
public class Planet implements Updatable {
    
    @Id
    @GeneratedValue
    private int planetId;
    
    private String gameAccountId;
    
    @JoinColumn
    @OneToOne(cascade = CascadeType.PERSIST)
    private Resources planetResources = new Resources(0, 0, 0);
    
    private String planetName;
    
    private int planetPosition;
    
    private int sectorId;
    
    @OneToMany(
            mappedBy = "owner",
            cascade  = CascadeType.ALL,
            orphanRemoval = true
            )
    private List<Building> buildings;
    
    public void onload(GameContext context) {
        if(buildings == null || buildings.isEmpty()) {
            final ArrayList<Building> tmp = new ArrayList<>();
            for(BuildingTemplate template : context.getBuildingsTemplates()) {
                final Building b = new Building(template, this);
                b.setLevel(template.getStartLevel());
                b.onload(context);
                tmp.add(b);
            }
            buildings = tmp;
        } else {
            buildings.stream().forEach(b -> b.onload(context));
        }
        context.getGame().getGameLoop().addObject(this);
    }
    
    public void onremove(GameContext context) {
        context.getGame().getGameLoop().removeObject(this);
    }
    
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
    
    public Resources getPlanetResources() {
        return planetResources;
    }

    public void setPlanetResources(Resources planetResources) {
        this.planetResources = planetResources;
    }
    

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
    
    public List<Building> getBuildings() {
        return this.buildings;
    }
    
    public Resources getGainPerMinute() {
        return this.buildings.stream().map(Building::getGainPerMinute).reduce( new Resources(), (ores, nres) -> {
            ores.add(nres);
            return ores;
        });
    }

    @Override
    public void update(Game g) {
        for(Building b: buildings) {
            b.update(g);
        }
    }

}
