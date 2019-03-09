package eternal.session;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.Resources;
import eternal.game.buildable.Building;
import eternal.game.control.GameAccount;
import eternal.game.control.PlanetHandler;
import eternal.game.environment.Planet;

/**
 * Helps the user the navigate in the application.
 * Contains the description for the main menu and allows the
 * setting of the main content via a public interface.
 * A ajax render main-content will still be needed to see the actual change.
 * The class provides some helper function to be used by EL in JSF.
 */
@Named
@SessionScoped
public class GameControls implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private SessionContext sessionContext;
    
    @Inject
    private PlanetHandler planetHandler;
    
    public final static Control OVERVIEW = new Control("Overview", "game-overview.xhtml");
    public final static Control GALAXIE = new Control("Galaxie", "game-sector-view.xhtml");
    public final static Control BUILDINGS = new Control("Buildings", "game-building.xhtml");
    public final static Control TRADE = new Control("Trade", "game-trade-offer.xhtml");
    public final static Control ACCOUNT = new Control("Account", "game-edit-account.xhtml");

    
    private static List<Control> ALL_CONTROLS = Arrays.asList(
            OVERVIEW,
            GALAXIE,
            BUILDINGS,
            TRADE,
            ACCOUNT);
    
    private Control currentFocus;
    
    private Planet planet;
    
    private GameAccount account;
    
    private boolean valid() {
        return planet != null && account != null;
    }
    
    private Optional<GameAccount> account() {
        return this.sessionContext.getUser().getGameAccount();
    }
    
    private Planet planet(GameAccount account) {
        return planetHandler.findPlanet(account.getHomePlanetId()).get();
    }
    
    @PostConstruct
    public void init() {
        currentFocus = ALL_CONTROLS.get(0);
        account = account().orElse(null);
        if(account != null) {
            planet = planet(account);
        }
    }
    
    public Control getCurrentFocus() {
        return currentFocus;
    }

    public void setCurrentFocus(Control currentFocus) {
        this.currentFocus = currentFocus;
    }
    
    public List<Control> getControls() {
        return ALL_CONTROLS;
    }
    
    public List<Building> getBuildings() {
        if(!valid()) {
            return Collections.emptyList();
        }
        return planet(account().get()).getBuildings();
    }
    
    public long getPlanetMetal() {
        if(!valid()) {
            return 0;
        }
        return planet.getPlanetResources().getMetal().getAmount();
    }
    
    public long getPlanetCrystal() {
        if(!valid()) {
            return 0;
        }
        return planet.getPlanetResources().getCrystal().getAmount();
    }
    
    public long getPlanetEnergy() {
        if(!valid()) {
            return 0;
        }
        return planet.getPlanetResources().getEnergy().getAmount();
    }
    
    public long getPlanetMetalGain() {
        if(!valid()) {
            return 0;
        }
        return planet.getGainPerMinute().getMetalAmount();
    }
    
    public long getPlanetCrystalGain() {
        if(!valid()) {
            return 0;
        }
        return planet.getGainPerMinute().getCrystalAmount();
    }
    
    public long getPlanetEnergyGain() {
        if(!valid()) {
            return 0;
        }
        return planet.getGainPerMinute().getEnergyAmount();
    }
    
    public Resources getResourceGainUpgradeDifference(Building building) {
        Resources res = new Resources(building.getGainPerMinuteAfterUpgrade());
        res.sub(building.getGainPerMinute());
        return res;
    }
    
    public boolean buildingHasResourceGain(Building b) {
        Resources after = b.getGainPerMinuteAfterUpgrade();
        return LongStream.of(after.getMetalAmount(), after.getCrystalAmount(), after.getEnergyAmount())
                .anyMatch( i -> i > 0);
    }

    public static class Control {
        
        private String label;
        private String outcome;
        
        public Control(String label, String outcome) {
            this.setLabel(label);
            this.setOutcome(outcome);
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getOutcome() {
            return outcome;
        }

        public void setOutcome(String outcome) {
            this.outcome = outcome;
        }
    }
}
