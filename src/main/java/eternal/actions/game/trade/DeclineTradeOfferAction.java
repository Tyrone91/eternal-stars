package eternal.actions.game.trade;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.AbstractAction;
import eternal.actions.mangement.SystemMessageHandler;
import eternal.game.TradeOffer;
import eternal.game.control.TradeOfferHandler;
import eternal.mangement.UserHandler;
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
            final boolean res = tradeHandler.declineTradeOffer(args[0]);
            userHandler.find(args[0].getInitiator().getOwnerId()).ifPresent(u -> sendConfirmation(u, args[0]));
            if(res) {
                response.setBad(false);
                response.setMessage("TradeOffer declined");
            }
            return res;
        } catch(PlanetNotFoundException ex) {
            response.setMessage("Missing planet: " + ex.getMessage());
            return false;
        }
    }
    
    private void sendConfirmation(User user, TradeOffer offer) {
        final String message = "%s, one of your TradeOffers was declined. \n"
                + "You got back your resources:\n"
                + " '%s' Metal\n"
                + " '%s' Crystal\n"
                + " '%s' Energy\n";
        final long metal = offer.getReceiverGets().getMetal();
        final long crystal = offer.getReceiverGets().getMetal();
        final long energy = offer.getReceiverGets().getMetal();
        messageHandler.sendMessage(user, "TradeOffer was declined", String.format(message, offer.getInitiator().getDisplayName(), metal, crystal, energy));
    }

}
