package eternal.session.interaction;

import java.io.Serializable;

import javax.inject.Inject;

import eternal.session.SessionContext;
import eternal.session.ViewControl;
import eternal.user.User;

public class BaseInteractionHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    protected SessionContext sessionContext;
    
    @Inject
    protected ViewControl viewControl;
    
    protected User user() {
        return this.sessionContext.getUser();
    }
}
