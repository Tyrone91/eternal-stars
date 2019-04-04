package eternal.game.resources;

import eternal.game.Resource;

public class ResourceSpecialization<T extends Resource> extends Resource {
    
    protected ResourceSpecialization(long initial) {
        super(initial);
    }
    
    public boolean isLessThen(ResourceSpecialization<T> res) {
        return super.isLessThen(res);
    }
    
    public boolean isMoreThen(ResourceSpecialization<T> res) {
        return super.isMoreThen(res);
    }
    
    public boolean isEqualTo(ResourceSpecialization<T> res) {
        return super.isEqualTo(res);
    }
    
    public boolean isMoreOrEqualThen(ResourceSpecialization<T> res) {
        return super.isMoreOrEqualThen(res);
    }
    
    public boolean isLessOrEqualThen(ResourceSpecialization<T> res) {
        return super.isLessOrEqualThen(res);
    }
    
    
    public ResourceSpecialization<T> sub(ResourceSpecialization<T> res) {
        super.sub(res.val());
        return this;
    }
    
    public ResourceSpecialization<T> add(ResourceSpecialization<T> res) {
        super.add(res.val());
        return this;
    }
}
