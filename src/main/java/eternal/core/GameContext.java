package eternal.core;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.control.UniverseHandler;
import eternal.game.environment.Universe;

@Named
@ApplicationScoped
public class GameContext {
    
    @Inject
    UniverseHandler uniHandler;
    
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
    
    
}
