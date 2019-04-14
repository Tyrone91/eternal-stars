package eternal.session;

import java.io.Serializable;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.Resource;
import eternal.game.Resources;
import eternal.game.TradeOffer;
import eternal.game.control.GameAccount;
import eternal.game.resources.Crystal;
import eternal.game.resources.Energy;
import eternal.game.resources.Metal;
import eternal.game.resources.ResourceSpecialization;
import eternal.persistence.ResourceFactory;
import eternal.persistence.data.ResourceTO;
import eternal.util.UITable;
import eternal.util.UITable.Row;
import eternal.util.UITable.RowComparator;

@Named
@SessionScoped
public class TradeControls implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private InteractionHandler handler;
    
    @Inject
    private ResourceFactory factory;
    
    private UITable<TradeOffer> sendOffersTable;
    private UITable<TradeOffer> receivedOffersTable;
    
    @PostConstruct
    private void setup() {
        final UITable.Builder<TradeOffer> builderSend = UITable.builder();
        sendOffersTable = builderSend
                .add("Receiver-Username", (column, o1, o2) -> usernamecomparator(o1.getReceiver(), o2.getReceiver()))
                .add("Receiver-Nickname", (column, o1, o2) -> nicknamecomparator(o1.getReceiver(), o2.getReceiver()))
                
                .add("Metal(+)", metal(TradeOffer::getReceiverGives))
                .add("Crystal(+)", crystal(TradeOffer::getReceiverGives))
                .add("Energy(+)", energy(TradeOffer::getReceiverGives))
                
                .add("Metal(-)", metal(TradeOffer::getReceiverGets))
                .add("Crystal(-)", crystal(TradeOffer::getReceiverGets))
                .add("Energy(-)", energy(TradeOffer::getReceiverGets))
                
                .build(handler.getTrade()::getSendTradeOffers, this::toSendMap);
        
        final UITable.Builder<TradeOffer> builderReceived = UITable.builder();
        receivedOffersTable = builderReceived
                .add("Send-by-Username", (column, o1, o2) -> usernamecomparator(o1.getInitiator(), o2.getInitiator()))
                .add("Send-by-Nickname", (column, o1, o2) -> nicknamecomparator(o1.getInitiator(), o2.getInitiator()))
                
                .add("Metal(+)", metal(TradeOffer::getReceiverGets))
                .add("Crystal(+)", crystal(TradeOffer::getReceiverGets))
                .add("Energy(+)", energy(TradeOffer::getReceiverGets))
                
                .add("Metal(-)", metal(TradeOffer::getReceiverGives))
                .add("Crystal(-)", crystal(TradeOffer::getReceiverGives))
                .add("Energy(-)", energy(TradeOffer::getReceiverGives))
                
                .build(handler.getTrade()::getReceivedTradeOffers, this::toSReceivedMap);
    }
    
    private Row<TradeOffer> toSendMap(TradeOffer offer, Row<TradeOffer> row) {
        return row
                .set("Receiver-Username", offer, o -> o.getReceiver().getOwnerId())
                .set("Receiver-Nickname", offer, o -> o.getReceiver().getDisplayName())
                
                .set("Metal(+)", offer, o -> String.valueOf(o.getReceiverGives().getMetal()))
                .set("Crystal(+)", offer, o -> String.valueOf(o.getReceiverGives().getCrystal()))
                .set("Energy(+)", offer, o -> String.valueOf(o.getReceiverGives().getEnergy()))
                
                .set("Metal(-)", offer, o -> String.valueOf(o.getReceiverGets().getMetal()))
                .set("Crystal(-)", offer, o -> String.valueOf(o.getReceiverGets().getCrystal()))
                .set("Energy(-)", offer, o -> String.valueOf(o.getReceiverGets().getEnergy()))
                
                .action("fas fa-times red", "decline", handler.getTrade()::declineTradeOffer
                );
        
    }
    
    private Row<TradeOffer> toSReceivedMap(TradeOffer offer, Row<TradeOffer> row) {
        return row
                .set("Send-by-Username", offer, o -> o.getInitiator().getOwnerId())
                .set("Send-by-Nickname", offer, o -> o.getInitiator().getDisplayName())
                
                .set("Metal(+)", offer, o -> String.valueOf(o.getReceiverGets().getMetal()))
                .set("Crystal(+)", offer, o -> String.valueOf(o.getReceiverGets().getCrystal()))
                .set("Energy(+)", offer, o -> String.valueOf(o.getReceiverGets().getEnergy()))
                
                .set("Metal(-)", offer, o -> String.valueOf(o.getReceiverGives().getMetal()))
                .set("Crystal(-)", offer, o -> String.valueOf(o.getReceiverGives().getCrystal()))
                .set("Energy(-)", offer, o -> String.valueOf(o.getReceiverGives().getEnergy()))
                
                .action("fas fa-times red", "decline", handler.getTrade()::declineTradeOffer)
                .action("fas fa-check green", "accept", handler.getTrade()::acceptTradeOffer);
    }
    
    public UITable<TradeOffer> getSendOffersTable() {
        return this.sendOffersTable.updateData();
    }
    
    public UITable<TradeOffer> getReceivedOffersTable() {
        return this.receivedOffersTable.updateData();
    }
    
    public int getTotalOffersSend() {
        return this.handler.getTrade().getSendTradeOffers().size();
    }
    
    public int getTotalOffersReceived() {
        return this.handler.getTrade().getReceivedTradeOffers().size();
    }
    
    private int nicknamecomparator(GameAccount acc1, GameAccount acc2) {
        return acc1.getDisplayName().compareTo(acc2.getDisplayName());
    }
    
    private int usernamecomparator(GameAccount acc1, GameAccount acc2) {
        return acc1.getOwnerId().compareTo(acc2.getOwnerId());
    }
    
    private RowComparator<TradeOffer> metal(Function<TradeOffer, ResourceTO> getter) {
        return (column, offer1, offer2) -> {
            return compare(get(getter.apply(offer1), this::metal), get(getter.apply(offer2), this::metal));
        };
    }
    
    private RowComparator<TradeOffer> crystal(Function<TradeOffer, ResourceTO> getter) {
        return (column, offer1, offer2) -> {
            return compare(get(getter.apply(offer1), this::crystal), get(getter.apply(offer2), this::crystal));
        };
    }
    
    private RowComparator<TradeOffer> energy(Function<TradeOffer, ResourceTO> getter) {
        return (column, offer1, offer2) -> {
            return compare(get(getter.apply(offer1), this::energy), get(getter.apply(offer2), this::energy));
        };
    }
    
    private <T extends Resource> int compare(ResourceSpecialization<T> r1, ResourceSpecialization<T> r2) {
        return r1.compareTo(r2);
    }
    
    private <T extends Resource> ResourceSpecialization<T> get(ResourceTO res, Function<Resources, ResourceSpecialization<T>> mapper) {
        return mapper.apply(get(res));
    }
    
    private ResourceSpecialization<Metal> metal(Resources res) {
        return res.getMetal();
    }
    
    private ResourceSpecialization<Crystal> crystal(Resources res) {
        return res.getCrystal();
    }
    
    private ResourceSpecialization<Energy> energy(Resources res) {
        return res.getEnergy();
    }
    
    private Resources get(ResourceTO res) {
        return factory.build(res);
    }
    
    
    
}
