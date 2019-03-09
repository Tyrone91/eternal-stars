package eternal.actions;

import java.io.Serializable;
import java.util.Optional;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.control.GameAccount;
import eternal.persistence.GameAccountDataAccessObject;
import eternal.requests.EditNicknameRequest;
import eternal.requests.RequestResponse;
import eternal.user.User;
import eternal.user.UserRight;

@Named
@SessionScoped
public class EditOwnNicknameAction extends AbstractAction<Boolean, EditNicknameRequest> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private RequestResponse response;
    
    @Inject
    private GameAccountDataAccessObject gameAccountDAO;

    @Override
    public UserRight getNeededRight() {
        return UserRight.EDIT_OWN_ACCOUNT;
    }

    @Override
    protected Boolean action(User user, EditNicknameRequest... args) {
        EditNicknameRequest request = args[0];
        
        Optional<GameAccount> account = user.getGameAccount();
        if(!account.isPresent() ) {
            error("No GameAccount found");
            return false;
        }
        
        account.get().setDisplayName(request.getNickname());
        gameAccountDAO.updateAccount(account.get());
        response.setMessage("Nickname updated");
        return true;
    }

}
