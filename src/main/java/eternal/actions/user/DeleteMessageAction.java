package eternal.actions.user;

import javax.inject.Inject;

import eternal.actions.AbstractAction;
import eternal.mangement.MessageHandler;
import eternal.persistence.data.MessageTO;
import eternal.requests.RequestResponse;
import eternal.user.User;
import eternal.user.UserRight;

public class DeleteMessageAction extends AbstractAction<Boolean, MessageTO>{
    
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
    protected Boolean action(User user, MessageTO... args) {
        final MessageTO message = args[0];
        if(messageHandler.deleteMessage(message)) {
            response.setMessage("Message deleted");
            response.setBad(false);
            return true;
        }
        response.setMessage("Upps... something went wrong");
        return false;
    }

}
