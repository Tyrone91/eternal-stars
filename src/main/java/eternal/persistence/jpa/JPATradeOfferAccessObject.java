package eternal.persistence.jpa;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import eternal.game.TradeOffer;
import eternal.persistence.PersistenceUnitNames;
import eternal.persistence.TradeOfferAccessObject;
import eternal.util.ExceptionHandler;

@Named
@ApplicationScoped
public class JPATradeOfferAccessObject implements TradeOfferAccessObject {
    
    @Inject
    private ExceptionHandler exceptionHandler;
    
    private EntityManager entityManager;
    
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
    public synchronized boolean storeOffer(TradeOffer offer) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(offer);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized boolean updateOffer(TradeOffer offer) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(offer);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized Optional<TradeOffer> findOffer(String id) {
        try {
            entityManager.clear();
            return Optional.ofNullable(entityManager.find(TradeOffer.class, id));
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteOffer(TradeOffer offer) {
        try {
            entityManager.getTransaction().begin();
            final TradeOffer toDelete = entityManager.merge(offer);
            entityManager.remove(toDelete);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public boolean storeOfferAndUpdateUser(TradeOffer offer) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(offer);
            entityManager.merge(offer.getInitiator());
            entityManager.merge(offer.getReceiver());
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            try {                
                entityManager.getTransaction().rollback();
            } catch(Exception ex) {
                exceptionHandler.handleException(ex);
            }
            exceptionHandler.handleException(e);
            return false;
        }
    }

}
