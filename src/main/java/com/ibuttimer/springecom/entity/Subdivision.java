package com.ibuttimer.springecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="subdivision")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Subdivision implements IEntity {

    public enum CountryFields {
        ID, COUNTRY, SUBDIV, NAME, LEVEL
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private Country country;

    @Column(name = "country_id")
    private Long country_id;

    @Column(name = "subdiv")
    private String subdiv;

    @Column(name = "name")
    private String name;

    @Column(name = "level")
    private String level;

    public static Subdivision of(Long id, Country country, String subdiv, String name, String level) {
        Subdivision subdivision = new Subdivision();

        subdivision.id = id;
        subdivision.country = country;
        subdivision.subdiv = subdiv;
        subdivision.name = name;
        subdivision.level = level;

        return subdivision;
    }

    public static Subdivision of(Country country, String subdiv, String name, String level) {
        return of(null, country, subdiv, name, level);
    }

    public static Subdivision of(Subdivision subdivision) {
        return of(subdivision.id, subdivision.country, subdivision.subdiv, subdivision.name, subdivision.level);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subdivision)) return false;

        Subdivision that = (Subdivision) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getCountry() != null ? !getCountry().equals(that.getCountry()) : that.getCountry() != null) return false;
        if (getSubdiv() != null ? !getSubdiv().equals(that.getSubdiv()) : that.getSubdiv() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getLevel() != null ? getLevel().equals(that.getLevel()) : that.getLevel() == null;
    }

    /**
     * Check object equality
     * @param o         object to check
     * @param excludes  list of fields to exclude
     * @return
     */
    public boolean equals(Object o, List<Enum<?>> excludes) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        Subdivision that = (Subdivision) o;

        boolean result = true;
        for (CountryFields field : CountryFields.values()) {
            if (excludes.contains(field)) {
                continue;
            }
            switch (field) {
                case ID:
                    result = getId().equals(that.getId());
                    break;
                case COUNTRY:
                case SUBDIV:
                case NAME:
                case LEVEL:
                    // nullable fields
                    result = Objects.equals(get(field), that.get(field));
                    break;
                default:
                    throw new UnsupportedOperationException("Field " + field + " not supported");
            }
            if (!result) {
                break;
            }
        }
        return result;
    }

    @Override
    public Object get(Enum<?> field) {
        Object result = null;
        if (CountryFields.ID.equals(field)) {
            result = getId();
        } else if (CountryFields.COUNTRY.equals(field)) {
            result = getCountry();
        } else if (CountryFields.SUBDIV.equals(field)) {
            result = getSubdiv();
        } else if (CountryFields.NAME.equals(field)) {
            result = getName();
        } else if (CountryFields.LEVEL.equals(field)) {
            result = getLevel();
        } else {
            throw new UnsupportedOperationException("Field " + field + " not supported");
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + (getSubdiv() != null ? getSubdiv().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getLevel() != null ? getLevel().hashCode() : 0);
        return result;
    }
}
