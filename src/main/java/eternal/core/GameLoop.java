package eternal.core;

import java.util.Set;

import javax.inject.Inject;

import eternal.util.ExceptionHandler;

public class GameLoop {
    
    @Inject
    private ExceptionHandler exceptionHandler;
    
    private Set<Updatable> updatableObjects;
    private Set<Updatable> objectsToRemove;
    private Set<Updatable> objectsToAdd;
    
    private double elapsedTime;
    private long lastTime;
    
    public GameLoop() {
        
    }
    
    public void addObject(Updatable obj) {
        this.objectsToAdd.add(obj);
    }
    
    public void removeObject(Updatable obj) {
        this.objectsToRemove.add(obj);
    }
    
    public void update() {
        lastTime = System.currentTimeMillis();
        
        objectsToRemove.forEach(updatableObjects::remove);
        objectsToAdd.forEach(updatableObjects::add);
        
        for(Updatable u : updatableObjects) {
            u.update();
        }
    }
    
    public void sleep(double targetFps) {
        elapsedTime = System.currentTimeMillis() - lastTime;
        double timeToWait = (1000f / targetFps);
        if(elapsedTime < timeToWait) {
            sleep(Math.round(timeToWait - elapsedTime));
            elapsedTime = System.currentTimeMillis() - lastTime;
        }
    }
    
    
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch(Exception e) {
            exceptionHandler.handleException(e);
        }
    }
    
    public static interface Updatable {
        public void update();
    }
}
