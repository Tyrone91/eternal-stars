package eternal.persistence;

import java.util.Optional;

import eternal.game.control.GameAccount;

public interface GameAccountDataAccessObject {
    
    public Optional<GameAccount> findAccount(String user);
    public boolean storeAccount(GameAccount account);
    public boolean updateAccount(GameAccount account);
    public boolean deleteAccount(String account);
}
