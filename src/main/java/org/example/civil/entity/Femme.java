package org.example.civil.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("F")
@NamedQuery(name = "Femme.findFemmesMarieesAuMoinsDeuxFois", 
            query = "SELECT m.femme FROM Mariage m GROUP BY m.femme HAVING COUNT(m) >= 2")
@NamedNativeQuery(
    name = "Femme.getNombreEnfantsBetweenDates", 
    query = "SELECT SUM(m.nbrEnfant) as total_enfants FROM mariage m WHERE m.femme_id = :femmeId AND m.dateDebut BETWEEN :date1 AND :date2",
    resultSetMapping = "ScalarLong"
)
@SqlResultSetMapping(
    name = "ScalarLong",
    columns = @ColumnResult(name = "total_enfants", type = Long.class)
)
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Mariage> mariages;

    public Femme() {}

    public Femme(String nom, String prenom, Date dateNaissance) {
        super(nom, prenom, dateNaissance);
    }

    public List<Mariage> getMariages() {
        return mariages;
    }

    public void setMariages(List<Mariage> mariages) {
        this.mariages = mariages;
    }
}
