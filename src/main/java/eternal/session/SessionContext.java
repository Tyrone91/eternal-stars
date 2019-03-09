package eternal.session;

import java.io.Serializable;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.core.GameContext;
import eternal.persistence.GameAccountDataAccessObject;
import eternal.user.AnonymousUser;
import eternal.user.User;

@Named
@SessionScoped
public class SessionContext implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    GameAccountDataAccessObject gameAccountDOA;
    
    @Inject
    private AnonymousUser anonymousUser;
    
    @Inject
    private GameContext gameContext;
    
    private Optional<User> user;
    
    @PostConstruct
    public void init() {
        user = Optional.empty();
    }
    
    @PreDestroy
    public void destroyed() {
        this.user = Optional.empty();
    }
    
    public SessionContext() {}
    
    public User getUser() {
        return this.user.orElse(anonymousUser);
    }
    
    public void setUser(User user) {
        this.user = Optional.ofNullable(user);
        this.user
            .map( u -> gameAccountDOA.findAccount(u.getUsername()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .ifPresent( account -> {
                this.user.get().setGameAccount(account);
                account.onload(gameContext);
            });
            
    }
    
    public boolean isLoggedIn() {
        return user.isPresent();
    }

}
