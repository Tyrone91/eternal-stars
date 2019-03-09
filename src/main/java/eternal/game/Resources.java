package eternal.game;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Resources {
    
    private static final String METAL_TYPE = "METAL_TYPE", CRYSTAL_TYPE = "CRYSTAL_TYPE", ENERGY_TYPE = "ENERGY_TYPE";
    
    @Id
    @GeneratedValue
    private int id;
    
    @JoinColumn
    @OneToOne(cascade = CascadeType.PERSIST)
    private Resource metal;
    
    @JoinColumn
    @OneToOne(cascade = CascadeType.PERSIST)
    private Resource crystal;
    
    @JoinColumn
    @OneToOne(cascade = CascadeType.PERSIST)
    private Resource energy;
    
    public Resources(long metal, long crystal, long energy) {
        this.metal = new Resource(METAL_TYPE, metal);
        this.crystal = new Resource(CRYSTAL_TYPE, crystal);
        this.energy = new Resource(ENERGY_TYPE, energy);
    }
    
    public Resources(Resources sources) {
        this(sources.getMetalAmount(), sources.getCrystalAmount(), sources.getEnergyAmount());
    }
    
    public Resources() {
        this(0,0,0);
    }

    public Resource getEnergy() {
        return energy;
    }

    public Resource getMetal() {
        return metal;
    }

    public Resource getCrystal() {
        return crystal;
    }
    
    public long getEnergyAmount() {
        return this.energy.getAmount();
    }
    
    public long getMetalAmount() {
        return this.metal.getAmount();
    }
    
    public long getCrystalAmount() {
        return this.crystal.getAmount();
    }
    
    public void add(Resources res) {
        this.metal = new Resource(this.metal).amount(res.getMetal());
        this.crystal = new Resource(this.crystal).amount(res.getCrystal());
        this.energy = new Resource(this.energy).amount(res.getEnergy());
    }

    public void sub(Resources res) {
        this.metal = new Resource(this.metal).amount(res.getMetal(), true);
        this.crystal = new Resource(this.crystal).amount(res.getCrystal(), true);
        this.energy = new Resource(this.energy).amount(res.getEnergy(), true);
    }

    public boolean isPayable(Resources res) {
        if(res.getMetal().getAmount() > this.metal.getAmount()) {
            return false;
        }
        
        if(res.getCrystal().getAmount() > this.crystal.getAmount()) {
            return false;
        }
        
        if(res.getEnergy().getAmount() > this.crystal.getAmount()) {
            return false;
        }
        
        return true;
    }
    
    
}