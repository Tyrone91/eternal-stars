package eternal.actions;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.mangement.UserHandler;
import eternal.requests.EditEmailRequest;
import eternal.requests.RequestResponse;
import eternal.user.User;
import eternal.user.UserRight;

/**
 * This action allows a user to change his own email.
 */
@Named
@SessionScoped
public class EditOwnEmailAction extends AbstractAction<Boolean, EditEmailRequest> implements Serializable {
    
    @Inject
    private RequestResponse response;
    
    @Inject
    private UserHandler userHandler;

    private static final long serialVersionUID = 1L;

    @Override
    public UserRight getNeededRight() {
        return UserRight.EDIT_OWN_ACCOUNT;
    }

    @Override
    protected Boolean action(User user, EditEmailRequest... args) {
        EditEmailRequest request = args[0];
        user.setEmail(request.getEmail());
        userHandler.updateUser(user);
        response.setMessage("Email updated");
        return true;
    }

}
