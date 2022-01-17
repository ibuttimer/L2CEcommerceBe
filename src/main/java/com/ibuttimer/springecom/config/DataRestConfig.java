package com.ibuttimer.springecom.config;

import com.ibuttimer.springecom.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.List;
import java.util.Set;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

    private final EntityManager entityManager;

    @Value("${allowed.origins}")
    String[] allowedOrigins;

    @Autowired
    public DataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        HttpMethod[] unsupported = { HttpMethod.PUT, HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE };

        // Disable HTTP methods
        for (Class<?> cls : List.of(
                Product.class, ProductCategory.class, Country.class, Subdivision.class, Order.class
        )) {
            config.getExposureConfiguration()
                    .forDomainType(cls)
                    .withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupported))
                    .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupported));
        }

        // expose ids
        exposeIds(config);

        // configure CORS mapping for REST repositories
        // Note: CORS for controllers needs to be set in WebConfig.addCorsMappings()
        WebConfig.addCorsMappings(cors, config.getBasePath().getPath(), allowedOrigins);
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        // list of entity classes
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        // entity types for the entities
        Class<?>[] domainTypes = entities.stream()
                .map(EntityType::getJavaType)
                .toArray(Class<?>[]::new);
        // expose entity ids for entity/domain types
        config.exposeIdsFor(domainTypes);
    }
}
