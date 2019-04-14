package eternal.actions.user;

import java.util.Optional;

import javax.inject.Inject;

import eternal.actions.AbstractAction;
import eternal.mangement.MessageHandler;
import eternal.mangement.UserHandler;
import eternal.requests.RequestResponse;
import eternal.requests.SendMessageRequest;
import eternal.user.User;
import eternal.user.UserRight;
import eternal.util.Strings;

public class SendMessageAction extends AbstractAction<Boolean, SendMessageRequest>{
    
    private static final long serialVersionUID = 1L;

    @Inject
    private MessageHandler messageHandler;
    
    @Inject
    private UserHandler userHandler;
    
    @Inject 
    private RequestResponse response;

    @Override
    public UserRight getNeededRight() {
        return UserRight.CHAT;
    }

    @Override
    protected Boolean action(User user, SendMessageRequest... args) {
        final SendMessageRequest request = args[0];
        
        if(!Strings.exists(request.getMessage(), request.getSubject(), request.getReceiverId() )) {
            response.setMessage("Not all needed data provided");
            return false;
        }
        
        final Optional<User> receiver = userHandler.find(request.getReceiverId());
        if(!receiver.isPresent() ) {
            response.setMessage("User was not found");
            return false;
        }
        
        if(request.getSubject().length() > 30) {
            response.setMessage("The subject title is too long");
            return false;
        }
        
        final boolean res= messageHandler.sendMessage(request.getSubject(), request.getMessage(), user, receiver.get());
        if(res) {
            response.setBad(false);
            response.setMessage("Message send to " + receiver.map(User::getUsername).get());
        }
        return res;
    }

}
