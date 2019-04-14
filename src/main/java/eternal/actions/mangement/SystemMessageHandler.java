package eternal.actions.mangement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.mangement.MessageHandler;
import eternal.user.User;
import eternal.util.SystemUtils;

@Named
@ApplicationScoped
public class SystemMessageHandler {

    @Inject
    private SystemUtils system;
    
    @Inject
    private MessageHandler messageHandler;
    
    public boolean sendMessage(User receiver, String subject, String message) {
        return messageHandler.sendMessage(subject, message, system.getSystemUser(), receiver);
    }
}
