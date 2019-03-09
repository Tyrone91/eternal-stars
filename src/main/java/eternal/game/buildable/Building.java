package eternal.game.buildable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import eternal.core.Game;
import eternal.core.GameContext;
import eternal.core.GameLoop.Updatable;
import eternal.game.Resources;
import eternal.game.environment.Planet;

@Entity
public class Building implements Updatable {
    
    private transient BuildingTemplate template;
    
    @Id
    @GeneratedValue
    private int id;
    
    private int templateId;
    private int level = 1;
    
    @ManyToOne
    private Planet owner;
    
    public Building(BuildingTemplate template, Planet target) {
        this.template = template;
        this.templateId = template.getId();
        this.owner = target;
    }
    
    public Building() {}
    
    public void onload(GameContext context) {
        this.template = context.findBuildingTemplate(templateId).get();
    }
    
    public int getTemplateId() {
        return this.templateId;
    }
    
    public int getLevel() {
        return level;
    }

    @Override
    public synchronized void update(Game g) {
        template.update(g, this);
    }
    
    public String getName() {
        return template.getName() + " Lvl: " + this.level;
    }
    
    public String getDescription() {
        return template.getDescription();
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public Planet getOwner() {
        return this.owner;
    }
    
    public Resources getCost() {
        double modifer = Math.pow(level + 1, 0.85);
        long metal = Math.round(template.getBuildingCost().getMetal().getAmount() * modifer);
        long crystal = Math.round(template.getBuildingCost().getCrystal().getAmount() * modifer);
        long energy = Math.round(template.getBuildingCost().getEnergy().getAmount() * modifer);
        return new Resources(metal, crystal, energy);
    }
    
    public synchronized boolean upgrade() {
        if(!owner.getPlanetResources().isPayable(getCost())) {
            return false;
        }
        ++level; //TODO: add time
        return true;
    }
    
    public Resources getGainPerMinute() {
        return this.template.getEstimatedGainPerMinute(this);
    }
    
    public Resources getGainPerMinuteAfterUpgrade() {
        return this.template.getEstimatedGainPerMinute(this, level + 1);
    }
    
}
