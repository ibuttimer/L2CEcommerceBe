package com.ibuttimer.springecom.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibuttimer.springecom.AbstractTest;
import com.ibuttimer.springecom.config.Config;
import com.ibuttimer.springecom.dao.ProductCategoryRepository;
import com.ibuttimer.springecom.dao.ProductRepository;
import com.ibuttimer.springecom.entity.IEntity;
import com.ibuttimer.springecom.entity.Product;
import com.ibuttimer.springecom.entity.ProductCategory;
import org.assertj.core.util.Streams;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.ibuttimer.springecom.api.ProductCategoryApiTest.setupCategories;
import static com.ibuttimer.springecom.config.Config.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductApiTest extends AbstractTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    private List<ProductCategory> repositoryCategories;
//    private static final int NUM_CATEGORIES = 2;
//    public static List<ProductCategory> CATEGORIES = LongStream.range(1, NUM_CATEGORIES + 1)    // db ids start at zero
//            .mapToObj(i -> ProductCategory.of(i, "category"+i))
//            .collect(Collectors.toList());

    private static final LocalDate timestamp = LocalDate.now();

    public List<Product> repositoryProducts;
    private static final int NUM_PRODUCTS = 3;
    public static List<Product> PRODUCTS = LongStream.range(1, NUM_PRODUCTS + 1)    // db ids start at zero
            .mapToObj(i -> Product.of(i, "sku"+i, "product"+i, "desc"+i, BigDecimal.valueOf(i),
                    "img"+i, true, (int) (10*i), timestamp, timestamp,
                    null))
            .collect(Collectors.toList());

    @BeforeAll
    public static void beforeAll() {
        AbstractTest.beforeAll();
    }

    @BeforeEach
    public void beforeEach() {
        for (JpaRepository<?, Long> repo : List.of(productRepository, productCategoryRepository)) {
            clearRepository(repo);
        }

        repositoryCategories = setupCategories(productCategoryRepository);

        // set product categories from repository
        for (int i = 0; i < PRODUCTS.size(); i++) {
            PRODUCTS.get(i).setCategory(
                    repositoryCategories.get(i % repositoryCategories.size()));
        }

        repositoryProducts = Streams.stream(
                        productRepository.saveAll(PRODUCTS))
                .collect(Collectors.toList());
        assertEquals(PRODUCTS.size(), repositoryProducts.size());

        for (int i = 0; i < PRODUCTS.size(); i++) {
            Product base = PRODUCTS.get(i);
            Product repo = repositoryProducts.get(i);
            // check equal excluding id and dates
            assertTrue(base.equals(repo,
                    List.of(Product.ProductFields.ID, Product.ProductFields.DATE_CREATED,
                            Product.ProductFields.LAST_UPDATED)));
        }
    }

    @DisplayName("Verify products api is read-only")
    @Test
    public void verifyProductsApiReadOnly() {
        Product newEntity = Product.of("new sku", "new name", "new description", BigDecimal.ONE,
                "new imageUrl", true, 1, LocalDate.now(), LocalDate.now(),
                repositoryCategories.get(0));
        Product updateEntity = Product.of(repositoryProducts.get(0));
        updateEntity.setName(updateEntity.getName() + " updated!");

        verifyApiReadOnly(this, getProductsUrl(), getProductIdUrl(updateEntity.getId()), newEntity, updateEntity);
    }

    public static void verifyApiReadOnly(AbstractTest test, String url, String idUrl, IEntity newEntity, IEntity updateEntity) {
        MockMvc mockMvc = test.getMockMvc();
        try {
            mockMvc.perform(post(url).content(
                            objectMapper.writeValueAsString(newEntity)))
                    .andExpect(status().isMethodNotAllowed());

            mockMvc.perform(put(idUrl).content(
                            objectMapper.writeValueAsString(updateEntity)))
                    .andExpect(status().isMethodNotAllowed());

            mockMvc.perform(patch(idUrl).content(
                        objectMapper.writeValueAsString(updateEntity)))
                .andExpect(status().isMethodNotAllowed());

            mockMvc.perform(delete(idUrl))
                    .andExpect(status().isMethodNotAllowed());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @DisplayName("Get products list")
    @Test
    public void getProductsList() {
        try {
            mockMvc.perform(get(
                            getProductsUrl()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(HAL_JSON))
                    .andExpect(mvcResult -> {
                        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

                        repositoryProducts.forEach(product -> {
                            verifyProduct(body, product);
                        });

                    });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void verifyProduct(String body, Product product) {
        assertTrue(body.contains(
            String.format("\"sku\" : \"%s\"", product.getSku())
        ), String.format("'sku' from %s not found", product));
        assertTrue(body.contains(
            String.format("\"name\" : \"%s\"", product.getName())
        ), String.format("'name' from %s not found", product));
        assertTrue(body.contains(
            String.format("\"description\" : \"%s\"", product.getDescription())
        ), String.format("'description' from %s not found", product));
        assertTrue(body.contains(
            String.format("\"unitPrice\" : %s", product.getUnitPrice().toString())
        ), String.format("'unitPrice' from %s not found", product));
        assertTrue(body.contains(
            String.format("\"imageUrl\" : \"%s\"", product.getImageUrl())
        ), String.format("'imageUrl' from %s not found", product));
        assertTrue(body.contains(
            String.format("\"active\" : %s", product.isActive())
        ), String.format("'active' from %s not found", product));
        assertTrue(body.contains(
            String.format("\"unitsInStock\" : %d", product.getUnitsInStock())
        ), String.format("'unitsInStock' from %s not found", product));
    }

    @DisplayName("Get product")
    @Test
    public void getProduct() {
        Product product = repositoryProducts.get(0);
        try {
            mockMvc.perform(get(
                            getProductIdUrl(product.getId())))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(HAL_JSON))
                    .andExpect(mvcResult -> {
                        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

                        verifyProduct(body, product);
                    });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    public static String getProductsUrl() {
        return Config.getInstance().getBasedUrl(PRODUCTS_URL, Map.of());
    }

    public static String getProductIdUrl(long id) {
        return Config.getInstance().getBasedUrl(PRODUCT_ID_URL, Map.of(ID_MARKER, id));
    }
}
