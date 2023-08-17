package com.terrence.customer;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer customerId);
    void insertCustomer(Customer customer);
    boolean existsCustomerWithEmail(String email);
    void deleteCustomer(Integer customerId);
    boolean existsCustomerWithId(Integer customerId);
    public void updateCustomer(Customer customer);
    Optional<Customer> selectUserByEmail(String email);
}
