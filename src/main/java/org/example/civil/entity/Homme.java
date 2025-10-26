package org.example.civil.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("H")
public class Homme extends Personne {

    @OneToMany(mappedBy = "homme", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Mariage> mariages;

    public Homme() {}

    public Homme(String nom, String prenom, Date dateNaissance) {
        super(nom, prenom, dateNaissance);
    }

    public List<Mariage> getMariages() {
        return mariages;
    }

    public void setMariages(List<Mariage> mariages) {
        this.mariages = mariages;
    }
}
