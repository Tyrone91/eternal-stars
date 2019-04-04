package eternal.requests;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import eternal.game.Resources;
import eternal.game.control.GameAccount;

@Named
@RequestScoped
public class SendTradeOfferRequest {
    
    private String message;
    private Resources offerGives;
    private Resources offerWansts;
    private GameAccount target;
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Resources getOfferGives() {
        return offerGives;
    }
    
    public void setOfferGives(Resources offerGives) {
        this.offerGives = offerGives;
    }
    
    public Resources getOfferWansts() {
        return offerWansts;
    }
    
    public void setOfferWansts(Resources offerWansts) {
        this.offerWansts = offerWansts;
    }
    
    public GameAccount getTarget() {
        return target;
    }
    
    public void setTarget(GameAccount target) {
        this.target = target;
    }
    
    
}
