package com.terrence.customer;

import com.terrence.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final JWTUtil jwtUtil;

    public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<CustomerDTO> getCustomers () {
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public CustomerDTO getCustomer (@PathVariable("customerId") Integer customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody CustomerRegistrationRequest newCustomer) {
        customerService.addCustomer(newCustomer);
        String jwtToken = jwtUtil.issueToken(newCustomer.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer customerId) {
        customerService.removeCustomer(customerId);
    }

    @PutMapping ("{customerId}")
    public void updateCustomer(@PathVariable("customerId") Integer customerId
                , @RequestBody CustomerUpdateRequest customerUpdateRequest) {
         customerService.updateCustomer(customerId, customerUpdateRequest);
    }
}
