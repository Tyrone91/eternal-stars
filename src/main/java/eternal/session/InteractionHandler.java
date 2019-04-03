package eternal.session;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.DeleteAnyUserAction;
import eternal.actions.EditOwnEmailAction;
import eternal.actions.EditOwnNicknameAction;
import eternal.actions.EditOwnPasswordAction;
import eternal.actions.EditRolesAction;
import eternal.actions.GetUserListAction;
import eternal.actions.GetUserRightsAction;
import eternal.actions.GetUserRolesListAction;
import eternal.actions.LoginAction;
import eternal.actions.LogoutAction;
import eternal.actions.RegistrationAction;
import eternal.actions.SendTradeOfferAction;
import eternal.actions.UpgradeBuildingAction;
import eternal.actions.ViewUniversesAction;
import eternal.game.buildable.Building;
import eternal.game.control.GameAccountCreator;
import eternal.game.environment.Universe;
import eternal.requests.EditEmailRequest;
import eternal.requests.EditNicknameRequest;
import eternal.requests.EditPasswordRequest;
import eternal.requests.EditUserRoleRequest;
import eternal.requests.GameAccountRegistrationRequest;
import eternal.requests.LoginRequest;
import eternal.requests.RegistrationRequest;
import eternal.requests.SendTradeOfferRequest;
import eternal.user.User;
import eternal.user.UserRight;
import eternal.user.UserRole;

/**
 * Main class for all interactions between a user and the application.
 * All actions a user can do should go over this class.
 * This class is the main reason that the {@link UserRight}-System works and no critical
 * informations to unauthorized user. To keep this no direct call the handler Objects or DB should be make from
 * JSF.
 */
@Named
@SessionScoped
public class InteractionHandler implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private ViewControl viewControl;
    
    @Inject
    private GameAccountCreator gameAccountCreator;

    @Inject
    private SessionContext sessionContext;
    
    @Inject
    private LoginRequest loginRequest;
    
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
    
    @Inject
    private EditOwnEmailAction editOwnEmailAction;
    
    @Inject
    private EditOwnNicknameAction editOwnNicknameAction;
    
    @Inject
    private EditOwnPasswordAction editOwnPasswordAction;
    
    @Inject
    private UpgradeBuildingAction upgradeBuildingAction;
    
    @Inject
    private SendTradeOfferAction sendTradeOfferAction;
    
    public Optional<User> login(LoginRequest request) {
        return loginAction.performAction(sessionContext.getUser(), request);
    }
    
    public String loginWithRedirect(LoginRequest request) {
        Optional<User> user = login(request);
        if(user.isPresent()) {
            if(user.get().getGameAccount().isPresent()) {
                return viewControl.pushPage("/game.xhtml");
            } else {
                return viewControl.showAdminPage(); // if the user has no GameAccount there is not much sense to go the game. If the user has not the authorization to see the admin stuff he will see a mostly empty side.
            }
            
        } else {
            return "";
        }
    }
    
    public void logout() {
        logoutAction.performAction(sessionContext.getUser());
    }
    
    public String logoutWithRedirect() {
        logout();
        return "/login.xhtml?faces-redirect=true";
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
    
    /**
     * Do a normal registration and then log the user in.
     * @param request
     * @return
     */
    public String registerWithGameAccountAndForward(GameAccountRegistrationRequest request) {
        Optional<User> user = registerWithGameAccount(request);
        if(!user.isPresent()) {
            return "";
        }
        loginRequest.setPassword(user.get().getPassword());
        loginRequest.setUsername(user.get().getUsername());
        return loginWithRedirect(loginRequest) + "?faces-redirect=true";
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
    
    public boolean editOwnEmail(EditEmailRequest request) {
        return editOwnEmailAction.performAction(sessionContext.getUser(), request).orElse(false);
    }
    
    public boolean editOwnNickname(EditNicknameRequest request) {
        return editOwnNicknameAction.performAction(sessionContext.getUser(), request).orElse(false);
    }
    
    public boolean editOwnPassword(EditPasswordRequest request) {
        return editOwnPasswordAction.performAction(sessionContext.getUser(), request).orElse(false);
    }
    
    public boolean upgradeBuilding(Building building) {
        return upgradeBuildingAction.performAction(sessionContext.getUser(), building).orElse(false);
    }
    
    public boolean sendTradeOffer(SendTradeOfferRequest request) {
        return sendTradeOfferAction.performAction(sessionContext.getUser(), request).orElse(false);
    }
    
    public boolean sendTradeOffer(Optional<SendTradeOfferRequest> request) {
        if(!request.isPresent() ) {
            return false;
        }
        return sendTradeOffer(request.get());
    }
}
