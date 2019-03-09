package eternal.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.buildable.BuildingTemplate;
import eternal.game.control.PlanetHandler;
import eternal.game.control.UniverseHandler;
import eternal.game.environment.Universe;

/**
 * Holder class for gameplay relevant information like {@link BuildingTemplate templates}.
 * Additionally the context holds some Beans that can be accessed by none managed objects.
 */
@Named
@ApplicationScoped
public class GameContext {
    
    private static Map<Integer, BuildingTemplate> buildTemplateMap(BuildingTemplate...buildingTemplates ) {
        final Map<Integer, BuildingTemplate> map = new HashMap<>();
        for(BuildingTemplate t : buildingTemplates) {
            map.put(t.getId(), t);
        }
        return map;
    }
    
    private final static Map<Integer, BuildingTemplate> BUILDING_TEMPLATES = buildTemplateMap(
            BuildingTemplate.METAL_MINE,
            BuildingTemplate.CRYSTAL_MINE,
            BuildingTemplate.ENERGY_GENERATOR,
            BuildingTemplate.DEEP_MINE
            );
    
    @Inject
    private UniverseHandler uniHandler;
    
    @Inject
    private Game game;
    
    @Inject
    private PlanetHandler planetHandler;
    
    public GameContext() {
        
    }
    
    @PostConstruct
    public void init() {
    }
    
    public void addShipTemplate() {
        
    }
    
    public void addBuildingTemplate() {
        
    }

    public Universe getUniverse() {
        return uniHandler.getCurrentUniverse();
    }

    public void setUniverse(Universe universe) {
        this.uniHandler.setCurrentUniverse(universe);
    }
    
    public UniverseHandler getUniverseHandler() {
        return this.uniHandler;
    }
    
    public Optional<BuildingTemplate> findBuildingTemplate(int id) {
        return Optional.ofNullable(BUILDING_TEMPLATES.get(id));
    }
    
    public List<BuildingTemplate> getBuildingsTemplates() {
        final List<BuildingTemplate> list = new ArrayList<>(BUILDING_TEMPLATES.values());
        list.sort( (t1,t2) -> Integer.compare(t1.getId(), t2.getId()) );
        return list;
    }
    
    public Game getGame() {
        return game;
    }
    
    public PlanetHandler getPlanetHandler() {
        return this.planetHandler;
    }
    
}
