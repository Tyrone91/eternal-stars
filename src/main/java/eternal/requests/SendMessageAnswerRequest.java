package eternal.requests;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import eternal.persistence.data.MessageTO;

@Named
@RequestScoped
public class SendMessageAnswerRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private MessageTO originalMessage;
    
    private String message;

    public MessageTO getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(MessageTO originalMessage) {
        this.originalMessage = originalMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    

}
