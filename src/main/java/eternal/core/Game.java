package eternal.core;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
    
    private boolean shutdown = false;
    
    @PostConstruct
    public void init() {
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
    
}
