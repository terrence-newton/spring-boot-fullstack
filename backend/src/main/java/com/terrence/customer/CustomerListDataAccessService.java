package com.terrence.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO{

    private List<Customer> customers = new ArrayList<>();

    private Integer lastId = 0;

    private Integer getNewId() {
        lastId += 1;
        return lastId;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return customers.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        Customer newCustomer = customer;
        newCustomer.setId(getNewId());
        customers.add(customer);
        lastId += 1;
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        customers.remove(this.selectCustomerById(customerId).get());
    }

    @Override
    public boolean existsCustomerWithId(Integer customerId) {
        return customers.stream()
                .anyMatch(customer -> customer.getId().equals(customerId));
    }

    @Override
    public void updateCustomer(Customer customer) {

        Customer updateCustomer = this.selectCustomerById(customer.getId()).get();

        if(updateCustomer != null) {

            if(customer.getName() != null) {
                updateCustomer.setName(customer.getName());
            }

            if(customer.getEmail() != null) {
                updateCustomer.setEmail(customer.getEmail());
            }

            if(customer.getAge() != null) {
                updateCustomer.setAge(customer.getAge());
            }

        }

    }
}
