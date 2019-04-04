package eternal.session.interaction;

import java.util.Collections;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.game.trade.AcceptTradeOfferAction;
import eternal.actions.game.trade.DeclineTradeOfferAction;
import eternal.actions.game.trade.GetReceivedTradeOffersAction;
import eternal.actions.game.trade.GetSendTradeOffersAction;
import eternal.actions.game.trade.SendTradeOfferAction;
import eternal.game.TradeOffer;
import eternal.requests.SendTradeOfferRequest;

@Named
@SessionScoped
public class TradeInteractionHandler extends BaseInteractionHandler {
    
    private static final long serialVersionUID = 1L;

    @Inject
    private SendTradeOfferAction sendTradeOfferAction;
    
    @Inject
    private AcceptTradeOfferAction acceptTradeOfferAction;
    
    @Inject
    private DeclineTradeOfferAction declineTradeOfferAction;
    
    @Inject
    private GetSendTradeOffersAction getSendTradeOffersAction;
    
    @Inject
    private GetReceivedTradeOffersAction getReceivedTradeOffersAction;
    
    public boolean sendTradeOffer(SendTradeOfferRequest request) {
        return sendTradeOfferAction.performAction(sessionContext.getUser(), request).orElse(false);
    }
    
    public boolean acceptTradeOffer(TradeOffer offer) {
        return acceptTradeOfferAction.performAction(sessionContext.getUser(), offer).orElse(false);
    }
    
    public boolean declineTradeOffer(TradeOffer offer) {
        return declineTradeOfferAction.performAction(sessionContext.getUser(), offer).orElse(false);
    }
    
    public List<TradeOffer> getSendTradeOffers() {
        return getSendTradeOffersAction.performAction(sessionContext.getUser()).orElseGet(Collections::emptyList);
    }
    
    public List<TradeOffer> getReceivedTradeOffers() {
        return getReceivedTradeOffersAction.performAction(sessionContext.getUser()).orElseGet(Collections::emptyList);
    }
}
