package eternal.game.control;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.core.GameContext;
import eternal.game.environment.Planet;
import eternal.game.environment.Sector;
import eternal.game.environment.Universe;
import eternal.persistence.UniverseDataAccessObject;

@Named
@ApplicationScoped
public class UniverseHandler {
    
    @Inject
    private PlanetHandler planetHandler;
    
    @Inject
    private UniverseDataAccessObject universeDAO;
    
    @Inject
    private GameContext gameContext;
    
    private Universe currentUniverse;

    @Inject
    public void init() {
        this.currentUniverse = findOrCreateUniverse();
    }
    
    public synchronized Universe findOrCreateUniverse() {
        List<Universe> universes = universeDAO.loadAll();
        if(!universes.isEmpty()) {
            final Universe tmp =  universes.get(0);
            tmp.onload(planetHandler, gameContext, loadSectorOf(tmp));
            return tmp;
        }
        
        final Universe uni = new Universe();
        uni.onload(planetHandler,gameContext, loadSectorOf(uni));
        universeDAO.storeUniverse(uni);
        
        return uni;
    }
    
    public synchronized List<Universe> getUniverseList() {
        return universeDAO.loadAll();
    }
    
    public synchronized void initUniverse(Universe universe) {
        universe.onload(planetHandler, gameContext, loadSectorOf(universe));
    }
    
    public synchronized Universe getCurrentUniverse() {
        return currentUniverse;
    }
    
    public synchronized void setCurrentUniverse(Universe universe) {
        this.currentUniverse = universe;
    }
    
    public synchronized Optional<Sector> findSector(int id) {
        return this.currentUniverse.findSector(id);
    }
    
    public synchronized boolean updateUniverse(Universe uni) {
        return universeDAO.updateuniverse(uni);
    }
    
    public synchronized void attachNewPlanet(Planet p) {
        Sector target = this.currentUniverse.attachNewPlanet(p);
        if(target.getPlanetCount() == 1) { // this means this is an new sector otherwise it would have at least two planets.
            //this.universeDAO.storeSector(target);// TODO: holzhammer
            this.updateUniverse(currentUniverse);
        }
        this.updateSector(target);
        this.updateSector(target.getNextSector());
        this.updateSector(target.getPrevSector());
        
        
    }
    
    public boolean detachPlanet(Planet p) {
        Optional<Sector> affectedSector = this.currentUniverse.detachSector(p);
        if(!affectedSector.isPresent()) {
            return false;
        }
        
        if(affectedSector.get().isEmpty()) {
           this.removeSector(affectedSector.get());
        } else {            
            this.updateSector(affectedSector.get());
        }
        
        return false;
    }
    
    public synchronized boolean updateSector(Optional<Sector> sec) {
        if(sec.isPresent()) {
            return updateSector(sec.get());
        }
        return false;
    }
    
    public synchronized boolean updateSector(Sector sec) {
        //TODO: remove Holzhammer-Methode
        return universeDAO.updateSector(sec);
        //return universeDAO.updateuniverse(currentUniverse);
    }
    
    public synchronized boolean removeSector(Sector sec) {
        if(!sec.isEmpty()) {
            return false; // We don't delete a sector with planets. We would have to delete the planets too and if the delete the planets we have to delete the gameccount.
        }
        
        this.currentUniverse.removeSector(sec);
        
        sec.getNextSector().ifPresent( s -> s.setPrevSector(null));
        sec.getPrevSector().ifPresent(s -> s.setNextSector(null));
        
        this.updateSector(sec.getNextSector());
        this.updateSector(sec.getPrevSector());
        
        this.updateUniverse(currentUniverse);
      //TODO: remove Holzhammer-Methode && überprüfe auf Dateileiche.
        return universeDAO.deleteSector(sec.getId());
    }
    
    public Sector requestNewSector() {
        final Sector sector = new Sector();
        this.universeDAO.storeSector(sector);
        return sector;
    }
    
    private Set<Sector> loadSectorOf(Universe uni ) {
        return uni.getSectorIDs().stream()
                .map(universeDAO::findSector)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }
    
    
}
