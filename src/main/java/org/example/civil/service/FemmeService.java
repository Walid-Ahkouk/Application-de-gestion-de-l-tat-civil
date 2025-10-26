package org.example.civil.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.example.civil.dao.IDao;
import org.example.civil.entity.Femme;
import org.example.civil.entity.Homme;
import org.example.civil.entity.Mariage;
import org.example.civil.util.HibernateUtil;

import java.util.Date;
import java.util.List;

public class FemmeService implements IDao<Femme> {

    private EntityManager entityManager;

    public FemmeService() {
        this.entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
    }

    @Override
    public void create(Femme o) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(o);
        transaction.commit();
    }

    @Override
    public Femme findById(int id) {
        return entityManager.find(Femme.class, id);
    }

    @Override
    public List<Femme> findAll() {
        return entityManager.createQuery("FROM Femme", Femme.class).getResultList();
    }

    /**
     * Named Native Query: Calcule le nombre total d'enfants pour une femme entre deux dates.
     */
    public Long getNombreEnfantsBetweenDates(int femmeId, Date d1, Date d2) {
        TypedQuery<Long> query = entityManager.createNamedQuery("Femme.getNombreEnfantsBetweenDates", Long.class);
        query.setParameter("femmeId", femmeId);
        query.setParameter("date1", d1);
        query.setParameter("date2", d2);
        Long result = query.getSingleResult();
        return result != null ? result : 0L;
    }

    /**
     * Named JPQL Query: Retourne les femmes mariées au moins deux fois.
     */
    public List<Femme> findFemmesMarieesAuMoinsDeuxFois() {
        TypedQuery<Femme> query = entityManager.createNamedQuery("Femme.findFemmesMarieesAuMoinsDeuxFois", Femme.class);
        return query.getResultList();
    }

    /**
     * Criteria API: Compte le nombre d'hommes mariés à exactement quatre femmes entre deux dates.
     */
    public Long countHommesMariesAQuatreFemmes(Date d1, Date d2) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Mariage> mariageRoot = cq.from(Mariage.class);

        cq.select(cb.countDistinct(mariageRoot.get("homme")))
          .where(cb.isNotNull(mariageRoot.get("homme")));

        Subquery<Integer> subquery = cq.subquery(Integer.class);
        Root<Mariage> subqueryMariageRoot = subquery.from(Mariage.class);
        subquery.select(subqueryMariageRoot.get("homme").get("id"))
                .where(cb.between(subqueryMariageRoot.get("dateDebut"), d1, d2))
                .groupBy(subqueryMariageRoot.get("homme").get("id"))
                .having(cb.equal(cb.count(subqueryMariageRoot.get("femme")), 4L));

        cq.where(mariageRoot.get("homme").get("id").in(subquery));

        return entityManager.createQuery(cq).getSingleResult();
    }

    /**
     * JPQL: Retourne la femme la plus âgée.
     */
    public Femme findFemmeLaPlusAgee() {
        TypedQuery<Femme> query = entityManager.createQuery(
                "SELECT f FROM Femme f ORDER BY f.dateNaissance ASC", Femme.class);
        query.setMaxResults(1);
        return query.getSingleResult();
    }
}
