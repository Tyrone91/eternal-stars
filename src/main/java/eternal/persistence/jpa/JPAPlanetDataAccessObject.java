package eternal.persistence.jpa;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import eternal.game.environment.Planet;
import eternal.persistence.PersistenceUnitNames;
import eternal.persistence.PlanetDataAccessObject;
import eternal.util.ExceptionHandler;

@Named 
@ApplicationScoped
public class JPAPlanetDataAccessObject implements PlanetDataAccessObject {
    
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
    public synchronized boolean storePlanet(Planet planet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(planet);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized boolean updatePlanet(Planet planet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(planet);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized Optional<Planet> findPlanet(int id) {
        try {
            entityManager.clear();
            return Optional.ofNullable(entityManager.find(Planet.class, id));
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return Optional.empty();
        }
    }

    @Override
    public boolean deletePlanet(int id) {
        try {
            Planet tmp = entityManager.find(Planet.class, id);
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
