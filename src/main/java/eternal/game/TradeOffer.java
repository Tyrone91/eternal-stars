package eternal.game;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import eternal.user.User;

@Entity
public class TradeOffer implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private int id;
    
    @ManyToOne
    private User initiator;
    
    @ManyToOne
    private User receiver;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Resources receiverGets;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Resources receiverGives;
    
    private String message;
    
    
    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Resources getReceiverGets() {
        return receiverGets;
    }

    public void setReceiverGets(Resources receiverGets) {
        this.receiverGets = receiverGets;
    }

    public Resources getReceiverGives() {
        return receiverGives;
    }

    public void setReceiverGives(Resources receiverGives) {
        this.receiverGives = receiverGives;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
