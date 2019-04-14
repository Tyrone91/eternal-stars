package eternal.persistence;

import java.util.Optional;

import eternal.persistence.data.MessageTO;

public interface MessageDataAccessObject {
    
    public Optional<MessageTO> find(String id);
    public boolean delete(String id);
    public boolean update(MessageTO message);
    public boolean store(MessageTO message);
    
    public boolean storeMessageAndUpdateUser(MessageTO message, boolean ignoreSender);
    public boolean deleteMessageAndUpdateUser(MessageTO message);
    
    
}
