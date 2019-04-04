package eternal.actions.game.trade;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.AbstractAction;
import eternal.game.TradeOffer;
import eternal.game.control.TradeOfferHandler;
import eternal.game.control.TradeOfferHandler.NotEnoughResourcesException;
import eternal.requests.RequestResponse;
import eternal.user.User;
import eternal.user.UserRight;
import eternal.util.GameAccountNotFoundException;
import eternal.util.PlanetNotFoundException;

@Named
@SessionScoped
public class AcceptTradeOfferAction extends AbstractAction<Boolean, TradeOffer> {

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
            return tradeHandler.acceptTradeOffer(args[0]);
        } catch (PlanetNotFoundException e) {
            response.setMessage("No planet found: " + e.getMessage());
        } catch(NotEnoughResourcesException e) {
            response.setMessage("Not enough resources");
        } catch(GameAccountNotFoundException e) {
            response.setMessage("Missing Account");
        }
        return false;
    }

}
