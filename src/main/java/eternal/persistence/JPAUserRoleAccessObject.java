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

import eternal.user.UserRole;
import eternal.util.ExceptionHandler;

@Named
@ApplicationScoped
public class JPAUserRoleAccessObject implements UserRoleAccessObject {

    @Inject
    private ExceptionHandler exceptionHandler;
    
    private EntityManager entityManager;

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
    public synchronized void storeRole(UserRole role) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(role);
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch(Exception e) {
            entityManager.clear();
            exceptionHandler.handleException(e);
        }
    }
    
    
    @Override
    public synchronized Set<UserRole> getAllRoles() {
        try {
            return new HashSet<>(entityManager.createQuery("SELECT role FROM UserRole AS role", UserRole.class).getResultList());
        } catch(Exception e) {
            entityManager.clear();
            exceptionHandler.handleException(e);
            return Collections.emptySet();
        }
    }
    
    @Override
    public synchronized Optional<UserRole> getRole(String roleName) {
        try {
            return Optional.of(entityManager.find(UserRole.class, roleName));
        } catch(Exception e) {
            entityManager.clear();
            exceptionHandler.handleException(e);
            return Optional.empty();
        }
    }

    @Override
    public synchronized boolean updateRole(UserRole role) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(role);
            entityManager.getTransaction().commit();
            return true;
        } catch(Exception e) {
            entityManager.clear();
            exceptionHandler.handleException(e);
            return false;
        }
    }
    
    @Override
    public synchronized boolean refreshRole(UserRole role) {
        try {
            /*
            entityManager.getTransaction().begin();
            entityManager.refresh(role);
            entityManager.getTransaction().commit();
            */
            return true;
        } catch(Exception e) {
            entityManager.clear();
            exceptionHandler.handleException(e);
            return false;
        }
    }
}
