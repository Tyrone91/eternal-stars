package eternal.session;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import eternal.game.environment.Sector;
import eternal.game.environment.Universe;

@Named
@SessionScoped
public class UniverseEditHolder implements Serializable {
    
    private static final long serialVersionUID = 1L;

    
    private Universe selectedUniverse;
    
    private Sector selectedSector;

    public Universe getSelectedUniverse() {
        return selectedUniverse;
    }

    public void setSelectedUniverse(Universe selection) {
        this.selectedUniverse = selection;
    }

    public Sector getSelectedSector() {
        return selectedSector;
    }

    public void setSelectedSector(Sector selectedSector) {
        this.selectedSector = selectedSector;
    }
}
