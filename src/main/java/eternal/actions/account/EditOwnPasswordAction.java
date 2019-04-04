package eternal.actions.account;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.AbstractAction;
import eternal.mangement.UserHandler;
import eternal.requests.EditPasswordRequest;
import eternal.requests.RequestResponse;
import eternal.user.User;
import eternal.user.UserRight;

/**
 * This allows the user to change his own password.
 */
@Named
@SessionScoped
public class EditOwnPasswordAction extends AbstractAction<Boolean, EditPasswordRequest> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private RequestResponse response;
    
    @Inject
    private UserHandler userHandler;

    @Override
    public UserRight getNeededRight() {
        return UserRight.EDIT_OWN_ACCOUNT;
    }

    @Override
    protected Boolean action(User user, EditPasswordRequest... args) {
        EditPasswordRequest request = args[0];
        
        if(!request.getOldPassword().equals(user.getPassword())) {
            error("Wrong password provided");
            return false;
        }
        if(request.getNewPassword().isEmpty()) {
            error("No new password provided");
            return false;
        }
        
        if(!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            error("Provided passwords are not equal");
            return false;
        }
        
        user.setPassword(request.getNewPassword());
        userHandler.updateUser(user);
        response.setMessage("Password updated");
        return true;
    }

}
