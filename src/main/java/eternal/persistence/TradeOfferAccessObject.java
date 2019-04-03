package eternal.persistence;

import java.util.Optional;

import eternal.game.TradeOffer;

public interface TradeOfferAccessObject {
    
    public boolean storeOffer(TradeOffer offer);
    public boolean updateOffer(TradeOffer offer);
    public boolean deleteOffer(TradeOffer offer);
    public boolean storeOfferAndUpdateUser(TradeOffer offer);
    public Optional<TradeOffer> findOffer(String id);
}
