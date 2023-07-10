package com.ibuttimer.springecom.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="product")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Product implements IEntity {

    public enum ProductFields {
        ID, SKU, NAME, DESCRIPTION, UNIT_PRICE, IMAGE_URL, ACTIVE, UNITS_IN_STOCK, DATE_CREATED, LAST_UPDATED,
        CATEGORY
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku")
    private String sku;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "active")
    private boolean active;

    @Column(name = "units_in_stock")
    private int unitsInStock;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDate dateCreated;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private LocalDate lastUpdated;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;


    public static Product of(Long id, String sku, String name, String description, BigDecimal unitPrice, String imageUrl,
                             boolean active, int unitsInStock, LocalDate dateCreated, LocalDate lastUpdated, ProductCategory category) {
        Product prod = new Product();

        prod.id = id;
        prod.sku = sku;
        prod.name = name;
        prod.description = description;
        prod.unitPrice = unitPrice;
        prod.imageUrl = imageUrl;
        prod.active = active;
        prod.unitsInStock = unitsInStock;
        prod.dateCreated = dateCreated;
        prod.lastUpdated = lastUpdated;
        prod.category = category;

        return prod;
    }

    public static Product of(String sku, String name, String description, BigDecimal unitPrice, String imageUrl,
                             boolean active, int unitsInStock, LocalDate dateCreated, LocalDate lastUpdated, ProductCategory category) {
        return of(null, sku, name, description, unitPrice, imageUrl, active, unitsInStock, dateCreated, lastUpdated, category);
    }

    public static Product of(Product prod) {
        return of(prod.id, prod.sku, prod.name, prod.description, prod.unitPrice, prod.imageUrl, prod.active,
                prod.unitsInStock, prod.dateCreated, prod.lastUpdated, prod.category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        Product product = (Product) o;

        if (isActive() != product.isActive()) return false;
        if (getUnitsInStock() != product.getUnitsInStock()) return false;
        if (!getId().equals(product.getId())) return false;
        if (getSku() != null ? !getSku().equals(product.getSku()) : product.getSku() != null) return false;
        if (getName() != null ? !getName().equals(product.getName()) : product.getName() != null) return false;
        if (getDescription() != null ? !getDescription().equals(product.getDescription()) : product.getDescription() != null)
            return false;
        if (getUnitPrice() != null ? !getUnitPrice().equals(product.getUnitPrice()) : product.getUnitPrice() != null)
            return false;
        if (getImageUrl() != null ? !getImageUrl().equals(product.getImageUrl()) : product.getImageUrl() != null)
            return false;
        if (getDateCreated() != null ? !getDateCreated().equals(product.getDateCreated()) : product.getDateCreated() != null)
            return false;
        return getLastUpdated() != null ? getLastUpdated().equals(product.getLastUpdated()) : product.getLastUpdated() == null;
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

        Product that = (Product) o;

        boolean result = true;
        for (ProductFields field : ProductFields.values()) {
            if (excludes.contains(field)) {
                continue;
            }
            switch (field) {
                case ID:
                    result = getId().equals(that.getId());
                    break;
                case SKU:
                case NAME:
                case DESCRIPTION:
                case IMAGE_URL:
                case DATE_CREATED:
                case LAST_UPDATED:
                case CATEGORY:
                    // nullable fields
                    result = Objects.equals(get(field), that.get(field));
                    break;
                case UNIT_PRICE:
                    result = getUnitPrice().equals(that.getUnitPrice());
                    break;
                case ACTIVE:
                    result = isActive() == that.isActive();
                    break;
                case UNITS_IN_STOCK:
                    result = getUnitsInStock() == that.getUnitsInStock();
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
        if (ProductFields.ID.equals(field)) {
            result = getId();
        } else if (ProductFields.SKU.equals(field)) {
            result = getSku();
        } else if (ProductFields.NAME.equals(field)) {
            result = getName();
        } else if (ProductFields.DESCRIPTION.equals(field)) {
            result = getDescription();
        } else if (ProductFields.UNIT_PRICE.equals(field)) {
            result = getUnitPrice();
        } else if (ProductFields.IMAGE_URL.equals(field)) {
            result = getImageUrl();
        } else if (ProductFields.ACTIVE.equals(field)) {
            result = isActive();
        } else if (ProductFields.UNITS_IN_STOCK.equals(field)) {
            result = getUnitsInStock();
        } else if (ProductFields.DATE_CREATED.equals(field)) {
            result = getDateCreated();
        } else if (ProductFields.LAST_UPDATED.equals(field)) {
            result = getLastUpdated();
        } else if (ProductFields.CATEGORY.equals(field)) {
            result = getCategory();
        } else {
            throw new UnsupportedOperationException("Field " + field + " not supported");
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + (getSku() != null ? getSku().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getUnitPrice() != null ? getUnitPrice().hashCode() : 0);
        result = 31 * result + (getImageUrl() != null ? getImageUrl().hashCode() : 0);
        result = 31 * result + (isActive() ? 1 : 0);
        result = 31 * result + getUnitsInStock();
        result = 31 * result + (getDateCreated() != null ? getDateCreated().hashCode() : 0);
        result = 31 * result + (getLastUpdated() != null ? getLastUpdated().hashCode() : 0);
        return result;
    }
}
