package eternal.core;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.util.ExceptionHandler;

@Named
@ApplicationScoped
public class GameLoop {
    
    public static final int ACCUMULATED_ELAPSED_TIME_THRESHOLD = 1000; 
    
    @Inject
    private ExceptionHandler exceptionHandler;
    
    private Set<Updatable> updatableObjects = new HashSet<>();
    private Set<Updatable> objectsToRemove = new HashSet<>();
    private Set<Updatable> objectsToAdd = new HashSet<>();
    
    private double elapsedTime;
    private double accElapsedTime;
    private long lastTime;
    private boolean resetAccElapsedTime = false;
    
    public GameLoop() {
        
    }
    
    public synchronized void addObject(Updatable obj) {
        this.objectsToAdd.add(obj);
    }
    
    public synchronized void removeObject(Updatable obj) {
        this.objectsToRemove.add(obj);
    }
    
    public void update(Game g) {
        lastTime = System.currentTimeMillis();
        
        synchronized (objectsToRemove) {            
            objectsToRemove.forEach(updatableObjects::remove);
        }
        synchronized (objectsToAdd) {            
            objectsToAdd.forEach(updatableObjects::add);
        }
        
        for(Updatable u : updatableObjects) {
            u.update(g);
        }
    }
    
    public void sleep(double targetFps) {
        elapsedTime = System.currentTimeMillis() - lastTime;
        double timeToWait = (1000f / targetFps);
        if(elapsedTime < timeToWait) {
            sleep(Math.round(timeToWait - elapsedTime));
            elapsedTime = System.currentTimeMillis() - lastTime;
        }
        if(resetAccElapsedTime) {
            accElapsedTime = 0;
            resetAccElapsedTime = false;
        }
        accElapsedTime += elapsedTime;
        if(accElapsedTime > ACCUMULATED_ELAPSED_TIME_THRESHOLD) {
            resetAccElapsedTime = true;
        }
    }
    
    public double getElapsedTime() {
        return this.elapsedTime;
    }
    
    public double getElapsedTimeAccumulated() {
        return accElapsedTime;
    }
    
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch(Exception e) {
            exceptionHandler.handleException(e);
        }
    }
    
    public static interface Updatable {
        public void update(Game g);
    }
}
