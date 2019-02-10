package eternal.game;

public interface Resources {
    
    public Resource getEnergy();
    public Resource getMetal();
    public Resource getCrystal();
    
    public void add(Resources res);
    public void sub(Resources res);
    
    public boolean isPayable(Resources cost);
    
}
