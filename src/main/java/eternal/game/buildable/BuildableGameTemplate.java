package eternal.game.buildable;

import eternal.game.Resources;

/**
 * Base template that allows construction like buildings or ships.
 */
public interface BuildableGameTemplate {
    
    public String getName();
    public String getDescription();
    public Resources getCosts();
}
