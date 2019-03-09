package eternal.actions;

import java.io.Serializable;
import java.util.Optional;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.TradeOffer;
import eternal.game.control.GameAccount;
import eternal.game.control.PlanetHandler;
import eternal.game.environment.Planet;
import eternal.requests.RequestResponse;
import eternal.requests.SendTradeOfferRequest;
import eternal.user.User;
import eternal.user.UserRight;

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
    private PlanetHandler planetHandler;

    @Override
    public UserRight getNeededRight() {
        return UserRight.TRADE;
    }

    @Override
    protected Boolean action(User user, SendTradeOfferRequest... args) {
        SendTradeOfferRequest request = args[0];
        
        Optional<GameAccount> account = user.getGameAccount();
        if(!account.isPresent()) {
            requestResponse.setMessage("No GameAccount found");
            return false;
        }
        
        Optional<Planet> planet =  planetHandler.findPlanet(account.get().getHomePlanetId());
        if(!planet.isPresent()) {
            requestResponse.setMessage("No planet found");
            return false;
        }
        
        if(!planet.get().getPlanetResources().isPayable(request.getOfferGives())) {
            requestResponse.setMessage("Not enough resources");
            return false;
        }
        
        TradeOffer tradeOffer = new TradeOffer();
        
        requestResponse.setMessage("Send trade offer");
        
        return true;
        
    }

}
