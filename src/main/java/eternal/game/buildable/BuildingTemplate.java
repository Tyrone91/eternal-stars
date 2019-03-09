package eternal.game.buildable;

import java.util.Optional;
import java.util.function.BiConsumer;

import eternal.core.Game;
import eternal.core.GameLoop;
import eternal.game.Resources;
import eternal.util.Equals;

public class BuildingTemplate {
    
    private final int id;
    private final String name;
    private final String description;
    private final Resources buildingCost;
    private final BiConsumer<Game, Building> onUpdate;
    private final Optional<Resources> gainPerMinute;
    private final int startLevel;
    
    public BuildingTemplate(int id, int startLevel, String name, String description, Resources cost, BiConsumer<Game, Building> onupdate, Resources gain) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.buildingCost = cost;
        this.onUpdate = onupdate;
        this.gainPerMinute = Optional.ofNullable(gain);
        this.startLevel = startLevel;
    }
    
    public BuildingTemplate(int id, String name, String description, Resources cost, BiConsumer<Game, Building> onupdate) {
        this(id, 1, name, description, cost, onupdate, null);
    }
    
    public BuildingTemplate(int id, String name, String description, Resources cost, BiConsumer<Game, Building> onupdate, Resources gain) {
        this(id, 1, name, description, cost, onupdate, gain);
    }
    
    public void update(Game g, Building building) {
        this.onUpdate.accept(g, building);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Resources getBuildingCost() {
        return buildingCost;
    }
    
    public Resources getEstimatedGainPerMinute() {
        if(gainPerMinute.isPresent()) {
            return gainPerMinute.get();
        } else {
            return new Resources(0,0,0);
        }
    }
    
    public int getStartLevel() {
        return this.startLevel;
    }
    
    public Resources getEstimatedGainPerMinute(Building source, int targetLevel) {
        Resources res = mult(targetLevel, getEstimatedGainPerMinute());
        return res;
    }
    
    public Resources getEstimatedGainPerMinute(Building source) {
        Resources res = mult(source.getLevel(), getEstimatedGainPerMinute());
        return res;
    }
    
    private static Resources of(long metal,long crystal, long energy) {
        return new Resources(metal, crystal, energy);
    }
    
    private static Resources mult(int level, Resources base) {
        final double modifier = 0.80;
        
        long metal = Math.round(base.getMetalAmount() * Math.pow(level, modifier));
        long crystal = Math.round(base.getCrystalAmount() * Math.pow(level, modifier));
        long energy = Math.round(base.getEnergyAmount() * Math.pow(level, modifier));
        return new Resources(metal, crystal, energy);
    }
    
    private static Resources mult(Resources res, double val) {
        return new Resources(
                Math.round(res.getMetalAmount() * val),
                Math.round(res.getCrystalAmount() * val),
                Math.round(res.getEnergyAmount() * val)
                );
    }
    
    private static final double MINUTE_IN_MS = 60 * 1000;
    private static Resources perSecond(Game g, Building b, Resources base) {
        //double elapsedTime = g.getGameLoop().getElapsedTime();
        double acc = g.getGameLoop().getElapsedTimeAccumulated();
        if(acc >= GameLoop.ACCUMULATED_ELAPSED_TIME_THRESHOLD) {
            double f = acc / 1000d;
            final Resources rate = mult(b.getLevel(), base);
            return mult(rate, f);
        }
        return new Resources(0,0,0);
        
    }
    
    @Override
    public boolean equals(Object obj) {
        return Equals
                .isNotNull(obj)
                .isInstance(BuildingTemplate.class)
                .checkIf( (b1, b2) -> b1.id == b2.id )
                .isEqualTo(this);
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }
    
    public final static BuildingTemplate METAL_MINE = new BuildingTemplate(
            0,
            "Metal Mine", "A mine used to collect minerals",
            of(250, 75, 120),
            (g, b) -> b.getOwner().getPlanetResources().add( perSecond(g, b, of(5, 0, 0) )),
            new Resources(60 * 5, 0, 0)
            );
    
    public final static BuildingTemplate CRYSTAL_MINE = new BuildingTemplate(
            1,
            "Crystal Mine", "A mine used to collect minerals",
            of(300, 250, 200),
            (g, b) -> b.getOwner().getPlanetResources().add( perSecond(g, b, of(0, 3, 0) )),
            new Resources( 0, 3 * 60, 0)
            );
    
    public final static BuildingTemplate ENERGY_GENERATOR = new BuildingTemplate(
            2,
            "Energy Generator", "A mine used to collect minerals",
            of(280, 250, 0),
            (g, b) -> b.getOwner().getPlanetResources().add( perSecond(g, b, of(0, 0, 4) )),
            new Resources(0,0, 4 * 60)
            );
    
    public final static BuildingTemplate DEEP_MINE = new BuildingTemplate(
            3,
            0,
            "Deep-Core Mine", "A mine used to collect minerals",
            of(5000, 6500, 10000),
            (g, b) -> b.getOwner().getPlanetResources().add( perSecond(g, b, of(50, 40, 5) )),
            new Resources(50 * 60, 40 * 60, 5 * 60)
            );
    
    
}
