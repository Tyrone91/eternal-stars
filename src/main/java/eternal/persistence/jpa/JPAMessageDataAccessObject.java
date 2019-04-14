package eternal.persistence.jpa;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import eternal.persistence.MessageDataAccessObject;
import eternal.persistence.PersistenceUnitNames;
import eternal.persistence.data.MessageTO;
import eternal.util.ExceptionHandler;

@Named
@ApplicationScoped
public class JPAMessageDataAccessObject implements MessageDataAccessObject {
    
    private EntityManager entityManager;
    
    @Inject
    private ExceptionHandler exceptionHandler;
    
    @PostConstruct
    public void init() {
        try {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory(PersistenceUnitNames.GAME_UNIT_NAME);
            entityManager = factory.createEntityManager();
        } catch(Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    @Override
    public synchronized Optional<MessageTO> find(String id) {
        try {
            entityManager.clear();
            return Optional.ofNullable(entityManager.find(MessageTO.class, id));
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return Optional.empty();
        }
    }

    @Override
    public synchronized boolean delete(String id) {
        try {
            final MessageTO tmp = entityManager.find(MessageTO.class, id);
            if(tmp == null) {
                return false;
            }
            entityManager.getTransaction().begin();
            entityManager.remove(tmp);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized boolean update(MessageTO message) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(message);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized boolean store(MessageTO message) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(message);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized boolean storeMessageAndUpdateUser(MessageTO message, boolean ignoreSender) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(message);
            entityManager.merge(message.getReceiver());
            if(!ignoreSender) {                
                entityManager.merge(message.getSender());
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized boolean deleteMessageAndUpdateUser(MessageTO message) {
        try {
            final MessageTO tmp = entityManager.find(MessageTO.class, message.getId());
            if(tmp == null) {
                return false;
            }
            
            tmp.getReceiver().removeReceivedMessage(message);
            tmp.getSender().removeSendMessage(message);
            
            entityManager.getTransaction().begin();
            entityManager.merge(tmp.getReceiver());
            entityManager.merge(tmp.getSender());
            entityManager.remove(tmp);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

}
