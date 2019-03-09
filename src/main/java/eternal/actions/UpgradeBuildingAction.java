package eternal.actions;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.buildable.Building;
import eternal.game.control.PlanetHandler;
import eternal.game.environment.Planet;
import eternal.requests.RequestResponse;
import eternal.user.User;
import eternal.user.UserRight;

/**
 * Allows the upgrade of a building.
 */
@Named
@SessionScoped
public class UpgradeBuildingAction extends AbstractAction<Boolean, Building> {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private RequestResponse requestResponse;
    
    @Inject
    private PlanetHandler planetHandler;

    @Override
    public UserRight getNeededRight() {
        return UserRight.BUILDING;
    }

    @Override
    protected Boolean action(User user, Building... args) {
        final Building target = args[0];
        
        if(!user.getGameAccount().isPresent()) {
            error("No GameAccount found");
            return false;
        }
        
        Planet planet = target.getOwner();
        
        if(!planet.getPlanetResources().isPayable(target.getCost())) {
            requestResponse.setMessage("Not enough resources to upgrade building");
            return false;
        }
        
        boolean res = target.upgrade();
        
        if(!res) {
            requestResponse.setMessage("Could not upgrade building");
            return false;
        }
        
        planet.getPlanetResources().sub(target.getCost());
        
        planetHandler.updatePlanet(planet);
        requestResponse.setMessage("Upgrading building");
        return true;
    }

}
