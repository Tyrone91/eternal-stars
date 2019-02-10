package eternal.mangement;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import eternal.user.User;

@Named
@ApplicationScoped
public class UserDataAccessObject {
    
    private EntityManager entityManager;
    
    public UserDataAccessObject() {
        
    }
    
    @PostConstruct
    public void init() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("user-db");
        entityManager = factory.createEntityManager();
    }
    
    public synchronized boolean storeUser(User user) {
        EntityTransaction trans = entityManager.getTransaction();
        trans.begin();
        entityManager.persist(user);
        trans.commit();
        return true;
    }
    
    public synchronized User loadUser(String username) {
        EntityTransaction trans = entityManager.getTransaction();
        trans.begin();
        
        entityManager.find(User.class, username);
        
        
        trans.commit();
        return null;
    }
    
}
