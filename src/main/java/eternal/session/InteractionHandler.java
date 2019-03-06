package eternal.session;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.EditRolesAction;
import eternal.actions.GetUserListAction;
import eternal.actions.GetUserRightsAction;
import eternal.actions.GetUserRolesListAction;
import eternal.actions.LoginAction;
import eternal.actions.LogoutAction;
import eternal.actions.RegistrationAction;
import eternal.requests.EditUserRoleRequest;
import eternal.requests.LoginRequest;
import eternal.requests.RegistrationRequest;
import eternal.user.User;
import eternal.user.UserRight;
import eternal.user.UserRole;

@Named
@SessionScoped
public class InteractionHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private SessionContext sessionContext;
    
    @Inject
    private LoginAction loginAction;
    
    @Inject
    private GetUserListAction getUserListAction;
    
    @Inject
    private RegistrationAction registrationAction;
    
    @Inject
    private GetUserRolesListAction userRolesListAction;
    
    @Inject
    private GetUserRightsAction userRightsAction;
    
    @Inject
    private EditRolesAction editRolesAction;
    
    @Inject
    private LogoutAction logoutAction;
    
    public Optional<User> login(LoginRequest request) {
        return loginAction.performAction(sessionContext.getUser(), request);
    }
    
    public void logout() {
        logoutAction.performAction(sessionContext.getUser());
    }
    
    public Optional<User> register(RegistrationRequest request) {
        return registrationAction.performAction(sessionContext.getUser(), request);
    }
    
    public List<User> allRegisteredUser() {
        return getUserListAction.performAction(sessionContext.getUser()).orElseGet(Collections::emptyList);
    }
    
    public List<UserRole> allAvailableUserRoles() {
        return userRolesListAction.performAction(sessionContext.getUser()).orElseGet(Collections::emptyList);
    }
    
    public List<UserRight> allUserRights() {
        return userRightsAction.performAction(sessionContext.getUser()).orElseGet( () -> {
            //return Arrays.asList(UserRight.LOGIN, UserRight.CHAT);
            return Collections.emptyList();
        });
    }
    
    public boolean editUserRoleRights(EditUserRoleRequest request) {
        Optional<Boolean> res = editRolesAction.performAction(sessionContext.getUser(), request);
        return res.isPresent() && res.get();
    }
    
    
}
