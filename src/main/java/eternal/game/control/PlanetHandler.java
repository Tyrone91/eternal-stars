package eternal.game.control;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.environment.Planet;
import eternal.persistence.PlanetDataAccessObject;

@Named
@ApplicationScoped
public class PlanetHandler {
    
    @Inject
    private PlanetDataAccessObject planetDAO;
    
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
        return planetDAO.updatePlanet(p);
    }
}
