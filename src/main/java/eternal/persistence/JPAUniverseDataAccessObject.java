package eternal.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import eternal.game.environment.Sector;
import eternal.game.environment.Universe;
import eternal.util.ExceptionHandler;

@Named
@ApplicationScoped
public class JPAUniverseDataAccessObject implements UniverseDataAccessObject {
    
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
    public synchronized Optional<Universe> findUniverse(int id) {
        try {
            entityManager.clear();
            return Optional.ofNullable(entityManager.find(Universe.class, id));
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<Sector> findSector(int id) {
        try {
            entityManager.clear();
            return Optional.ofNullable(entityManager.find(Sector.class, id));
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return Optional.empty();
        }
    }

    @Override
    public synchronized List<Universe> loadAll() {
        try {
            entityManager.clear();
            return new ArrayList<>(entityManager.createQuery("SELECT u FROM Universe AS u", Universe.class).getResultList());
            
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return Collections.emptyList();
        }
    }

    @Override
    public synchronized boolean storeUniverse(Universe uni) {
        try {
            entityManager.clear();
            entityManager.getTransaction().begin();
            entityManager.persist(uni);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }
    
    @Override
    public boolean storeSector(Sector sector) {
        try {
            entityManager.clear();
            entityManager.getTransaction().begin();
            entityManager.persist(sector);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized boolean updateuniverse(Universe uni) {
        try {
            entityManager.clear();
            entityManager.getTransaction().begin();
            entityManager.merge(uni);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized boolean updateSector(Sector sector) {
        try {
            entityManager.clear();
            entityManager.getTransaction().begin();
            entityManager.merge(sector);
            entityManager.getTransaction().commit();
            entityManager.clear();
            return true;
        } catch(Exception e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public synchronized boolean deleteSector(int sectorId) {
        try {
            Sector tmp = entityManager.find(Sector.class, sectorId);
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
