package eternal.persistence.data;

import javax.persistence.Embeddable;

import eternal.game.Resources;

@Embeddable
public class ResourceTO {
    
    private long metal;
    private long crystal;
    private long energy;
    
    public long getMetal() {
        return metal;
    }
    
    public void setMetal(long metal) {
        this.metal = metal;
    }
    
    public long getCrystal() {
        return crystal;
    }
    
    public void setCrystal(long crystal) {
        this.crystal = crystal;
    }
    
    public long getEnergy() {
        return energy;
    }
    
    public void setEnergy(long energy) {
        this.energy = energy;
    }
    
    public ResourceTO updateFrom(Resources res) {
        this.setCrystal(res.getCrystalAmount());
        this.setMetal(res.getMetalAmount());
        this.setEnergy(res.getEnergyAmount());
        return this;
    }
    
}
    
