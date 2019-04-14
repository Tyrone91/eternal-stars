package eternal.mangement;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.persistence.MessageDataAccessObject;
import eternal.persistence.data.MessageTO;
import eternal.user.User;

@Named
@ApplicationScoped
public class MessageHandler {
    
    @Inject
    private MessageDataAccessObject messageDAO;
    
    public boolean sendMessage(MessageTO message) {
        message.setSendDate(new Date());
        message.getSender().addSendMessage(message);
        message.getReceiver().addReceivedMessage(message);
        return messageDAO.storeMessageAndUpdateUser(message, false);
        
    }
    
    public boolean sendMessage(String subject, String message, User from, User receiver) {
        final MessageTO obj = new MessageTO();
        obj.setReceiver(receiver);
        obj.setSender(from);
        obj.setSubject(subject);
        obj.setMessage(message);
        return sendMessage(obj);
    }
    
    public boolean deleteMessage(MessageTO message) { //TODO: Delete removes the message for both sides.
        return messageDAO.deleteMessageAndUpdateUser(message);
    }
    
    public MessageTO createAnswer(MessageTO original, String anwser) {
        final MessageTO message = new MessageTO();
        message.setReceiver(original.getSender());
        message.setSender(original.getReceiver());
        message.setSubject("RE: [" + original.getSubject() + "]");
        message.setMessage(anwser);
        return message;
    }
    
}
