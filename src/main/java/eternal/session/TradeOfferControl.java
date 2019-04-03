package eternal.session;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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
    
    public List<TradeOffer> getSendTradeOffers() {
        return user().getTradeOffers().stream().filter(this::isSendByUser).collect(Collectors.toList());
    }
    
    public List<TradeOffer> getReceivedTradeOffers() {
        return user().getTradeOffers().stream().filter(this::isNotSendByUser).collect(Collectors.toList());
    }
    
    private boolean isNotSendByUser(TradeOffer offer) {
        return !isSendByUser(offer);
    }
    
    private boolean isSendByUser(TradeOffer offer) {
        return offer.getInitiator().getUsername().equals(user().getUsername());
    }
    
    private User user() {
        return sessionContext.getUser();
    }
}
