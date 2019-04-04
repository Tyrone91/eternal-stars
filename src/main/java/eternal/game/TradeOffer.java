package eternal.game;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import eternal.game.control.GameAccount;
import eternal.persistence.data.ResourceTO;

@Entity
public class TradeOffer implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private int id;
    
    @ManyToOne
    private GameAccount initiator;
    
    @ManyToOne
    private GameAccount receiver;
    
    @Embedded
    @AttributeOverrides( value = {
            @AttributeOverride( name = "crystal", column= @Column(name = "get_crystal")),
            @AttributeOverride( name = "metal", column= @Column(name = "get_metal")),
            @AttributeOverride( name = "energy", column= @Column(name = "get_energy"))
    })
    private ResourceTO receiverGets;
    
    @Embedded
    private ResourceTO receiverGives;
    
    private String message;
    
    public GameAccount getInitiator() {
        return initiator;
    }

    public void setInitiator(GameAccount initiator) {
        this.initiator = initiator;
    }

    public GameAccount getReceiver() {
        return receiver;
    }

    public void setReceiver(GameAccount receiver) {
        this.receiver = receiver;
    }

    public ResourceTO getReceiverGets() {
        return receiverGets;
    }

    public void setReceiverGets(ResourceTO receiverGets) {
        this.receiverGets = receiverGets;
    }

    public ResourceTO getReceiverGives() {
        return receiverGives;
    }

    public void setReceiverGives(ResourceTO receiverGives) {
        this.receiverGives = receiverGives;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
