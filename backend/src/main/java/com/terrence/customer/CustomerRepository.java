package com.terrence.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    public boolean existsCustomerByEmail(String email);
    public boolean existsCustomerById(Integer customerId);
}
