package com.ibuttimer.springecom.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Table(name = "address")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Address implements IEntity {

    public enum AddressFields {
        ID, CITY, COUNTRY, STATE, STREET, ZIP_CODE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "street")
    private String street;

    @Column(name = "zip_code")
    private String zipCode;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        Address address = (Address) o;

        if (getId() != null ? !getId().equals(address.getId()) : address.getId() != null) return false;
        if (getCity() != null ? !getCity().equals(address.getCity()) : address.getCity() != null) return false;
        if (getCountry() != null ? !getCountry().equals(address.getCountry()) : address.getCountry() != null)
            return false;
        if (getState() != null ? !getState().equals(address.getState()) : address.getState() != null) return false;
        if (getStreet() != null ? !getStreet().equals(address.getStreet()) : address.getStreet() != null) return false;
        return getZipCode() != null ? getZipCode().equals(address.getZipCode()) : address.getZipCode() == null;
    }

    @Override
    public boolean equals(Object o, List<Enum<?>> excludes) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        Address that = (Address) o;

        boolean result = true;
        for (AddressFields field : AddressFields.values()) {
            if (excludes.contains(field)) {
                continue;
            }
            switch (field) {
                case ID:
                    result = getId().equals(that.getId());
                    break;
                case CITY:
                case COUNTRY:
                case STATE:
                case STREET:
                case ZIP_CODE:
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
        if (AddressFields.ID.equals(field)) {
            result = getId();
        } else if (AddressFields.CITY.equals(field)) {
            result = getCity();
        } else if (AddressFields.COUNTRY.equals(field)) {
            result = getCountry();
        } else if (AddressFields.STATE.equals(field)) {
            result = getState();
        } else if (AddressFields.STREET.equals(field)) {
            result = getStreet();
        } else if (AddressFields.ZIP_CODE.equals(field)) {
            result = getZipCode();
        } else {
            throw new UnsupportedOperationException("Field " + field + " not supported");
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + (getState() != null ? getState().hashCode() : 0);
        result = 31 * result + (getStreet() != null ? getStreet().hashCode() : 0);
        result = 31 * result + (getZipCode() != null ? getZipCode().hashCode() : 0);
        return result;
    }
}