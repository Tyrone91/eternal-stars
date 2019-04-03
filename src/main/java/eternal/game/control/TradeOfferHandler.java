package eternal.game.control;

import java.util.Optional;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.Resources;
import eternal.game.TradeOffer;
import eternal.game.environment.Planet;
import eternal.mangement.UserHandler;
import eternal.persistence.TradeOfferAccessObject;
import eternal.user.User;
import eternal.util.GameAccountNotFoundException;
import eternal.util.PlanetNotFoundException;

@Named
@ApplicationScoped
public class TradeOfferHandler {
    
    @Inject
    private TradeOfferAccessObject tradeOfferDAO;
    
    @Inject
    private UserHandler userHandler;
    
    @Inject
    private PlanetHandler planetHandler;
    
    public synchronized boolean acceptTradeOffer() {
        return true;
    }
    
    public synchronized boolean declineTradeOffer() {
        return true;
    }
    
    public synchronized boolean sendTradeOffer(TradeOffer offer) throws NotEnoughResourcesException, GameAccountNotFoundException, PlanetNotFoundException {
        return checkResourcesStorage(offer.getInitiator(), offer.getReceiverGets(), planet -> {
            planet.getPlanetResources().sub(offer.getReceiverGets());
            if(!planetHandler.updatePlanet(planet)) {
                return false;
            }
            
            offer.getInitiator().addTradeOffer(offer);
            offer.getReceiver().addTradeOffer(offer);
            
            return tradeOfferDAO.storeOfferAndUpdateUser(offer);
        });
    }
    
    private boolean checkResourcesStorage(User payer, Resources amount, Function<Planet, Boolean> onSuccess) throws GameAccountNotFoundException, PlanetNotFoundException {
        if(!payer.getGameAccount().isPresent()) {
            throw new GameAccountNotFoundException(payer);
        }
        
        GameAccount account = payer.getGameAccount().get();
        
        Optional<Planet> planet = planetHandler.findPlanet(account.getHomePlanetId());
        if(!planet.isPresent()) {
           throw new PlanetNotFoundException(account.getHomePlanetId(), account);
        }
        
        if(planet.get().getPlanetResources().isPayable(amount)) {
            return onSuccess.apply(planet.get());
        }
        return false;
    }
    
    public class NotEnoughResourcesException extends Exception {
       
        private static final long serialVersionUID = 1L;
        private User user;
        private Resources resources;
        
        public NotEnoughResourcesException(User withMissingRes, Resources res) {
            super(String.format("[%s] has not enough resources.", withMissingRes.getUsername() ));
            this.user = withMissingRes;
            this.resources = res;
        }
        
        public User getUserOfMissingResources() {
            return this.user;
        }
        
        public Resources getWantedResources() {
            return this.resources;
        }
    }
}
