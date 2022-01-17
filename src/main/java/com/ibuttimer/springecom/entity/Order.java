package com.ibuttimer.springecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Table(name = "orders", indexes = {
        @Index(name = "UK_billing_address_id", columnList = "billing_address_id", unique = true),
        @Index(name = "UK_shipping_address_id", columnList = "shipping_address_id", unique = true),
        @Index(name = "K_customer_id", columnList = "customer_id")
})
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Order implements IEntity {

    public enum OrderFields {
        ID, ORDER_TRACKING_NUMBER, TOTAL_PRICE, TOTAL_QUANTITY,
        BILLING_ADDRESS, CUSTOMER, SHIPPING_ADDRESS, STATUS,
        DATE_CREATED, LAST_UPDATED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_tracking_number")
    private String orderTrackingNumber;

    @Column(name = "total_price", precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "id")
    private Address billingAddress;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    private Address shippingAddress;

    @Column(name = "status", length = 128)
    private String status;

    @Column(name = "date_created")
    @CreationTimestamp
    private Instant dateCreated;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private Instant lastUpdated;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    @ToString.Exclude
    @JsonIgnore
    private Collection<OrderItem> orderItems;


    public boolean add(OrderItem orderItem) {
        boolean result = false;
        if (orderItem != null) {
            if (orderItems == null) {
                orderItems = new HashSet<>();
            }
            result = orderItems.add(orderItem);
            orderItem.setOrder(this);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        Order order = (Order) o;

        if (getId() != null ? !getId().equals(order.getId()) : order.getId() != null) return false;
        if (getOrderTrackingNumber() != null ? !getOrderTrackingNumber().equals(order.getOrderTrackingNumber()) : order.getOrderTrackingNumber() != null)
            return false;
        if (getTotalPrice() != null ? !getTotalPrice().equals(order.getTotalPrice()) : order.getTotalPrice() != null)
            return false;
        if (getTotalQuantity() != null ? !getTotalQuantity().equals(order.getTotalQuantity()) : order.getTotalQuantity() != null)
            return false;
        if (getBillingAddress() != null ? !getBillingAddress().equals(order.getBillingAddress()) : order.getBillingAddress() != null)
            return false;
        if (getCustomer() != null ? !getCustomer().equals(order.getCustomer()) : order.getCustomer() != null)
            return false;
        if (getShippingAddress() != null ? !getShippingAddress().equals(order.getShippingAddress()) : order.getShippingAddress() != null)
            return false;
        if (getStatus() != null ? !getStatus().equals(order.getStatus()) : order.getStatus() != null) return false;
        if (getDateCreated() != null ? !getDateCreated().equals(order.getDateCreated()) : order.getDateCreated() != null)
            return false;
        return getLastUpdated() != null ? getLastUpdated().equals(order.getLastUpdated()) : order.getLastUpdated() == null;
    }

    @Override
    public boolean equals(Object o, List<Enum<?>> excludes) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        Order that = (Order) o;

        boolean result = true;
        for (OrderFields field : OrderFields.values()) {
            if (excludes.contains(field)) {
                continue;
            }
            switch (field) {
                case ID:
                    result = getId().equals(that.getId());
                    break;
                case ORDER_TRACKING_NUMBER:
                case TOTAL_PRICE:
                case TOTAL_QUANTITY:
                case BILLING_ADDRESS:
                case CUSTOMER:
                case SHIPPING_ADDRESS:
                case STATUS:
                case DATE_CREATED:
                case LAST_UPDATED:
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
        if (OrderFields.ID.equals(field)) {
            result = getId();
        } else if (OrderFields.ORDER_TRACKING_NUMBER.equals(field)) {
            result = getOrderTrackingNumber();
        } else if (OrderFields.TOTAL_PRICE.equals(field)) {
            result = getTotalPrice();
        } else if (OrderFields.TOTAL_QUANTITY.equals(field)) {
            result = getTotalQuantity();
        } else if (OrderFields.BILLING_ADDRESS.equals(field)) {
            result = getBillingAddress();
        } else if (OrderFields.CUSTOMER.equals(field)) {
            result = getCustomer();
        } else if (OrderFields.SHIPPING_ADDRESS.equals(field)) {
            result = getShippingAddress();
        } else if (OrderFields.STATUS.equals(field)) {
            result = getStatus();
        } else if (OrderFields.DATE_CREATED.equals(field)) {
            result = getDateCreated();
        } else if (OrderFields.LAST_UPDATED.equals(field)) {
            result = getLastUpdated();
        } else {
            throw new UnsupportedOperationException("Field " + field + " not supported");
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getOrderTrackingNumber() != null ? getOrderTrackingNumber().hashCode() : 0);
        result = 31 * result + (getTotalPrice() != null ? getTotalPrice().hashCode() : 0);
        result = 31 * result + (getTotalQuantity() != null ? getTotalQuantity().hashCode() : 0);
        result = 31 * result + (getBillingAddress() != null ? getBillingAddress().hashCode() : 0);
        result = 31 * result + (getCustomer() != null ? getCustomer().hashCode() : 0);
        result = 31 * result + (getShippingAddress() != null ? getShippingAddress().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getDateCreated() != null ? getDateCreated().hashCode() : 0);
        result = 31 * result + (getLastUpdated() != null ? getLastUpdated().hashCode() : 0);
        return result;
    }
}