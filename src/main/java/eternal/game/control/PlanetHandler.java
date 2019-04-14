package eternal.game.control;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.core.GameContext;
import eternal.game.environment.Planet;
import eternal.persistence.PlanetDataAccessObject;

/**
 * Management class to abstract the DB and the programm logic.
 * All Planet related stuff should go over this class.
 */
@Named
@ApplicationScoped
public class PlanetHandler {
    
    @Inject
    private PlanetDataAccessObject planetDAO;
    
    @Inject
    private GameContext context;
    
    private Map<Integer, Planet> cachedPlanets = new HashMap<>();
    
    @PostConstruct
    public void init() {
        
    }
    
    public boolean renamePlanet(int planetId, String name) {
        Optional<Planet> planet = findPlanet(planetId);
        if(!planet.isPresent()) {
            return false;
        }
        planet.get().setName(name);
        return planetDAO.updatePlanet(planet.get());
    }
    
    public Optional<Planet> findPlanet(int planetId) {
        Planet p = cachedPlanets.get(planetId);
        if( p != null) {
            return Optional.of(p);
        }
        
        Optional<Planet> res = this.planetDAO.findPlanet(planetId);
        res.ifPresent( pl -> cachedPlanets.put(planetId, pl));
        res.ifPresent( pl -> pl.onload(context));
        
        return res;
    }
    
    public void clearChache() {
        Collection<Planet> planets = cachedPlanets.values();
        cachedPlanets.clear();
        for(Planet p : planets) {
            updatePlanet(p);
        }
    }
    
    public boolean removePlanet(int planetId) {
        return this.planetDAO.deletePlanet(planetId);
    }
    
    public boolean updatePlanet(Planet p) {
        p.beforeSave();
        return planetDAO.updatePlanet(p);
    }
}
