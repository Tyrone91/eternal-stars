package eternal.actions.game.trade;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.AbstractAction;
import eternal.game.TradeOffer;
import eternal.game.control.TradeOfferHandler;
import eternal.requests.RequestResponse;
import eternal.user.User;
import eternal.user.UserRight;
import eternal.util.PlanetNotFoundException;

@Named
@SessionScoped
public class DeclineTradeOfferAction extends AbstractAction<Boolean, TradeOffer> {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private RequestResponse response;
    
    @Inject
    private TradeOfferHandler tradeHandler;

    @Override
    public UserRight getNeededRight() {
        return UserRight.TRADE;
    }

    @Override
    protected Boolean action(User user, TradeOffer... args) {
        try {
            return tradeHandler.declineTradeOffer(args[0]);
        } catch(PlanetNotFoundException ex) {
            response.setMessage("Missing planet: " + ex.getMessage());
            return false;
        }
    }

}
