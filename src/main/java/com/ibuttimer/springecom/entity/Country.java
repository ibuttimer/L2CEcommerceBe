package com.ibuttimer.springecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="country")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Country implements IEntity {

    public enum CountryFields {
        ID, CODE, NAME
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "country")
    @ToString.Exclude
    @JsonIgnore
    private Collection<Subdivision> subdivisions;

    public static Country of(Long id, String code, String name) {
        Country country = new Country();

        country.id = id;
        country.code = code;
        country.name = name;

        return country;
    }

    public static Country of(String code, String name) {
        return of(null, code, name);
    }

    public static Country of(Country country) {
        return of(country.id, country.code, country.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        Country product = (Country) o;

        if (!getId().equals(product.getId())) return false;
        if (getCode() != null ? !getCode().equals(product.getCode()) : product.getCode() != null) return false;
        return getName() != null ? getName().equals(product.getName()) : product.getName() == null;
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

        Country that = (Country) o;

        boolean result = true;
        for (CountryFields field : CountryFields.values()) {
            if (excludes.contains(field)) {
                continue;
            }
            switch (field) {
                case ID:
                    result = getId().equals(that.getId());
                    break;
                case CODE:
                case NAME:
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
        } else if (CountryFields.CODE.equals(field)) {
            result = getCode();
        } else if (CountryFields.NAME.equals(field)) {
            result = getName();
        } else {
            throw new UnsupportedOperationException("Field " + field + " not supported");
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
