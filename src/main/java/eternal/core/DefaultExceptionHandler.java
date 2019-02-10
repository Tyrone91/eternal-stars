package eternal.core;

import javax.inject.Named;

import eternal.util.ExceptionHandler;

@Named 
public class DefaultExceptionHandler implements ExceptionHandler {

    @Override
    public void handleException(Exception e) {
        e.printStackTrace(System.err); //TODO: use real logging
    }

}
