package com.ebankingproject.e_banking_backend.repositories;

import com.ebankingproject.e_banking_backend.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Page<Customer> findByNameContains(String kw,Pageable pageable);
//    Customer findByUserId(Long userId);
}
