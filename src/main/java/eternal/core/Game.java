package eternal.core;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Game {
    
    @Inject
    private GameContext gameContext;
    
    @Inject
    private GameLoop gameLoop;
    
    private boolean shutdown = false;
    
    @PostConstruct
    public void init() {
        
    }
    
    public void run() {
        while(!shutdown) {
            gameLoop.update(this);
            gameLoop.sleep(60);
        }
    }
    
    public GameContext getContext() {
        return gameContext;
    }
    
}
