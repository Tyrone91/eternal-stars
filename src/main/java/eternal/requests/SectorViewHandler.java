package eternal.requests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.control.GameAccount;
import eternal.game.control.PlanetHandler;
import eternal.game.control.UniverseHandler;
import eternal.game.environment.Planet;
import eternal.game.environment.Sector;
import eternal.game.environment.Universe;
import eternal.mangement.UserHandler;
import eternal.persistence.GameAccountDataAccessObject;
import eternal.persistence.UniverseDataAccessObject;
import eternal.session.SessionContext;
import eternal.user.User;

@Named
@SessionScoped
public class SectorViewHandler implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private SessionContext sessionContext;
    
    @Inject
    private UniverseHandler universeHandler;
    
    @Inject
    private UniverseDataAccessObject universeDAO;
    
    @Inject
    private PlanetHandler planetHandler;
    
    @Inject
    private GameAccountDataAccessObject accountDAO;
    
    @Inject
    private UserHandler userHandler;
    
    private GameAccount account;
    
    private int selectedIndex = 0;
    
    private List<IndexSectorTuple> sortedSectors = Collections.emptyList();
    
    private void sortSectors() {
        Universe u = universeHandler.getCurrentUniverse();
        if(u.getSectors().isEmpty()) {
            return;
        }
        
        Sector start = u.getSectors().get(0);
        while(start.getPrevSector().isPresent() ) {
            start = start.getPrevSector().get();
        }
        
        sortedSectors = new ArrayList<>();
        
        int i = 0;
        Sector current = start;
        while(current != null) {
            final IndexSectorTuple tuple = new IndexSectorTuple();
            tuple.setSector(current);
            tuple.setIndex(i++);
            sortedSectors.add(tuple);
            current = current.getNextSector().orElse(null);
        }
    }
    
    @PostConstruct
    public void init() {
        if(sessionContext.getUser().getGameAccount().isPresent()) {            
            account = sessionContext.getUser().getGameAccount().get();
            sortSectors();
            Planet home = planetHandler.findPlanet(account.getHomePlanetId()).get();
            selectedIndex = sortedSectors.stream()
                    .filter( tuple -> tuple.sector.getId() == home.getSector())
                    .map(IndexSectorTuple::getIndex)
                    .findAny().orElse(0);
        }
    }
    
    public List<IndexSectorTuple> getSortedSecotrs() {
        return this.sortedSectors;
    }
    
    public IndexSectorTuple getSelectedSector() {
        return sortedSectors.get(selectedIndex);
    }
    
    public void selectIndex(int index) {
        selectedIndex = index;
    }
    
    public void goNext() {
        if(selectedIndex + 1 >= sortedSectors.size() ) {
            return;
        }
        ++selectedIndex;
    }
    
    public void goPrev() {
        if( selectedIndex - 1 < 0) {
            return;
        }
        --selectedIndex;
    }
    
    public boolean isSelected(int index) {
        return selectedIndex == index;
    }
    
    public boolean isNotSelected(int index) {
        return !isSelected(index);
    }
    
    public List<Planet> getPlanetsOfSelectedSector() {
        List<Planet> planets = getSelectedSector().getSector().getPlanets();
        planets.sort( (p1,p2) -> Integer.compare(p1.getPosition(), p2.getPosition()) );
        return planets;
    }
    
    public String getOwnerNameOfPlanet(Planet p) {
        return accountDAO
            .findAccount(p.getOwnerId())
            .map(GameAccount::getDisplayName)
            .orElse(p.getOwnerId());
    }
    
    public class IndexSectorTuple {
        
        private int index;
        private Sector sector;
        
        public Sector getSector() {
            return sector;
        }
        public void setSector(Sector sector) {
            this.sector = sector;
        }
        
        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
    }
    
    public User getUserOfPlanet(Planet p) {
        return userHandler.find(p.getOwnerId()).get();
    }

}
