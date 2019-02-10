package eternal.core;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class EternalStarApp {
    
    public EternalStarApp() {}
    
    @PostConstruct
    public void init() {
        System.out.println("Application: started");
    }
    
    public String getName() {
        return "Eternal Stars";
    }
    
    public String getVersion() {
        return "0.0.1";
    }
}
