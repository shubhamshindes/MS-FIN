package com.fin.customer_service.repository;

import com.fin.customer_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByNationalIdentityNumber(String nationalIdentityNumber);
    boolean existsByEmail(String email);
    boolean existsByNationalIdentityNumber(String nationalIdentityNumber);
}