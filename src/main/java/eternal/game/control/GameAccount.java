package eternal.game.control;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import eternal.core.GameContext;
import eternal.game.TradeOffer;
import eternal.user.User;

/**
 * A {@link GameAccount} only exists in the context of a normal user.
 * The admin as an example would be user who is not suppose to play the game so he would not have a {@link GameAccount}.
 * The most users will have a {@link GameAccount}.
 * The {@link GameAccount} augments the normal user-account with the game relevant information like homeplanet and displayname.
 */
@Entity
public class GameAccount {
    
    @Id
    private String ownerId;
    
    private String displayName;
    
    private int homePlanetId;
    
    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER)
    private Set<TradeOffer> receivedTradeOffers = new HashSet<>();
    
    @OneToMany(mappedBy = "initiator", fetch = FetchType.EAGER)
    private Set<TradeOffer> sendTradeOffers = new HashSet<>();

    public String getOwnerId() {
        return ownerId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setOwnerId(User owner) {
        this.ownerId = owner.getUsername();
    }

    public int getHomePlanetId() {
        return homePlanetId;
    }

    public void setHomePlanetId(int homePlanetId) {
        this.homePlanetId = homePlanetId;
    }
    
    public void onload(GameContext context) {
            
    }
    
    public void addReceivedTradeOffer(TradeOffer offer) {
        this.receivedTradeOffers.add(offer);
    }
    
    public void addSendTradeOffer(TradeOffer offer) {
        this.receivedTradeOffers.add(offer);
    }
    
    public void removeReceivedTradeOffer(TradeOffer offer) {
        this.receivedTradeOffers.remove(offer);
    }
    
    public void removeSendTradeOffer(TradeOffer offer) {
        this.sendTradeOffers.remove(offer);
    }
    
    public Set<TradeOffer> getReceivedTradeOffers() {
        return this.receivedTradeOffers;
    }
    
    public Set<TradeOffer> getSendTradeOffers() {
        return this.sendTradeOffers;
    }
}
