package eternal.game.environment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import eternal.core.Game;
import eternal.core.GameLoop.Updatable;
import eternal.game.control.PlanetHandler;
import eternal.util.Equals;

/**
 * A Sector represents a local system with a limited number of players.
 * A Sector provides a better overview and creates a real space because Sectors a connected to only to two
 * more {@link Sector}s. This can be used to calculate a pseudo distance between planets. From position to position in the same 
 * system and from sector to sector in the universe.
 */
@Entity
public class Sector implements Updatable {
    
    private final static int PLANETS_PER_SECTOR = 5;
    
    private transient PlanetHandler planetHandler;
    
    private transient Universe parentUniverse;
    
    @Id
    @GeneratedValue
    @Column(name = "SECTOR_ID")
    private int sectorId;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<Integer> planetIds = new HashSet<>();
    
    @OneToOne
    private Sector nextSector;
    
    @OneToOne(mappedBy = "nextSector")
    private Sector prevSector;
    
    public boolean hasSpace() {
        return planetIds.size() < PLANETS_PER_SECTOR;
    }
    
    public boolean insert(Planet planet) {
        if(hasSpace() ) {
            planet.setSector(this.sectorId);
            planet.setPosition(getFreeSlot());
            return planetIds.add(planet.getPlanetId());
        }
        return false;
    }
    
    public boolean remove(Planet planet) {
        return planetIds.remove(planet.getPlanetId());
    }

    public Optional<Sector> getPrevSector() {
        return Optional.ofNullable(prevSector);
    }

    public void setPrevSector(Sector prevSector) {
        this.prevSector = prevSector;
    }

    public Optional<Sector> getNextSector() {
        return Optional.ofNullable(nextSector);
    }

    public void setNextSector(Sector nextSector) {
        this.nextSector = nextSector;
    }
    
    public int getId() {
        return this.sectorId;
    }
    
    public void onload(PlanetHandler handler, Universe parent) {
        this.planetHandler = handler;
        this.parentUniverse = parent;
        this.planetIds = new TreeSet<>(this.planetIds);
    }
    
    /**
     * Searches for a free slot in this Sector.
     * @return
     */
    private int getFreeSlot() {
        Set<Integer> used = new HashSet<>();
        this.planetIds.stream()
            .map(planetHandler::findPlanet)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .mapToInt(Planet::getPosition)
            .forEach( pos -> used.add(pos));
        
        for(int i = 0; i < PLANETS_PER_SECTOR; ++i) {
            if(!used.contains(i) ) {
                return i;
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public void update(Game g) {
        for(int id : planetIds) {
            planetHandler.findPlanet(id).ifPresent( p -> p.update(g));
        }
    }
    
    public int getPlanetCount() {
        return this.planetIds.size();
    }
    
    public List<Planet> getPlanets() {
        if(planetHandler == null) {
            return Collections.emptyList();
        }
        return this.planetIds.stream()
            .map(planetHandler::findPlanet)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }
    
    public boolean isEmpty() {
        return this.planetIds.isEmpty();
    }
    
    public boolean hasPlanet(int planetId) {
        return this.planetIds.contains(planetId);
    }
    
    public boolean hasPlanet(Planet p) {
        return hasPlanet(p.getPlanetId());
    }
    
    @Override
    public boolean equals(Object obj) {
        return Equals
                .isNotNull(obj)
                .isInstance(Sector.class)
                .checkIf( (s1,s2) -> s1.sectorId == s2.sectorId )
                .isEqualTo(this);
        //return super.equals(obj); //TODO: work on this.
    }
    
    @Override
    public int hashCode() {
       return this.sectorId;
       //return super.hashCode(); //TODO: work on this.
    }

}
