package eternal.core;

import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.dev.DemoInit;
import eternal.game.control.PlanetHandler;
import eternal.game.control.UniverseHandler;
import eternal.game.environment.Sector;
import eternal.game.environment.Universe;

@Named
@ApplicationScoped
public class EternalStarApp implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private DemoInit demo;
    
    @Inject
    private UniverseHandler universeHandler;
    
    @Inject
    private PlanetHandler planetHandler;
    
    private Game game;

    public EternalStarApp() {}
   
    
    @PostConstruct
    public void init() {
        Universe u = universeHandler.getCurrentUniverse();
        universeHandler.initUniverse(u);
        int size = u.getSectors().stream()
            .map(Sector::getPlanets)
            .flatMap(Stream::of)
            .collect(Collectors.toList()).size();
        System.out.println("loaded " + size + " planets");
        
        demo.createDemoGame();
    }
    
    public String getName() {
        return "Eternal Stars";
    }
    
    public String getVersion() {
        return "0.0.1";
    }
}
