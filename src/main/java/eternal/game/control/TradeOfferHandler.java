package eternal.game.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.Resources;
import eternal.game.TradeOffer;
import eternal.game.environment.Planet;
import eternal.mangement.GameAccountHandler;
import eternal.mangement.UserHandler;
import eternal.persistence.ResourceFactory;
import eternal.persistence.TradeOfferAccessObject;
import eternal.user.User;
import eternal.util.GameAccountNotFoundException;
import eternal.util.PlanetNotFoundException;

@Named
@ApplicationScoped
public class TradeOfferHandler {
    
    @Inject
    private ResourceFactory factory;
    
    @Inject
    private TradeOfferAccessObject tradeOfferDAO;
    
    @Inject
    private UserHandler userHandler;
    
    @Inject
    private PlanetHandler planetHandler;
    
    @Inject
    private GameAccountHandler accountHandler;
    
    public synchronized boolean acceptTradeOffer(TradeOffer offer) throws NotEnoughResourcesException, PlanetNotFoundException, GameAccountNotFoundException {
        return checkResourcesStorage(offer.getReceiver(), factory.build(offer.getReceiverGives()), planet -> {
            final Resources receiverGets = factory.build(offer.getReceiverGets());
            final Resources receiverGives = factory.build(offer.getReceiverGives());
            
            final Planet receiver = planet;
            final Planet initiator = findPlanet(offer.getInitiator());
            
            receiver.getPlanetResources().add(receiverGets);
            receiver.getPlanetResources().sub(receiverGives);
            
            initiator.getPlanetResources().add(receiverGives);
            
            planetHandler.updatePlanet(receiver);
            planetHandler.updatePlanet(initiator);
            
            return removeTradeOffer(offer);
            
        });
    }
    
    public synchronized boolean declineTradeOffer(TradeOffer offer) throws PlanetNotFoundException {
        final Planet initiator = findPlanet(offer.getInitiator());
        initiator.getPlanetResources().add(factory.build(offer.getReceiverGets()));
        planetHandler.updatePlanet(initiator);
        return removeTradeOffer(offer);
    }
    
    public List<TradeOffer> getSendTradeOffers(User user) {
        return findAccount(user)
                .map(GameAccount::getSendTradeOffers)
                .map(this::asList)
                .orElseGet(Collections::emptyList);
    }
    
    public List<TradeOffer> getReceivedTradeOffers(User user) {
        return findAccount(user)
                .map(GameAccount::getReceivedTradeOffers)
                .map(this::asList)
                .orElseGet(Collections::emptyList);
    }
    
    public String getInitiatorName(TradeOffer offer) {
        return getName(offer.getInitiator());
    }
    
    public String getReceiverName(TradeOffer offer) {
        return getName(offer.getReceiver());
    }
    
    private String getName(GameAccount account) {
        return getName(account::getOwnerId, account::getDisplayName);
    }
    
    private String getName(Supplier<String> id, Supplier<String> name) {
        return String.format("%s(%s)", id.get(), name.get() );
    }
    
    private Optional<GameAccount> findAccount(User user) {
        return this.accountHandler.findAccount(user);
    }
    
    private boolean removeTradeOffer(final TradeOffer offer) {
        
        offer.getReceiver().removeReceivedTradeOffer(offer);
        offer.getInitiator().removeSendTradeOffer(offer);
        
        accountHandler.updateAccount(offer.getReceiver());
        accountHandler.updateAccount(offer.getInitiator());
        
        return tradeOfferDAO.deleteOffer(offer);
    }
    
    public synchronized boolean sendTradeOffer(TradeOffer offer) throws NotEnoughResourcesException, GameAccountNotFoundException, PlanetNotFoundException {
        return checkResourcesStorage(offer.getInitiator(), factory.build(offer.getReceiverGets()), planet -> {
            planet.getPlanetResources().sub(factory.build(offer.getReceiverGets()));
            if(!planetHandler.updatePlanet(planet)) {
                return false;
            }
            
            offer.getInitiator().addSendTradeOffer(offer);
            offer.getReceiver().addReceivedTradeOffer(offer);
            
            return tradeOfferDAO.storeOfferAndUpdateUser(offer);
        });
    }
    
    private List<TradeOffer> asList(Collection<TradeOffer> offers) {
        return new ArrayList<>(offers);
    }
    
    private Planet findPlanet(GameAccount account) throws PlanetNotFoundException {
        return planetHandler
                .findPlanet(account.getHomePlanetId())
                .orElseThrow( () -> new PlanetNotFoundException(account.getHomePlanetId(), account));
    }
    
    private boolean checkResourcesStorage(GameAccount payer, Resources amount, Function<Planet, Boolean> onSuccess) throws GameAccountNotFoundException, PlanetNotFoundException, NotEnoughResourcesException {
        
        Optional<Planet> planet = planetHandler.findPlanet(payer.getHomePlanetId());
        if(!planet.isPresent()) {
           throw new PlanetNotFoundException(payer.getHomePlanetId(), payer);
        }
        
        if(planet.get().getPlanetResources().isPayable(amount)) {
            return onSuccess.apply(planet.get());
        } else {
            throw new NotEnoughResourcesException(payer, amount);
        }
    }
    
    public class NotEnoughResourcesException extends Exception {
       
        private static final long serialVersionUID = 1L;
        private GameAccount account;
        private Resources resources;
        
        public NotEnoughResourcesException(GameAccount withMissingRes, Resources res) {
            super(String.format("[%s] has not enough resources.", withMissingRes.getOwnerId() ));
            this.account = withMissingRes;
            this.resources = res;
        }
        
        public GameAccount getAccountOfMissingResources() {
            return this.account;
        }
        
        public Resources getWantedResources() {
            return this.resources;
        }
    }
}
