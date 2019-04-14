package eternal.actions.game.trade;

import java.io.Serializable;
import java.util.Optional;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.AbstractAction;
import eternal.actions.mangement.SystemMessageHandler;
import eternal.game.TradeOffer;
import eternal.game.control.GameAccount;
import eternal.game.control.TradeOfferHandler;
import eternal.game.control.TradeOfferHandler.NotEnoughResourcesException;
import eternal.mangement.GameAccountHandler;
import eternal.mangement.UserHandler;
import eternal.persistence.ResourceFactory;
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
    private ResourceFactory factory;
    
    @Inject
    private RequestResponse requestResponse;
    
    @Inject
    private TradeOfferHandler tradeOfferHandler;
    
    @Inject
    private GameAccountHandler accountHandler;
    
    @Inject
    private SystemMessageHandler messageHandler;
    
    @Inject
    private UserHandler userHandler;

    @Override
    public UserRight getNeededRight() {
        return UserRight.TRADE;
    }

    @Override
    protected Boolean action(User user, SendTradeOfferRequest... args) {
        SendTradeOfferRequest request = args[0];
        
        final Optional<GameAccount> initiator = accountHandler.findAccount(user.getUsername());
        final GameAccount receiver = request.getTarget();
        
        if(!initiator.isPresent()) {
            requestResponse.setMessage("GameAccount not found");
            return false;
        }
        
        final TradeOffer tradeOffer = new TradeOffer();
        tradeOffer.setInitiator(initiator.get());
        tradeOffer.setReceiver(receiver);
        tradeOffer.setMessage(request.getMessage());
        tradeOffer.setReceiverGets(factory.build(request.getOfferGives()));
        tradeOffer.setReceiverGives(factory.build(request.getOfferWansts()));
        
        try {            
            final boolean res = tradeOfferHandler.sendTradeOffer(tradeOffer);
            userHandler.find(receiver.getOwnerId()).ifPresent( u -> message(u, receiver, tradeOffer));
            if(res) {
                requestResponse.setBad(false);
                requestResponse.setMessage("TradeOffer send to " + receiver.getDisplayName());
            }
            return res;
        } catch(PlanetNotFoundException ex) {
            requestResponse.setMessage("No planet found");
        } catch (NotEnoughResourcesException ex) {
            requestResponse.setMessage("Not enough resources");
        } catch (GameAccountNotFoundException e) {
            requestResponse.setMessage("GameAccount not found");
        }
        return false;
    }
    
    private void message(final User receiver, final GameAccount acc,final TradeOffer offer) {
        final String message = 
                "%s, you have received a new TradeOffer from '%s' aka '%s' \n" + 
                "To see the offer visit the TradeHub. \n" +
                "Message for offer: %s ";
        final String receiverDisplayNmae = acc.getDisplayName();
        final String senderDisplayName = offer.getInitiator().getDisplayName();
        final String senderUsername = offer.getInitiator().getOwnerId();
        final String senderMessage = offer.getMessage();
        final String text = String.format(message, receiverDisplayNmae, senderUsername, senderDisplayName, senderMessage);
        messageHandler.sendMessage(receiver, "New TradeOffer", text);
    }
}
