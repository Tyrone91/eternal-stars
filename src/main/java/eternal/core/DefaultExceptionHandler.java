package eternal.core;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eternal.util.ExceptionHandler;

@Named
@ApplicationScoped
public class DefaultExceptionHandler implements ExceptionHandler {
    
//    private File log = new File("/logs/eternal-exception.txt");
    private final static Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    
    @PostConstruct
    public void init() {
    }
    
    @Override
    public void handleException(Exception ex) {
        try {
            ex.printStackTrace(System.err);
            LOGGER.error(ex);
        } catch(Exception e) {
            //e.printStackTrace(System.err);
        }
        //System.exit(1);
    }

}
