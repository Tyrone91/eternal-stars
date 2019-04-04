package eternal.mangement;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.control.GameAccount;
import eternal.persistence.GameAccountDataAccessObject;
import eternal.user.User;

@Named
@ApplicationScoped
public class GameAccountHandler {
    
    @Inject
    private GameAccountDataAccessObject accountDAO;
    
    public Optional<GameAccount> findAccount(final User owner) {
        return findAccount(owner.getUsername());
    }
    
    public Optional<GameAccount> findAccount(final String id) {
        return accountDAO.findAccount(id);
    }
    
    public boolean removeAccount(final GameAccount account) {
        return accountDAO.deleteAccount(account.getOwnerId());
    }
    
    public boolean updateAccount(final GameAccount account) {
        return accountDAO.updateAccount(account);
    }
}
