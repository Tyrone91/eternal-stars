package eternal.persistence;

import java.util.List;
import java.util.Optional;

import eternal.game.environment.Sector;
import eternal.game.environment.Universe;

public interface UniverseDataAccessObject {
    
    public Optional<Universe> findUniverse(int id);
    public Optional<Sector> findSector(int id);
    
    public boolean storeUniverse(Universe uni);
    public boolean storeSector(Sector sector);
    
    public boolean updateuniverse(Universe uni);
    public boolean updateSector(Sector sector);
    
    public boolean deleteSector(int sectorId);
    public List<Universe> loadAll();
}
