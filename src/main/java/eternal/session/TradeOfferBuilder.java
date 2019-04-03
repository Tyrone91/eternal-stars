package eternal.session;

import java.io.Serializable;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.Resources;
import eternal.mangement.UserHandler;
import eternal.persistence.GameAccountDataAccessObject;
import eternal.requests.RequestResponse;
import eternal.requests.SendTradeOfferRequest;
import eternal.user.User;

@Named
@RequestScoped
public class TradeOfferBuilder implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private SessionContext sessionContext;
    
    @Inject
    private UserHandler userHandler;
    
    @Inject
    private RequestResponse requestResponse;
    
    @Inject
    private SendTradeOfferRequest tradeOffer;
    
    @Inject
    private GameAccountDataAccessObject accountDAO;
    
    @Inject
    private GameControls gameControls;
    
    private long metalOffer;
    private long metalDemand;
    
    private long crystalOffer;
    private long crystalDemand;
    
    private long energyOffer;
    private long energyDemand;
    
    public long getMetalOffer() {
        return metalOffer;
    }

    public void setMetalOffer(long metalOffer) {
        this.metalOffer = metalOffer;
    }

    public long getMetalDemand() {
        return metalDemand;
    }

    public void setMetalDemand(long metalDemand) {
        this.metalDemand = metalDemand;
    }

    public long getCrystalOffer() {
        return crystalOffer;
    }

    public void setCrystalOffer(long crystalOffer) {
        this.crystalOffer = crystalOffer;
    }

    public long getCrystalDemand() {
        return crystalDemand;
    }

    public void setCrystalDemand(long crystalDemand) {
        this.crystalDemand = crystalDemand;
    }

    public long getEnergyOffer() {
        return energyOffer;
    }

    public void setEnergyOffer(long energyOffer) {
        this.energyOffer = energyOffer;
    }

    public long getEnergyDemand() {
        return energyDemand;
    }

    public void setEnergyDemand(long energyDemand) {
        this.energyDemand = energyDemand;
    }

    public String getPartnerUsername() {
        return partnerUsername;
    }

    public void setPartnerUsername(String partnerUsername) {
        this.partnerUsername = partnerUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String partnerUsername;
    private String message;
    
    public Optional<SendTradeOfferRequest> createOffer() {
        
        final Optional<User> target = userHandler.find(partnerUsername);
        if(!target.isPresent()) {
            requestResponse.setMessage("Trade partner not found");
            return Optional.empty();
        }
        
        if(!accountDAO.findAccount(target.get().getUsername()).isPresent()) {
            requestResponse.setMessage("The requested partner can not trade");
            return Optional.empty();
        }
        
        tradeOffer.setMessage(message);
        
        final Resources offer = new Resources(metalOffer, crystalOffer, energyOffer);
        final Resources demand = new Resources(metalDemand, crystalDemand, energyDemand);
        
        tradeOffer.setOfferGives(offer);
        tradeOffer.setOfferWansts(demand);        
        
        tradeOffer.setTarget(target.get());
        
        return Optional.of(tradeOffer);
    }
        
    public void tradeWith(User user) {
        tradeOffer.setTarget(user);
        this.partnerUsername = user.getUsername();
        gameControls.setCurrentFocus(GameControls.TRADE);
    }

}
