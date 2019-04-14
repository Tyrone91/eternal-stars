package eternal.actions.user;

import javax.inject.Inject;

import eternal.actions.AbstractAction;
import eternal.mangement.MessageHandler;
import eternal.persistence.data.MessageTO;
import eternal.requests.RequestResponse;
import eternal.requests.SendMessageAnswerRequest;
import eternal.user.User;
import eternal.user.UserRight;
import eternal.util.Strings;

public class AnswerMessageAction extends AbstractAction<Boolean, SendMessageAnswerRequest>{
    
    private static final long serialVersionUID = 1L;

    @Inject
    private MessageHandler messageHandler;
    
    @Inject 
    private RequestResponse response;

    @Override
    public UserRight getNeededRight() {
        return UserRight.CHAT;
    }

    @Override
    protected Boolean action(User user, SendMessageAnswerRequest... args) {
        final SendMessageAnswerRequest request = args[0];
        
        if(!Strings.exists(request.getMessage()) ) {
            response.setMessage("Not all needed data provided");
            return false;
        }
        final MessageTO message = messageHandler.createAnswer(request.getOriginalMessage(), request.getMessage());
        final boolean res = messageHandler.sendMessage(message);
        if(res) {
            response.setBad(false);
            response.setMessage("Send answer to " + message.getReceiver().getUsername());
        }
        return res;
    }

}
