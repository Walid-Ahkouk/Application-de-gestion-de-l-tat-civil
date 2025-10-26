package org.example.civil.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.civil.dao.IDao;
import org.example.civil.entity.Femme;
import org.example.civil.entity.Homme;
import org.example.civil.entity.Mariage;
import org.example.civil.util.HibernateUtil;

import java.util.Date;
import java.util.List;

public class HommeService implements IDao<Homme> {

    private EntityManager entityManager;

    public HommeService() {
        this.entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
    }

    @Override
    public void create(Homme o) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(o);
        transaction.commit();
    }

    @Override
    public Homme findById(int id) {
        return entityManager.find(Homme.class, id);
    }

    @Override
    public List<Homme> findAll() {
        return entityManager.createQuery("FROM Homme", Homme.class).getResultList();
    }

    /**
     * JPQL: Retourne les épouses d'un homme dont le mariage a débuté entre deux dates.
     */
    public List<Femme> findEpousesBetweenDates(int hommeId, Date d1, Date d2) {
        TypedQuery<Femme> query = entityManager.createQuery(
                "SELECT m.femme FROM Mariage m WHERE m.homme.id = :hommeId AND m.dateDebut BETWEEN :d1 AND :d2", Femme.class);
        query.setParameter("hommeId", hommeId);
        query.setParameter("d1", d1);
        query.setParameter("d2", d2);
        return query.getResultList();
    }

    /**
     * JPQL: Affiche les détails des mariages d'un homme.
     */
    public void displayMariagesDetails(int hommeId) {
        Homme homme = findById(hommeId);
        if (homme == null) {
            System.out.println("Aucun homme trouvé avec l'ID: " + hommeId);
            return;
        }

        System.out.println("Mariages de " + homme.getPrenom() + " " + homme.getNom() + " :");
        TypedQuery<Mariage> query = entityManager.createQuery("FROM Mariage m WHERE m.homme.id = :hommeId", Mariage.class);
        query.setParameter("hommeId", hommeId);
        List<Mariage> mariages = query.getResultList();

        for (Mariage m : mariages) {
            String status = (m.getDateFin() == null) ? "En cours" : "Échoué";
            System.out.println("  - Avec: " + m.getFemme().getPrenom() + " " + m.getFemme().getNom() +
                    ", Date début: " + m.getDateDebut() + ", Statut: " + status);
        }
    }
}
