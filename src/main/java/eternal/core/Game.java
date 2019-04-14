package eternal.core;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.control.PlanetHandler;
import eternal.game.control.UniverseHandler;
import eternal.game.environment.Planet;
import eternal.game.environment.Sector;

/**
 * Holder class for the {@link GameContext} and the {@link GameLoop}.
 * Runs the updates in a endless loop.
 */
@Named
@ApplicationScoped
public class Game implements Runnable {
    
    @Inject
    private GameContext gameContext;
    
    @Inject
    private GameLoop gameLoop;
    
    @Inject
    private PlanetHandler planetHandler;
    
    @Inject 
    private UniverseHandler universeHandler;
    
    private boolean shutdown = false;
    
    private double elapsedTime = 0;
    
    @PostConstruct
    public void init() {
        
        /*
        gameLoop.addObject(g -> {
            elapsedTime += g.getGameLoop().getElapsedTime();
            if(elapsedTime < 5000) {
                return;
            }
            elapsedTime = 0;
            saveAllPlanets();
        });
        */
        new Thread(this).start();
        
    }
    
    @Override
    public void run() {
        while(!shutdown) {
            gameLoop.update(this);
            gameLoop.sleep(60);
        }
    }
    
    public GameContext getContext() {
        return gameContext;
    }
    
    public GameLoop getGameLoop() {
        return gameLoop;
    }
    
    private void saveAllPlanets() {
        List<Sector> sectors = universeHandler.getCurrentUniverse().getSectors();
        for(Sector s : sectors) {
            for(Planet p : s.getPlanets()) {
                p.setName("Test Name");
                planetHandler.updatePlanet(p);
            }
        }
    }
    
}
