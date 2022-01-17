package com.ibuttimer.springecom.dao;

import com.ibuttimer.springecom.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestResource(exported = true)
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerEmail(@RequestParam("email") String email, Pageable pageable);

    Page<Order> findByCustomerEmailOrderByDateCreatedDesc(@RequestParam("email") String email, Pageable pageable);
}
