package eternal.core;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.dev.DemoInit;

@Named
@ApplicationScoped
public class EternalStarApp implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    DemoInit demo;
    
    private Game game;

    public EternalStarApp() {}
   
    
    @PostConstruct
    public void init() {
        demo.createDemoGame();
    }
    
    public String getName() {
        return "Eternal Stars";
    }
    
    public String getVersion() {
        return "0.0.1";
    }
}
