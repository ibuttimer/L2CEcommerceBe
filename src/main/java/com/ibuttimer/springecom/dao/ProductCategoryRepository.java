package com.ibuttimer.springecom.dao;

import com.ibuttimer.springecom.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import static com.ibuttimer.springecom.config.Config.PRODUCT_CATEGORIES_PATH;

@RepositoryRestResource(collectionResourceRel = "productCategory", path = PRODUCT_CATEGORIES_PATH, exported = true)
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
