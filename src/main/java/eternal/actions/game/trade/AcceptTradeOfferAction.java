package eternal.actions.game.trade;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.AbstractAction;
import eternal.actions.mangement.SystemMessageHandler;
import eternal.game.TradeOffer;
import eternal.game.control.TradeOfferHandler;
import eternal.game.control.TradeOfferHandler.NotEnoughResourcesException;
import eternal.mangement.UserHandler;
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
    
    @Inject
    private UserHandler userHandler;
    
    @Inject
    private SystemMessageHandler messageHandler;

    @Override
    public UserRight getNeededRight() {
        return UserRight.TRADE;
    }

    @Override
    protected Boolean action(User user, TradeOffer... args) {
        try {
            final boolean res = tradeHandler.acceptTradeOffer(args[0]);
            
            userHandler.find(args[0].getInitiator().getOwnerId()).ifPresent(u -> sendConfirmation(u, args[0]));
            if(res) {
                response.setBad(false);
                response.setMessage("Accepted TradeOffer");
            }
            return res;
        } catch (PlanetNotFoundException e) {
            response.setMessage("No planet found: " + e.getMessage());
        } catch(NotEnoughResourcesException e) {
            response.setMessage("Not enough resources");
        } catch(GameAccountNotFoundException e) {
            response.setMessage("Missing Account");
        }
        return false;
    }
    
    private void sendConfirmation(User user, TradeOffer offer) {
        final String message = "%s, your one of your TradeOffers was accepted. \n"
                + "You received:\n"
                + " '%s' Metal\n"
                + " '%s' Crystal\n"
                + " '%s' Energy\n";
        final long metal = offer.getReceiverGives().getMetal();
        final long crystal = offer.getReceiverGives().getMetal();
        final long energy = offer.getReceiverGives().getMetal();
        messageHandler.sendMessage(user, "TradeOffer was accepted", String.format(message, offer.getInitiator().getDisplayName(), metal, crystal, energy));
    }

}
