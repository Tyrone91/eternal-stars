package eternal.persistence.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import eternal.user.User;

@Entity(name = "Message")
public class MessageTO {
    
    @Id
    @GeneratedValue
    private int id;
    
    @OneToOne
    private User receiver;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;
    
    @OneToOne
    private User sender;
    
    private String subject;
    
    private String message;

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }
    
    public void setSendDate(Date date) {
        this.sendDate = date;
    }
    
    public Date getSendDate() {
        return this.sendDate;
    }
}
