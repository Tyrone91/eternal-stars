package eternal.actions.game.trade;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.AbstractAction;
import eternal.game.TradeOffer;
import eternal.game.control.TradeOfferHandler;
import eternal.user.User;
import eternal.user.UserRight;

@Named
@SessionScoped
public class GetReceivedTradeOffersAction extends AbstractAction<List<TradeOffer>, Void> {

    @Inject
    private TradeOfferHandler handler;
    
    @Override
    public UserRight getNeededRight() {
        return UserRight.TRADE;
    }

    @Override
    protected List<TradeOffer> action(User user, Void... args) {
        return handler.getReceivedTradeOffers(user);
    }

}
