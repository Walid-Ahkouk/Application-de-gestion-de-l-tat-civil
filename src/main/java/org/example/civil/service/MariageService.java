package org.example.civil.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.civil.dao.IDao;
import org.example.civil.entity.Mariage;
import org.example.civil.util.HibernateUtil;

import java.util.List;

public class MariageService implements IDao<Mariage> {

    private EntityManager entityManager;

    public MariageService() {
        this.entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
    }

    // L'ID est une clé composée, donc findById(int) n'est pas applicable.
    // On pourrait implémenter une méthode findById(MariagePK id).

    @Override
    public void create(Mariage o) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(o);
        transaction.commit();
    }

    @Override
    public Mariage findById(int id) {
        // Non pertinent pour une clé composée
        throw new UnsupportedOperationException("findById(int) not supported, use findById(MariagePK id)");
    }

    @Override
    public List<Mariage> findAll() {
        return entityManager.createQuery("FROM Mariage", Mariage.class).getResultList();
    }
}
