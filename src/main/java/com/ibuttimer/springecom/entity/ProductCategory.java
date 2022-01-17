package com.ibuttimer.springecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="product_category")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProductCategory implements IEntity {

    public enum ProductCategoryFields {
        ID, CATEGORY_NAME
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    @ToString.Exclude
    @JsonIgnore
    private Collection<Product> products;

    public static ProductCategory of(Long id, String categoryName) {
        ProductCategory category = new ProductCategory();

        category.id = id;
        category.categoryName = categoryName;

        return category;
    }

    public static ProductCategory of(String categoryName) {
        return of(null, categoryName);
    }

    public static ProductCategory of(ProductCategory category) {
        return of(category.id, category.categoryName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        return equals(o, List.of());
    }

    @Override
    public boolean equals(Object o, List<Enum<?>> excludes) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        ProductCategory that = (ProductCategory) o;

        boolean result = true;
        for (ProductCategoryFields field : ProductCategoryFields.values()) {
            if (excludes.contains(field)) {
                continue;
            }
            switch (field) {
                case ID:
                    result = getId().equals(that.getId());
                    break;
                case CATEGORY_NAME:
                    result = getCategoryName() != null ? getCategoryName().equals(that.getCategoryName()) : that.getCategoryName() == null;
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
        if (ProductCategoryFields.ID.equals(field)) {
            result = getId();
        } else if (ProductCategoryFields.CATEGORY_NAME.equals(field)) {
            result = getCategoryName();
        } else {
            throw new UnsupportedOperationException("Field " + field + " not supported");
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + (getCategoryName() != null ? getCategoryName().hashCode() : 0);
        return result;
    }
}
