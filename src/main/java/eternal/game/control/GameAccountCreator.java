package eternal.game.control;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.core.Game;
import eternal.game.environment.Planet;
import eternal.persistence.GameAccountDataAccessObject;
import eternal.persistence.PlanetDataAccessObject;
import eternal.user.User;
import eternal.util.ExceptionHandler;

/**
 * Management class to create {@link GameAccount} for a given user.
 * This class will be responsible for creating the homeplanet too and will
 * store both in the DB.
 */
@Named
@ApplicationScoped
public class GameAccountCreator {
    
    @Inject
    private ExceptionHandler exceptionHandler;
    
    @Inject
    private Game game;
    
    @Inject
    private GameAccountDataAccessObject gameAccountDAO;
    
    @Inject
    private PlanetDataAccessObject planetDAO;
    
    @Inject
    private UniverseHandler universeHandler;
    
    public Optional<GameAccount> createGameAccount(User user, String displayName) {
        try {
            Optional<GameAccount> prevAccount = gameAccountDAO.findAccount(user.getUsername());
            if(prevAccount.isPresent()) {
                return prevAccount;
            }
            
            final Planet planet = new Planet();
            
            final GameAccount account = new GameAccount();
            
            account.setDisplayName(displayName);
            account.setOwnerId(user);
            
            planet.setOwner(account.getOwnerId());
            planet.onload(game.getContext());
            planetDAO.storePlanet(planet); //Get id
            
            account.setHomePlanetId(planet.getPlanetId());
            
            universeHandler.attachNewPlanet(planet);
            planet.setName(getDefaultPlanetNameFor(planet));
            
            planetDAO.updatePlanet(planet); // we needed the id for insert, but after that we changed the planet again so we need to update.
            gameAccountDAO.storeAccount(account);
            
            return Optional.of(account);
            
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return Optional.empty();
        }
    }
    
    private String getDefaultPlanetNameFor(Planet p) {
        return String.format("Planet [%s] of Emporer '%s'", p.getPosition(), p.getOwnerId());
    }
    
}
