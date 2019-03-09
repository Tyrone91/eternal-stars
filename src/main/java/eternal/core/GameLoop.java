package eternal.core;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.util.ExceptionHandler;

/**
 * Abstraction for the main loop of the game.
 * The loop is designed to insert and remove objects without disturbing the current loop.
 * Objects will be removed before the next loop.
 * The loop tracks the time how long a cycle needed to complete and will sleep till the wanted FPS/UPS is achieved.
 */
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
    
    /**
     * Adds an object to the add list. Object will be inserted at the beginning of the next loop.
     * @param obj
     */
    public synchronized void addObject(Updatable obj) {
        this.objectsToAdd.add(obj);
    }
    
    /**
     * Adds an object to the remove list. Te object will be removed before the beginning of the next loop
     * @param obj
     */
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
    
    /**
     * Returns immediately if the target FPS/UPS is already reached.
     * If not the function will try to sleep till the target is reached and return then.
     * @param targetFps
     */
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
    
    /**
     * Returns the elapsed time between the last loop and the current one.
     * @return
     */
    public double getElapsedTime() {
        return this.elapsedTime;
    }
    
    /**
     * The elapsed time can the very small.
     * For 60 FPS/UPS 16ms. For some calculations this is to small.
     * The accumulated elapsed time counts till one second before it will be reseted 
     * and can be used by calculations that needed bigger numbers.
     * @return
     */
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
