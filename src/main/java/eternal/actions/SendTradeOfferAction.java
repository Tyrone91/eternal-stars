package eternal.actions;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.TradeOffer;
import eternal.game.control.TradeOfferHandler;
import eternal.game.control.TradeOfferHandler.NotEnoughResourcesException;
import eternal.requests.RequestResponse;
import eternal.requests.SendTradeOfferRequest;
import eternal.user.User;
import eternal.user.UserRight;
import eternal.util.GameAccountNotFoundException;
import eternal.util.PlanetNotFoundException;

/**
 * Sends a trade offer to the requested user.
 * TODO: not finished.
 */
@Named
@SessionScoped
public class SendTradeOfferAction extends AbstractAction<Boolean, SendTradeOfferRequest> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private RequestResponse requestResponse;
    
    @Inject
    private TradeOfferHandler tradeOfferHandler;

    @Override
    public UserRight getNeededRight() {
        return UserRight.TRADE;
    }

    @Override
    protected Boolean action(User user, SendTradeOfferRequest... args) {
        SendTradeOfferRequest request = args[0];
        
        final User initiator = user;
        final User receiver = request.getTarget();
        
        final TradeOffer tradeOffer = new TradeOffer();
        tradeOffer.setInitiator(initiator);
        tradeOffer.setReceiver(receiver);
        tradeOffer.setMessage(request.getMessage());
        tradeOffer.setReceiverGets(request.getOfferGives());
        tradeOffer.setReceiverGives(request.getOfferWansts());
        
        try {            
            return tradeOfferHandler.sendTradeOffer(tradeOffer);
        } catch(PlanetNotFoundException ex) {
            requestResponse.setMessage("No planet found");
        } catch (GameAccountNotFoundException ex) {
            requestResponse.setMessage("No GameAccount found");
        } catch (NotEnoughResourcesException ex) {
            requestResponse.setMessage("Not enough resources");
        }
        return false;
    }
}
