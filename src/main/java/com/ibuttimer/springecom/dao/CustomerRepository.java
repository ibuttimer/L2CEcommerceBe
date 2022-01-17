package com.ibuttimer.springecom.dao;

import com.ibuttimer.springecom.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestResource(exported = false)
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation

    Customer findByEmail(@RequestParam("email") String email);

    Customer findByFirstNameAndLastNameAndEmail(@RequestParam("firstName") String firstName,
                                                @RequestParam("lastName") String lastName,
                                                @RequestParam("email") String email);
}