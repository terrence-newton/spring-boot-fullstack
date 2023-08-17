package com.terrence.customer;

import com.terrence.exception.DuplicateResourceException;
import com.terrence.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDTOMapper mapper;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO, PasswordEncoder passwordEncoder, CustomerDTOMapper mapper) {
        this.customerDAO = customerDAO;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    public List<CustomerDTO> getAllCustomers () {

        return customerDAO.selectAllCustomers()
                .stream()
                .map(mapper).toList();
    }

    public CustomerDTO getCustomer (Integer customerId) {
        CustomerDTO customer = customerDAO.selectCustomerById(customerId)
                .map(mapper)
                .orElseThrow(() -> customerWithIdNotFound(customerId));
        return customer;
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (!customerDAO.existsCustomerWithEmail(customerRegistrationRequest.email())) {
            customerDAO.insertCustomer(
                    new Customer(
                            customerRegistrationRequest.name(),
                            customerRegistrationRequest.email(),
                            passwordEncoder.encode(customerRegistrationRequest.password()),
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
                , "password"
                , customerUpdateRequest.age().orElse(originalCustomer.getAge())
                ,customerUpdateRequest.gender().orElse(originalCustomer.getGender())
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

    }

    private RuntimeException customerWithIdNotFound(Integer customerId) {
        return new ResourceNotFoundException("customer with id [%s] not found".formatted(customerId));
    }

    private RuntimeException customerWithEmailAlreadyExists() {
        return new DuplicateResourceException("email already exists.");
    }
}
