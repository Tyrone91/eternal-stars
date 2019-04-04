package eternal.game.resources;

public class Crystal extends ResourceSpecialization<Crystal> {

    public Crystal(long initial) {
        super(initial);
    }
    
    public Crystal() {
        super(0L);
    }
    
    public Crystal(Crystal source) {
        this(source.val());
    }
    
    @Override
    public Crystal add(ResourceSpecialization<Crystal> res) {
        super.add(res);
        return this;
    }
    
    @Override
    public Crystal sub(ResourceSpecialization<Crystal> res) {
        super.sub(res);
        return this;
    }

}
