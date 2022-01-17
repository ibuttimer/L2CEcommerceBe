package com.ibuttimer.springecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Table(name = "customer")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Customer implements IEntity {

    public enum CustomerFields {
        ID, FIRST_NAME, LAST_NAME, EMAIL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @ToString.Exclude
    @JsonIgnore
    private Collection<Order> orders;

    public boolean add(Order order) {
        boolean result = false;
        if (order != null) {
            if (orders == null) {
                orders = new HashSet<>();
            }
            result = orders.add(order);
            order.setCustomer(this);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        Customer customer = (Customer) o;

        if (getId() != null ? !getId().equals(customer.getId()) : customer.getId() != null) return false;
        if (getFirstName() != null ? !getFirstName().equals(customer.getFirstName()) : customer.getFirstName() != null)
            return false;
        if (getLastName() != null ? !getLastName().equals(customer.getLastName()) : customer.getLastName() != null)
            return false;
        return getEmail() != null ? getEmail().equals(customer.getEmail()) : customer.getEmail() == null;
    }

    @Override
    public boolean equals(Object o, List<Enum<?>> excludes) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        Customer that = (Customer) o;

        boolean result = true;
        for (CustomerFields field : CustomerFields.values()) {
            if (excludes.contains(field)) {
                continue;
            }
            switch (field) {
                case ID:
                    result = getId().equals(that.getId());
                    break;
                case FIRST_NAME:
                case LAST_NAME:
                case EMAIL:
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
        if (CustomerFields.ID.equals(field)) {
            result = getId();
        } else if (CustomerFields.FIRST_NAME.equals(field)) {
            result = getFirstName();
        } else if (CustomerFields.LAST_NAME.equals(field)) {
            result = getLastName();
        } else if (CustomerFields.EMAIL.equals(field)) {
            result = getEmail();
        } else {
            throw new UnsupportedOperationException("Field " + field + " not supported");
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        return result;
    }
}