package com.ibuttimer.springecom.api;

import com.ibuttimer.springecom.AbstractTest;
import com.ibuttimer.springecom.config.Config;
import com.ibuttimer.springecom.dao.ProductCategoryRepository;
import com.ibuttimer.springecom.entity.Product;
import com.ibuttimer.springecom.entity.ProductCategory;
import org.assertj.core.util.Streams;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.ibuttimer.springecom.api.ProductApiTest.verifyApiReadOnly;
import static com.ibuttimer.springecom.config.Config.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductCategoryApiTest extends AbstractTest {

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    private List<ProductCategory> repositoryCategories;
    private static final int NUM_CATEGORIES = 2;
    private static final List<ProductCategory> CATEGORIES = LongStream.range(1, NUM_CATEGORIES + 1)    // db ids start at zero
            .mapToObj(i -> ProductCategory.of(i, "category"+i))
            .collect(Collectors.toList());

    @BeforeAll
    public static void beforeAll() {
        AbstractTest.beforeAll();
    }

    @BeforeEach
    public void beforeEach() {
        repositoryCategories = setupCategories(productCategoryRepository);
    }

    public static List<ProductCategory> setupCategories(ProductCategoryRepository repository) {
        for (JpaRepository<?, Long> repo : List.of(repository)) {
            clearRepository(repo);
        }

        List<ProductCategory> categories = Streams.stream(
                        repository.saveAll(CATEGORIES))
                .collect(Collectors.toList());
        assertEquals(CATEGORIES.size(), categories.size());

        for (int i = 0; i < CATEGORIES.size(); i++) {
            ProductCategory base = CATEGORIES.get(i);
            ProductCategory repo = categories.get(i);
            // check equal excluding id
            assertTrue(base.equals(repo, List.of(ProductCategory.ProductCategoryFields.ID)));
        }

        return categories;
    }

    @DisplayName("Verify product categories api is read-only")
    @Test
    public void verifyProductCategoriesApiReadOnly() {
        ProductCategory newEntity = ProductCategory.of("new category");
        ProductCategory updateEntity = ProductCategory.of(repositoryCategories.get(0));
        updateEntity.setCategoryName(updateEntity.getCategoryName() + " updated!");

        verifyApiReadOnly(this, getProductCategoriesUrl(), getProductCategoryIdUrl(updateEntity.getId()),
                newEntity, updateEntity);
    }

    @DisplayName("Get product categories list")
    @Test
    public void getProductsList() {
        try {
            mockMvc.perform(get(
                            getProductCategoriesUrl()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(HAL_JSON))
                    .andExpect(mvcResult -> {
                        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

                        repositoryCategories.forEach(category -> {
                            verifyProductCategory(body, category);
                        });

                    });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void verifyProductCategory(String body, ProductCategory category) {
        assertTrue(body.contains(
            String.format("\"categoryName\" : \"%s\"", category.getCategoryName())
        ), String.format("'categoryName' from %s not found", category));
    }

    @DisplayName("Get product category")
    @Test
    public void getProduct() {
        ProductCategory category = repositoryCategories.get(0);
        try {
            mockMvc.perform(get(
                            getProductCategoryIdUrl(category.getId())))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(HAL_JSON))
                    .andExpect(mvcResult -> {
                        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

                        verifyProductCategory(body, category);
                    });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    public static String getProductCategoriesUrl() {
        return Config.getInstance().getBasedUrl(PRODUCT_CATEGORIES_URL, Map.of());
    }

    public static String getProductCategoryIdUrl(long id) {
        return Config.getInstance().getBasedUrl(PRODUCT_CATEGORIES_ID_URL, Map.of(ID_MARKER, id));
    }
}
