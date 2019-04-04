package eternal.game;

import eternal.game.resources.Crystal;
import eternal.game.resources.Energy;
import eternal.game.resources.Metal;

public class Resources {
    
    private Crystal crystal = new Crystal();
    private Metal metal = new Metal();
    private Energy energy = new Energy();
    
    
    public Resources(long metal, long crystal, long energy) {
        this.metal = new Metal(metal);
        this.crystal = new Crystal(crystal);
        this.energy = new Energy(energy);
    }
    
    public Resources(Resources sources) {
        this(sources.getMetalAmount(), sources.getCrystalAmount(), sources.getEnergyAmount());
    }
    
    public Resources() {
        this(0,0,0);
    }

    public Metal getMetal() {
        return metal;
    }

    public Crystal getCrystal() {
        return crystal;
    }

    public Energy getEnergy() {
        return energy;
    }
    
    public long getEnergyAmount() {
        return this.energy.val();
    }
    
    public long getMetalAmount() {
        return this.metal.val();
    }
    
    public long getCrystalAmount() {
        return this.crystal.val();
    }
    
    public void add(Resources res) {
        this.metal = this.metal.add(res.getMetal());
        this.crystal = this.crystal.add(res.getCrystal());
        this.energy = this.energy.add(res.getEnergy());
    }

    public void sub(Resources res) {
        this.metal = this.metal.sub(res.getMetal());
        this.crystal = this.crystal.sub(res.getCrystal());
        this.energy = this.energy.sub(res.getEnergy());
    }

    public boolean isPayable(Resources res) {
        return this.metal.isMoreOrEqualThen(res.metal) &&
               this.crystal.isMoreOrEqualThen(res.crystal) &&
               this.energy.isMoreOrEqualThen(res.energy);
    }
}