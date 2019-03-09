package eternal.requests;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import eternal.game.Resources;
import eternal.user.User;

@Named
@RequestScoped
public class SendTradeOfferRequest {
    
    private String message;
    private Resources offerGives;
    private Resources offerWansts;
    private User target;
    
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
    
    public User getTarget() {
        return target;
    }
    
    public void setTarget(User target) {
        this.target = target;
    }
    
    
}
