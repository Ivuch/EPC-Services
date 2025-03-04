package com.hawaii.epc.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;
import java.util.List;

public class PersistenceService2 {

    private EntityManagerFactory emf;

    public PersistenceService2(String persistenceUnit) {
        // Use "PostgreSQL_PU" for PostgreSQL, "SQLite_PU" for SQLite
        emf = Persistence.createEntityManagerFactory(persistenceUnit);
    }

    public void close() {
        if (emf != null) {
            emf.close();
        }
    }

    @Transactional
    public <T> void persist(T entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        EntityManager em = emf.createEntityManager();
        List<T> results = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
        em.close();
        return results;
    }
}
