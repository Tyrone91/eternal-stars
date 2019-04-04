package eternal.game.resources;

import javax.persistence.Embeddable;

@Embeddable
public class Metal extends ResourceSpecialization<Metal> {

    public Metal(long initial) {
        super(initial);
    }
    
    public Metal() {
        this(0L);
    }
    
    public Metal(Metal source) {
        this(source.val());
    }
    
    @Override
    public Metal add(ResourceSpecialization<Metal> metal) {
        super.add(metal);
        return this;
    }
    
    @Override
    public Metal sub(ResourceSpecialization<Metal> metal) {
        super.sub(metal);
        return this;
    }
}
