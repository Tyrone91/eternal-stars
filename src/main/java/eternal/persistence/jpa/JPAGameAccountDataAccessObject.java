package eternal.persistence.jpa;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import eternal.game.control.GameAccount;
import eternal.persistence.GameAccountDataAccessObject;
import eternal.persistence.PersistenceUnitNames;
import eternal.util.ExceptionHandler;

@Named 
@ApplicationScoped
public class JPAGameAccountDataAccessObject implements GameAccountDataAccessObject {
    
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
    public synchronized Optional<GameAccount> findAccount(String user) {
        try {
            entityManager.clear();
            return Optional.ofNullable(entityManager.find(GameAccount.class, user));
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return Optional.empty();
        }
    }

    @Override
    public synchronized boolean storeAccount(GameAccount account) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(account);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized boolean updateAccount(GameAccount account) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(account);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized boolean deleteAccount(String account) {
        try {
            GameAccount tmp = entityManager.find(GameAccount.class, account);
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

}
