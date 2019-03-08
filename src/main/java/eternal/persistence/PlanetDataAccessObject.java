package eternal.persistence;

import java.util.Optional;

import eternal.game.environment.Planet;

public interface PlanetDataAccessObject {
    
    public boolean storePlanet(Planet planet);
    public boolean updatePlanet(Planet planet);
    public Optional<Planet> findPlanet(int id);
    public boolean deletePlanet(int id);
}
