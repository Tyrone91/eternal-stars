package eternal.game.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import eternal.core.Game;
import eternal.core.GameContext;
import eternal.core.GameLoop.Updatable;
import eternal.game.control.PlanetHandler;

@Entity
public class Universe implements Updatable {
    
    @Id
    @GeneratedValue
    private int id;
    
    private transient Set<Sector> sectors = new HashSet<>();
    
    private transient PlanetHandler planetHandler;
    
    private transient GameContext gameContext;
    
    private transient Map<Integer, Sector> fastAccessSectors = new HashMap<>();
    
    @ElementCollection
    private Set<Integer> sectorIds = new HashSet<>();

    @Override
    public synchronized void update(Game g) {
        for(Sector s : sectors) {
            s.update(g);
        }
    }
    
    private synchronized Sector createNewSector()  {
        Sector tmp = gameContext.getUniverseHandler().requestNewSector();
        sectors.add(tmp);
        sectorIds.add(tmp.getId());
        tmp.onload(planetHandler, this);
        return tmp;
    }
    
    
    public synchronized Sector attachNewPlanet(Planet p) {
        Sector last = getSectorZero();
        while(last.getNextSector().isPresent()) {
            last = last.getNextSector().get();
        }
        if(!last.hasSpace()) {
            final Sector tmp = createNewSector();
            tmp.setPrevSector(last);
            last.setNextSector(tmp);
            last = tmp;
        }
        p.setSector(this.id);
        last.insert(p);
        return last;
    }
    
    public synchronized Optional<Sector> detachSector(Planet planet) {
        int sectorId = planet.getSector();
        Optional<Sector> sector = findSector(sectorId);
        if(!sector.isPresent()) {
            return Optional.empty();
        }
        
        if(!sector.get().remove(planet)) {
            return Optional.empty();
        }
        return sector;
    }
    
    public void removeSector(Sector sector) {
        sectors.remove(sector);
        fastAccessSectors.remove(sector.getId());
        sectorIds.remove(sector.getId());
    }
    
    public synchronized void onload(PlanetHandler planetHandler, GameContext context, Set<Sector> sectors) {
        this.sectors = sectors;
        this.planetHandler = planetHandler;
        this.gameContext = context;
        for(Sector s:  sectors) {
            s.onload(planetHandler, this);
        }
    }
    
    public GameContext getGameContext() {
        return gameContext;
    }
    
    private synchronized Sector getSectorZero() {
        if(sectors.isEmpty()) {
            return createNewSector();
        }
        return sectors.stream().filter(s -> !s.getPrevSector().isPresent() ).findAny().get();
    }
    
    public synchronized Optional<Sector> findSector(int sectorId) {
        Sector prev = fastAccessSectors.get(sectorId);
        if(prev != null) {
            return Optional.of(prev);
        }
        
        Optional<Sector> res = this.sectors.stream().filter( sec -> sec.getId() == sectorId).findAny();
        if(res.isPresent()) {
            res.get().onload(planetHandler, this);
            fastAccessSectors.put(sectorId, res.get());
        }
        return res;
    }
    
    public int getUniverseId() {
        return id;
    }
    
    public synchronized int getSectorCount() {
        return sectors.size();
    }
    
    public synchronized int getPlanetCount() {
        return this.sectors.stream().mapToInt(Sector::getPlanetCount).sum();
    }
    
    public synchronized List<Sector> getSectors() {
        return new ArrayList<>(this.sectors);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        
        if( !(obj instanceof Universe) ) {
            return false;
        }
        
        return this.id == ((Universe)obj).id;
        /*
        return Equals
                .isNotNull(obj)
                .isInstance(Universe.class)
                .checkIf( (u1, u2) -> u1.id == u2.id)
                .isEqualTo(this);
         */
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }
    
    public Set<Integer> getSectorIDs() {
        return this.sectorIds;
    }

}
