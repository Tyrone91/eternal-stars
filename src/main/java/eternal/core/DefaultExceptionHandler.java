package eternal.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import eternal.util.ExceptionHandler;

@Named
@ApplicationScoped
public class DefaultExceptionHandler implements ExceptionHandler {
    
    private File log = new File("c:/temp/eternal-logs/exception.log");
    
    @PostConstruct
    public void init() {
        if(!log.exists()) {
            try {                
                log.createNewFile();
            } catch(Exception e) {
                //e.printStackTrace(System.err);
            }
        }
    }
    
    @Override
    public void handleException(Exception ex) {
        try (FileOutputStream os = new FileOutputStream(log, true)) {
            ex.printStackTrace(System.err);
            ex.printStackTrace( new PrintStream(os));
        } catch(Exception e) {
            //e.printStackTrace(System.err);
        }
        //System.exit(1);
    }

}
