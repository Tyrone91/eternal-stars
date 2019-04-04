package eternal.session;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.TradeOffer;
import eternal.user.User;

@Named
@SessionScoped
public class TradeOfferControl implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private SessionContext sessionContext;
    
    public void acceptTradeOffer(TradeOffer offer) {
        
    }
    
    public void declineTradeOffer(TradeOffer offer) {
        
    }
    
    private User user() {
        return sessionContext.getUser();
    }
}
