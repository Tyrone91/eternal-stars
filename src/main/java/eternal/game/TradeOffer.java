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
    private User aceptor;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Resources aceptorGets;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Resources aceptorGives;
    
    private String message;
    
}
