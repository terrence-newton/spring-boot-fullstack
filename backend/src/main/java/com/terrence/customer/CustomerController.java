package com.terrence.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers () {
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomer (@PathVariable("customerId") Integer customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public void AddCustomer (@RequestBody CustomerRegistrationRequest newCustomer) {
        customerService.addCustomer(newCustomer);
    }
    @DeleteMapping("{customerId}")
    public void DeleteCustomer (@PathVariable("customerId") Integer customerId) {
        customerService.removeCustomer(customerId);
    }

    @PutMapping ("{customerId}")
    public void UpdateCustomer (@PathVariable("customerId") Integer customerId
                ,@RequestBody CustomerUpdateRequest customerUpdateRequest) {
         customerService.updateCustomer(customerId, customerUpdateRequest);
    }
}
