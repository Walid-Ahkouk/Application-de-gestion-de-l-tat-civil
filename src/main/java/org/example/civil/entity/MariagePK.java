package org.example.civil.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MariagePK implements Serializable {

    private int hommeId;
    private int femmeId;

    public MariagePK() {}

    public MariagePK(int hommeId, int femmeId) {
        this.hommeId = hommeId;
        this.femmeId = femmeId;
    }

    // Getters, Setters, equals, and hashCode
    public int getHommeId() {
        return hommeId;
    }

    public void setHommeId(int hommeId) {
        this.hommeId = hommeId;
    }

    public int getFemmeId() {
        return femmeId;
    }

    public void setFemmeId(int femmeId) {
        this.femmeId = femmeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MariagePK mariagePK = (MariagePK) o;
        return hommeId == mariagePK.hommeId && femmeId == mariagePK.femmeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hommeId, femmeId);
    }
}
