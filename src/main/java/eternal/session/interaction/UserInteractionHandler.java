package eternal.session.interaction;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.actions.user.AnswerMessageAction;
import eternal.actions.user.DeleteMessageAction;
import eternal.actions.user.SendMessageAction;
import eternal.persistence.data.MessageTO;
import eternal.requests.SendMessageAnswerRequest;
import eternal.requests.SendMessageRequest;

@Named
@ApplicationScoped
public class UserInteractionHandler extends BaseInteractionHandler {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Inject
    private SendMessageAction sendMessageAction;
    
    @Inject
    private AnswerMessageAction answerMessageAction;
    
    @Inject
    private DeleteMessageAction deleteMessageAction;
    
    public boolean sendMessage(SendMessageRequest request) {
        return this.sendMessageAction.performAction(this.user(), request).orElse(false);
    }
    
    public boolean answerMessage(SendMessageAnswerRequest request) {
        return this.answerMessageAction.performAction(this.user(), request).orElse(false);
    }
    
    public boolean deleteMessage(MessageTO message) {
        return this.deleteMessageAction.performAction(this.user(), message).orElse(false);
    }

}
