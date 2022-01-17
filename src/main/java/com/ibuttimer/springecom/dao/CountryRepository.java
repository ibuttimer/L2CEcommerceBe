package com.ibuttimer.springecom.dao;

import com.ibuttimer.springecom.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestResource(exported = true)
public interface CountryRepository extends JpaRepository<Country, Long> {

    Country findByCode(@RequestParam("code") String code);

    Page<Country> findByNameContaining(@RequestParam("name") String name, Pageable pageable);

}
