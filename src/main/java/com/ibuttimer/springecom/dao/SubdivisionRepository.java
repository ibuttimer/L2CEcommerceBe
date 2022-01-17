package com.ibuttimer.springecom.dao;

import com.ibuttimer.springecom.entity.Subdivision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@RepositoryRestResource(exported = true)
public interface SubdivisionRepository extends JpaRepository<Subdivision, Long> {

    Page<Subdivision> findBySubdiv(@RequestParam("subdiv") String subdiv, Pageable pageable);

    Page<Subdivision> findByNameContaining(@RequestParam("name") String name, Pageable pageable);

    Collection<Subdivision> findByCountryId(Long country_id);

    @Query("SELECT d from Subdivision d join Country c on c.id = d.country_id WHERE c.code = ?1 ORDER BY d.name ASC")
    Collection<Subdivision> findByCountryCode(String code);


}

