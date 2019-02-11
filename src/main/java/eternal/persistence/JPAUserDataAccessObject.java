package eternal.persistence;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import eternal.user.User;
import eternal.util.ExceptionHandler;

@Named
@ApplicationScoped
public class JPAUserDataAccessObject implements UserDataAccessObject {
    
    //@PersistenceContext(unitName = "eternal-stars-user-db",  type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    
    @Inject
    private ExceptionHandler exceptionHandler;
    
    public JPAUserDataAccessObject() {
        
    }
    
    @PostConstruct
    public void init() {
        try {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory(PersistenceUnitNames.USER_UNIT_NAME);
            entityManager = factory.createEntityManager();
        } catch(Exception e) {
            exceptionHandler.handleException(e);
        }
        
    }
    
    @Override
    public synchronized Set<User> fetchAllUsers() {
        System.out.println("fetching all user");
        try {
            return new HashSet<>(entityManager.createQuery("SELECT u FROM User AS u", User.class).getResultList());
            
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return Collections.emptySet();
        }
        
    }
   
    @Override
    public synchronized boolean storeUser(User user) {
        try {
            
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.flush();
            entityManager.getTransaction().commit();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }
    
    @Override
    public synchronized Optional<User> loadUser(String username) {
        Optional<User> res = Optional.ofNullable(entityManager.find(User.class, username));
        return res;
    }

    @Override
    public boolean deleteUser(String username) {
        try {
            loadUser(username).ifPresent( u -> {
                entityManager.getTransaction().begin();
                entityManager.remove(u);
                entityManager.getTransaction().commit();
            });
            return !loadUser(username).isPresent(); //TODO: performance noooot good
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.flush();
            entityManager.getTransaction().commit();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }
    
}

