package eternal.game.control;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.environment.Planet;
import eternal.game.environment.Sector;
import eternal.persistence.GameAccountDataAccessObject;

/**
 * Management class to remove a {@link GameAccount}.
 * Because we don't want empty {@link Sector} we have to delete not only the planet.
 */
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
        
        return gameAccoutnDAO.deleteAccount(account.getOwnerId());
    }
    
    public boolean removeAccount(String gameAccountId ) {
        Optional<GameAccount> account = gameAccoutnDAO.findAccount(gameAccountId);
        if(!account.isPresent()) {
            return false;
        }
        return removeAccount(account.get());
    }
}
