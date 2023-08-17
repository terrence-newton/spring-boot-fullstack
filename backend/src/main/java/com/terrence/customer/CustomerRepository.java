package com.terrence.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    public boolean existsCustomerByEmail(String email);
    public boolean existsCustomerById(Integer customerId);
    public Optional<Customer> findCustomerByEmail(String email);
}
