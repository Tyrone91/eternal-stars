package eternal.factories;

import javax.inject.Named;

import eternal.game.Resource;
import eternal.game.Resources;

@Named
public class ResourcesFactory {
    
    public Resources create() {
        return create(0);
    }
    
    public Resources create(int amout) {
        return create(amout, amout, amout);
    }
    
    public Resources create(int metal, int crystal, int energy) {
        return new ResourcesImpl(metal, crystal, energy);
    }
    
    private static class ResourcesImpl implements Resources {
        
        private static final String METAL_TYPE = "METAL_TYPE", CRYSTAL_TYPE = "CRYSTAL_TYPE", ENERGY_TYPE = "ENERGY_TYPE";
        
        private int id;
        
        private Resource metal;
        
        private Resource crystal;
        
        private Resource energy;
        
        public ResourcesImpl(long metal, long crystal, long energy) {
            this.metal = new SimpleResource(METAL_TYPE, metal);
            this.crystal = new SimpleResource(CRYSTAL_TYPE, crystal);
            this.energy = new SimpleResource(ENERGY_TYPE, energy);
        }

        @Override
        public Resource getEnergy() {
            return energy;
        }

        @Override
        public Resource getMetal() {
            return metal;
        }

        @Override
        public Resource getCrystal() {
            return crystal;
        }
        
        @Override
        public void add(Resources res) {
            this.metal = new SimpleResource(this.metal).amount(res.getMetal());
            this.crystal = new SimpleResource(this.crystal).amount(res.getCrystal());
            this.energy = new SimpleResource(this.energy).amount(res.getEnergy());
        }

        @Override
        public void sub(Resources res) {
            this.metal = new SimpleResource(this.metal).amount(res.getMetal());
            this.crystal = new SimpleResource(this.crystal).amount(res.getCrystal());
            this.energy = new SimpleResource(this.energy).amount(res.getEnergy());
        }

        @Override
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
        
        private static class SimpleResource implements Resource {
            
            private String type;
            private long amount;
            
            public SimpleResource() {}
            
            public SimpleResource(String type, long amount) {
                this.type = type;
                this.amount = amount;
            }
            
            public SimpleResource(Resource source) {
                this(source.getType(), source.getAmount());
            }
            
            @Override
            public String getType() {
                return type;
            }

            @Override
            public long getAmount() {
                return amount;
            }
            
            private SimpleResource amount(long inc) {
                if(Long.MAX_VALUE - inc < this.amount) {
                    this.amount = Long.MAX_VALUE;
                } else {                    
                    this.amount += inc;
                }
                if(amount < 0) {
                    amount = 0;
                }
                return this;
            }
            
            private SimpleResource amount(Resource inc) {
                return amount(inc, false);
             }
            
            private SimpleResource amount(Resource inc, boolean sub) {
                if(sub) {
                    return amount(-inc.getAmount());
                }
               return amount(inc.getAmount());
            }
            
        }
    }
}
