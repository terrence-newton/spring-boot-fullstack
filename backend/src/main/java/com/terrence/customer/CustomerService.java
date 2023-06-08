package com.terrence.customer;

import com.terrence.exception.DuplicateResourceException;
import com.terrence.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers () {
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomer (Integer customerId) {
        Customer customer = customerDAO.selectCustomerById(customerId)
                .orElseThrow(() -> customerWithIdNotFound(customerId));
        return customer;
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (!customerDAO.existsCustomerWithEmail(customerRegistrationRequest.email())) {
            customerDAO.insertCustomer(
                    new Customer(
                            customerRegistrationRequest.name(),
                            customerRegistrationRequest.email(),
                            customerRegistrationRequest.age(),
                            customerRegistrationRequest.gender()
                    )
            );
        } else {
            throw customerWithEmailAlreadyExists();
        }
    }

    public void removeCustomer(Integer customerId) {
        if (customerDAO.existsCustomerWithId(customerId)) {
            customerDAO.deleteCustomer(customerId);
        } else {
            throw customerWithIdNotFound(customerId);
        }
    }

    public void updateCustomer(Integer customerId, CustomerUpdateRequest customerUpdateRequest) {
        /*
        - check if id exists
        - check if any real updates
        - check if new email is not already taken
         */

        Customer originalCustomer = customerDAO.selectCustomerById(customerId)
                .orElseThrow(() -> customerWithIdNotFound(customerId));

        Customer updatedCustomer = new Customer(
          customerId
          ,customerUpdateRequest.name().orElse(originalCustomer.getName())
          ,customerUpdateRequest.email().orElse(originalCustomer.getEmail())
          ,customerUpdateRequest.age().orElse(originalCustomer.getAge())
        );

        if (updatedCustomer.equals(originalCustomer)) {
            throw new DuplicateResourceException("no changes made.");
        }

        if (!updatedCustomer.getEmail().equals(originalCustomer.getEmail())
                && customerDAO.existsCustomerWithEmail(updatedCustomer.getEmail())
        ) {
            throw customerWithEmailAlreadyExists();
        }

        customerDAO.updateCustomer(updatedCustomer);
        //customerDAO.selectCustomerById(customerId).get();

    }

    private RuntimeException customerWithIdNotFound(Integer customerId) {
        return new ResourceNotFoundException("customer with id [%s] not found".formatted(customerId));
    }

    private RuntimeException customerWithEmailAlreadyExists() {
        return new DuplicateResourceException("email already exists.");
    }
}
