package com.terrence.security;

import com.terrence.customer.CustomerDTO;
import com.terrence.customer.CustomerService;
import com.terrence.customer.auth.AuthenticationRequest;
import com.terrence.customer.auth.AuthenticationResponse;
import com.terrence.customer.auth.AuthenticationService;
import com.terrence.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(CustomerService customerService, AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login (@RequestBody AuthenticationRequest authenticationRequest) {

        AuthenticationResponse response = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .build()
                //.body(response)
                ;

    }
}
