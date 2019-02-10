package eternal.core;

public class Game {
    
    private GameLoop gameLoop;
    private boolean shutdown = false;
    
    public void run() {
        while(!shutdown) {
            gameLoop.update();
            gameLoop.sleep(60);
        }
    }
    
}
