package eternal.game.resources;

import javax.persistence.Embeddable;

@Embeddable
public class Energy extends ResourceSpecialization<Energy> {

    public Energy(long initial) {
        super(initial);
    }
    
    public Energy() {
        this(0L);
    }
    
    public Energy(Energy energy) {
        this(energy.val());
    }
    
    @Override
    public Energy add(ResourceSpecialization<Energy> res) {
        super.add(res);
        return this;
    }
    
    @Override
    public Energy sub(ResourceSpecialization<Energy> res) {
        super.sub(res);
        return this;
    }
}
