package eternal.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import eternal.game.Resources;
import eternal.persistence.data.ResourceTO;

@Named
@ApplicationScoped
public class ResourceFactory {
    
    public Resources build(ResourceTO res) {
        return new Resources(res.getMetal(), res.getCrystal(), res.getEnergy());
    }
    
    public ResourceTO build(Resources res) {
        final ResourceTO to = new ResourceTO();
        to.setMetal(res.getMetal().val());
        to.setCrystal(res.getCrystal().val());
        to.setEnergy(res.getEnergy().val());
        return to;
    }
}
