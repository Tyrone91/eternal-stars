package eternal.session;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.mangement.UserHandler;
import eternal.persistence.data.MessageTO;
import eternal.requests.SendMessageAnswerRequest;
import eternal.requests.SendMessageRequest;
import eternal.user.User;
import eternal.util.SystemUtils;
import eternal.util.UITable;
import eternal.util.UITable.Row;


@Named
@SessionScoped
public class MessageControls implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private UITable<MessageTO> userSendTable;
    private UITable<MessageTO> userReceivedTable;
    
    private Optional<User> target;
    
    private Optional<MessageTO> selectedMessage;
    
    @Inject
    private SessionContext context;
    
    @Inject
    private GameControls controls;
    
    @Inject
    private SendMessageAnswerRequest answerRequest;
    
    @Inject
    private SendMessageRequest sendMessageRequest;
    
    @Inject
    private InteractionHandler handler;
    
    @Inject
    private SystemUtils systemUtils;
    
    @Inject
    private UserHandler userHandler;
    
    @PostConstruct
    private void setup() {
        this.setUser(context.getUser());
    }
    
    private int byusername(Supplier<User> u1, Supplier<User> u2) {
        return u1.get().getUsername().compareTo(u2.get().getUsername());
    }

    private UITable<MessageTO> buildSendMessages(Supplier<Collection<MessageTO>> supplier) {
        final UITable.Builder<MessageTO> builder = UITable.builder();
        return builder
            .add("To", (col, m1,m2) -> byusername(m1::getReceiver, m2::getReceiver))
            .add("Subject", (col,m1,m2) -> m1.getSubject().compareTo(m2.getSubject()))
            .add("Message")
            .add("Date", (col,m1,m2) -> m1.getSendDate().compareTo(m2.getSendDate()) )
            .build(supplier, this::sendmapper);
    }
    
    
    private UITable<MessageTO> buildReceivedMessages(Supplier<Collection<MessageTO>> supplier) {
        final UITable.Builder<MessageTO> builder = UITable.builder();
        return builder
            .add("From", (col, m1,m2) -> byusername(m1::getSender, m2::getSender))
            .add("Subject", (col,m1,m2) -> m1.getSubject().compareTo(m2.getSubject()))
            .add("Message")
            .add("Date", (col,m1,m2) -> m1.getSendDate().compareTo(m2.getSendDate()) )
            .build(supplier, this::receivedmapper);
    }
    
    private Row<MessageTO> receivedmapper(MessageTO message, Row<MessageTO> row) {
        return row
                .set("From", message, m -> message.getSender().getUsername())
                .set("Subject", message, MessageTO::getSubject)
                .set("Message", message, this::messageshorter)
                .set("Date", message, this::datesatring)
                
                .action("fas fa-eye", "view", this::viewMessage)
                .action("fas fa-times red", "delete", this::deleteMessage);
    }
    
    private Row<MessageTO> sendmapper(MessageTO message, Row<MessageTO> row) {
        return row
                .set("To", message, m -> message.getReceiver().getUsername())
                .set("Subject", message, MessageTO::getSubject)
                .set("Message", message, this::messageshorter)
                .set("Date", message, this::datesatring)
                
                .action("fas fa-eye", "view", this::viewMessage)
                .action("fas fa-times red", "delete", this::deleteMessage);
    }
    
    private void selectMessage(MessageTO message) {
        this.selectedMessage = Optional.ofNullable(message);
    }
    
    private String datesatring(MessageTO message) {
        final Date d = message.getSendDate();
        return new SimpleDateFormat("HH:mm MM/dd/yyyy").format(d);
    }
    
    private void viewMessage(MessageTO message) {
        this.selectMessage(message);
        this.answerRequest.setOriginalMessage(message);
        controls.setCurrentFocus(GameControls.VIEW_MESSAGE);
    }
    
    private String messageshorter(MessageTO message) {
        if(message.getMessage().length() < 40) {
            return message.getMessage();
        } else {
            return message.getMessage().substring(0, 37) + "...";
        }
    }
    
    public UITable<MessageTO> getSendMessages() {
        return Optional.ofNullable(this.userSendTable).map(UITable::updateData).orElse(null);
    }
    
    public UITable<MessageTO> getReceivedMessages() {
        return Optional.ofNullable(this.userReceivedTable).map(UITable::updateData).orElse(null);
    }
    
    public void setUser(User user) {
        this.target = Optional.ofNullable(user);
        this.target.ifPresent( u -> this.userSendTable = buildSendMessages(this::fetchSendData));
        this.target.ifPresent( u -> this.userReceivedTable = buildReceivedMessages(this::fetchReceivedData));
    }
    
    public User getUser() {
        return this.target.orElse(null);
    }
    
    public int getTotalReceivedMessages() {
        return this.target.map(User::getReceivedMessages).map(Set::size).orElse(0);
    }
    
    public int getTotalSendMessages() {
        return this.target.map(User::getSendMessages).map(Set::size).orElse(0);
    }
    
    public MessageTO getSelectedMessage() {
        return this.selectedMessage.orElse(null);
    }
    
    public void sendAnswer() {
        answerRequest.setOriginalMessage(getSelectedMessage());
        handler.getUser().answerMessage(answerRequest);
        backToMessages();
    }
    
    public void backToMessages() {
        controls.setCurrentFocus(GameControls.MESSAGES);;
    }
    
    public void deleteMessage(MessageTO message) {
        this.handler.getUser().deleteMessage(message);
        backToMessages();
    }
    
    public boolean isSelectedMessageReadOnly() {
        return this.selectedMessage.map(systemUtils::isSystemMessage).orElse(false);
    }
    
    public synchronized int getUnreadMessages() {
        if(target.isPresent()) {
            Optional<User> newUser = userHandler.find(target.map(User::getUsername).get());
            if(newUser.isPresent() && relevantChange(target.get(), newUser.get())) {
                target = newUser;
            }
        }
        return this.getTotalReceivedMessages(); //TODO: add uncread.
    }
    
    private boolean relevantChange(User u1, User u2) {
        boolean received = u1.getReceivedMessages().size() != u2.getReceivedMessages().size();
        return received;
    }
    
    public void messageTo(User user) {
        sendMessageRequest.setReceiverId(user.getUsername());
        this.controls.setCurrentFocus(GameControls.MESSAGES);
    }
    
    private synchronized List<MessageTO> fetchSendData() {
        return asSortedList(this.target.map(User::getSendMessages).orElseGet(Collections::emptySet));
    }
    
    private synchronized List<MessageTO> fetchReceivedData() {
        return asSortedList(this.target.map(User::getReceivedMessages).orElseGet(Collections::emptySet));
    }
    
    private List<MessageTO> asSortedList(Collection<MessageTO> messages) {
        final List<MessageTO> list = new ArrayList<>(messages);
        list.sort( (m1, m2) -> m2.getSendDate().compareTo(m2.getSendDate()) );
        return list;
    }
}
