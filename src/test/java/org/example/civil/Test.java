package org.example.civil;

import jakarta.persistence.EntityManager;
import org.example.civil.entity.Femme;
import org.example.civil.entity.Homme;
import org.example.civil.entity.Mariage;
import org.example.civil.service.FemmeService;
import org.example.civil.service.HommeService;
import org.example.civil.service.MariageService;
import org.example.civil.util.HibernateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Test {

    public static void main(String[] args) throws ParseException {
        // Initialisation des services
        HommeService hommeService = new HommeService();
        FemmeService femmeService = new FemmeService();
        MariageService mariageService = new MariageService();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // --- PHASE 1: Création des données de test ---
        System.out.println("Création des données de test...");

        // Hommes
        Homme h1 = new Homme("Dupont", "Jean", sdf.parse("1980-05-10"));
        Homme h2 = new Homme("Martin", "Pierre", sdf.parse("1975-11-20"));
        Homme h3 = new Homme("Bernard", "Paul", sdf.parse("1990-01-15"));
        Homme h4 = new Homme("Dubois", "Jacques", sdf.parse("1982-03-25"));
        Homme h5 = new Homme("Moreau", "Louis", sdf.parse("1988-07-07"));

        // Femmes
        Femme f1 = new Femme("Petit", "Marie", sdf.parse("1985-02-12"));
        Femme f2 = new Femme("Robert", "Julie", sdf.parse("1987-09-30"));
        Femme f3 = new Femme("Richard", "Camille", sdf.parse("1992-04-05"));
        Femme f4 = new Femme("Durand", "Isabelle", sdf.parse("1973-12-01")); // La plus âgée
        Femme f5 = new Femme("Leroy", "Sophie", sdf.parse("1989-06-18"));
        Femme f6 = new Femme("Girard", "Nathalie", sdf.parse("1991-08-22"));
        Femme f7 = new Femme("Bonnet", "Émilie", sdf.parse("1984-10-03"));
        Femme f8 = new Femme("Roux", "Céline", sdf.parse("1993-03-14")); // Sera mariée 2 fois
        Femme f9 = new Femme("Vincent", "Audrey", sdf.parse("1986-11-11"));
        Femme f10 = new Femme("Lefevre", "Laura", sdf.parse("1990-05-29"));

        // Persistance des personnes
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        em.persist(h1); em.persist(h2); em.persist(h3); em.persist(h4); em.persist(h5);
        em.persist(f1); em.persist(f2); em.persist(f3); em.persist(f4); em.persist(f5);
        em.persist(f6); em.persist(f7); em.persist(f8); em.persist(f9); em.persist(f10);
        em.getTransaction().commit();

        // Mariages
        // h1: 2 mariages (1 en cours, 1 échoué)
        Mariage m1 = new Mariage(h1, f1, sdf.parse("2005-06-20"), sdf.parse("2010-01-15"), 2);
        Mariage m2 = new Mariage(h1, f2, sdf.parse("2012-08-10"), null, 1);

        // h2: 4 mariages (pour le test de la méthode Criteria)
        Mariage m3 = new Mariage(h2, f3, sdf.parse("2015-01-01"), null, 0);
        Mariage m4 = new Mariage(h2, f5, sdf.parse("2016-02-02"), null, 1);
        Mariage m5 = new Mariage(h2, f6, sdf.parse("2017-03-03"), null, 2);
        Mariage m6 = new Mariage(h2, f7, sdf.parse("2018-04-04"), null, 0);

        // f8: mariée 2 fois
        Mariage m7 = new Mariage(h3, f8, sdf.parse("2019-05-05"), null, 3);
        Mariage m8 = new Mariage(h4, f8, sdf.parse("2022-06-06"), null, 0);
        
        // Mariage pour f9 pour tester le nombre d'enfants
        Mariage m9 = new Mariage(h5, f9, sdf.parse("2014-01-01"), sdf.parse("2020-01-01"), 4);

        // Persistance des mariages
        em.getTransaction().begin();
        em.persist(m1); em.persist(m2); em.persist(m3); em.persist(m4); em.persist(m5);
        em.persist(m6); em.persist(m7); em.persist(m8); em.persist(m9);
        em.getTransaction().commit();

        System.out.println("Données créées avec succès.");
        System.out.println("\n--- PHASE 2: Exécution des requêtes spécifiques ---");

        // 1. (HommeService) Épouses de h1 entre 2011 et 2013
        System.out.println("\n1. Épouses de " + h1.getPrenom() + " (mariage entre 2011-2013) :\n");
        List<Femme> epouses = hommeService.findEpousesBetweenDates(h1.getId(), sdf.parse("2011-01-01"), sdf.parse("2013-12-31"));
        epouses.forEach(e -> System.out.println("  - " + e.getPrenom() + " " + e.getNom()));

        // 2. (HommeService) Détails des mariages de h1
        System.out.println("\n2. Détails des mariages pour " + h1.getPrenom() + " :\n");
        hommeService.displayMariagesDetails(h1.getId());

        // 3. (FemmeService) Nombre d'enfants de f9 entre 2013 et 2015
        System.out.println("\n3. Nombre d'enfants pour " + f9.getPrenom() + " (mariage entre 2013-2015) :\n");
        Long nbrEnfants = femmeService.getNombreEnfantsBetweenDates(f9.getId(), sdf.parse("2013-01-01"), sdf.parse("2015-12-31"));
        System.out.println("  - Total: " + nbrEnfants);

        // 4. (FemmeService) Femmes mariées au moins deux fois
        System.out.println("\n4. Femmes mariées au moins deux fois :\n");
        List<Femme> femmesMariees2Fois = femmeService.findFemmesMarieesAuMoinsDeuxFois();
        femmesMariees2Fois.forEach(f -> System.out.println("  - " + f.getPrenom() + " " + f.getNom()));

        // 5. (FemmeService) Hommes mariés à 4 femmes entre 2014 et 2019
        System.out.println("\n5. Nombre d'hommes mariés à 4 femmes (mariages entre 2014-2019) :\n");
        Long countHommes = femmeService.countHommesMariesAQuatreFemmes(sdf.parse("2014-01-01"), sdf.parse("2019-12-31"));
        System.out.println("  - Nombre d'hommes: " + countHommes);

        // 6. (FemmeService) Femme la plus âgée
        System.out.println("\n6. Femme la plus âgée :\n");
        Femme femmeAgee = femmeService.findFemmeLaPlusAgee();
        System.out.println("  - " + femmeAgee.getPrenom() + " " + femmeAgee.getNom() + " (née le " + sdf.format(femmeAgee.getDateNaissance()) + ")");

        // --- PHASE 3: Nettoyage ---
        em.close();
        HibernateUtil.shutdown();
        System.out.println("\nProgramme terminé.");
    }
}
