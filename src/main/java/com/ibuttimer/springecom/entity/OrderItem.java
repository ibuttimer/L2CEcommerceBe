package com.ibuttimer.springecom.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Table(name = "order_item", indexes = {
        @Index(name = "K_order_id", columnList = "order_id")
})
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OrderItem implements IEntity {

    public enum OrderItemFields {
        ID, IMAGE_URL, QUANTITY, UNIT_PRICE, ORDER, PRODUCT, PRODUCT_ID
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price", precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name="product_id")
    private Long productId;

//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;

        OrderItem orderItem = (OrderItem) o;

        if (getId() != null ? !getId().equals(orderItem.getId()) : orderItem.getId() != null) return false;
        if (getImageUrl() != null ? !getImageUrl().equals(orderItem.getImageUrl()) : orderItem.getImageUrl() != null)
            return false;
        if (getQuantity() != null ? !getQuantity().equals(orderItem.getQuantity()) : orderItem.getQuantity() != null)
            return false;
        if (getUnitPrice() != null ? !getUnitPrice().equals(orderItem.getUnitPrice()) : orderItem.getUnitPrice() != null)
            return false;
        if (getOrder() != null ? !getOrder().equals(orderItem.getOrder()) : orderItem.getOrder() != null) return false;
//        if (getProduct() != null ? !getProduct().equals(orderItem.getProduct()) : orderItem.getProduct() != null) return false;
        return getProductId() != null ? getProductId().equals(orderItem.getProductId()) : orderItem.getProductId() == null;
    }

    @Override
    public boolean equals(Object o, List<Enum<?>> excludes) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        OrderItem that = (OrderItem) o;

        boolean result = true;
        for (OrderItemFields field : OrderItemFields.values()) {
            if (excludes.contains(field)) {
                continue;
            }
            switch (field) {
                case ID:
                    result = getId().equals(that.getId());
                    break;
                case IMAGE_URL:
                case QUANTITY:
                case UNIT_PRICE:
                case ORDER:
                case PRODUCT:
                case PRODUCT_ID:
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
        if (OrderItemFields.ID.equals(field)) {
            result = getId();
        } else if (OrderItemFields.IMAGE_URL.equals(field)) {
            result = getImageUrl();
        } else if (OrderItemFields.QUANTITY.equals(field)) {
            result = getQuantity();
        } else if (OrderItemFields.UNIT_PRICE.equals(field)) {
            result = getUnitPrice();
        } else if (OrderItemFields.ORDER.equals(field)) {
            result = getOrder();
        } else if (OrderItemFields.PRODUCT.equals(field)) {
//            result = getProduct();
        } else if (OrderItemFields.PRODUCT_ID.equals(field)) {
            result = getProductId();
        } else {
            throw new UnsupportedOperationException("Field " + field + " not supported");
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getImageUrl() != null ? getImageUrl().hashCode() : 0);
        result = 31 * result + (getQuantity() != null ? getQuantity().hashCode() : 0);
        result = 31 * result + (getUnitPrice() != null ? getUnitPrice().hashCode() : 0);
        result = 31 * result + (getOrder() != null ? getOrder().hashCode() : 0);
//        result = 31 * result + (getProduct() != null ? getProduct().hashCode() : 0);
        result = 31 * result + (getProductId() != null ? getProductId().hashCode() : 0);
        return result;
    }
}