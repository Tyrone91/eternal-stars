package eternal.session;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.DeleteAnyUserAction;
import eternal.actions.EditRolesAction;
import eternal.actions.GetUserListAction;
import eternal.actions.GetUserRightsAction;
import eternal.actions.GetUserRolesListAction;
import eternal.actions.LoginAction;
import eternal.actions.LogoutAction;
import eternal.actions.RegistrationAction;
import eternal.actions.ViewUniversesAction;
import eternal.game.control.GameAccountCreator;
import eternal.game.environment.Universe;
import eternal.requests.EditUserRoleRequest;
import eternal.requests.GameAccountRegistrationRequest;
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
    private GameAccountCreator gameAccountCreator;

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
    
    @Inject
    private ViewUniversesAction viewUniversesAction;
    
    @Inject
    private DeleteAnyUserAction deleteAnyUserAction;
    
    public Optional<User> login(LoginRequest request) {
        return loginAction.performAction(sessionContext.getUser(), request);
    }
    
    public String loginWithRedirect(LoginRequest request) {
        Optional<User> user = login(request);
        if(user.isPresent()) {
            return "/game.xhtml";
        } else {
            return "";
        }
    }
    
    public void logout() {
        logoutAction.performAction(sessionContext.getUser());
    }
    
    public String logoutWithRedirect() {
        logout();
        return "/login.xhtml";
    }
    
    public Optional<User> register(RegistrationRequest request) {
        return registrationAction.performAction(sessionContext.getUser(), request);
    }
    
    public Optional<User> registerWithGameAccount(GameAccountRegistrationRequest request) {
        Optional<User> user = registrationAction.performAction(sessionContext.getUser(), request);
        if(user.isPresent()) {
            if(request.getDisplayName() == null || request.getDisplayName().isEmpty() ) {
                request.setDisplayName(request.getUsername());
            }
            gameAccountCreator.createGameAccount(user.get(), request.getDisplayName());
        }
        return user;
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
    
    public List<Universe> listUniverses() {
        return viewUniversesAction.performAction(sessionContext.getUser()).orElseGet(Collections::emptyList);
    }
    
    public boolean deleteAnyUser(User target) {
        return deleteAnyUserAction.performAction(sessionContext.getUser(), target).orElse(false);
    }
}
