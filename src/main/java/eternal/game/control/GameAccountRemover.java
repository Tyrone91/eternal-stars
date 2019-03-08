package eternal.game.control;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.environment.Planet;
import eternal.persistence.GameAccountDataAccessObject;

@Named
@ApplicationScoped
public class GameAccountRemover {
    
    @Inject
    private PlanetHandler planetHandler;
    
    @Inject
    private UniverseHandler universeHandler;
    
    @Inject 
    private GameAccountDataAccessObject gameAccoutnDAO;
    
    public boolean removeAccount(GameAccount account) {
        Optional<Planet> planet = planetHandler.findPlanet(account.getHomePlanetId());
        if(planet.isPresent()) {
            universeHandler.detachPlanet(planet.get());
            planetHandler.removePlanet(planet.get().getPlanetId());
        }
        
        //TODO: remove future fleet stuff too.
        
        return gameAccoutnDAO.deleteAccount(account.getId());
    }
    
    public boolean removeAccount(String gameAccountId ) {
        Optional<GameAccount> account = gameAccoutnDAO.findAccount(gameAccountId);
        if(!account.isPresent()) {
            return false;
        }
        return removeAccount(account.get());
    }
}
