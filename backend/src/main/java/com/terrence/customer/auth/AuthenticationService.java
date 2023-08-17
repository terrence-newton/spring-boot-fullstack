package com.terrence.customer.auth;

import com.terrence.customer.Customer;
import com.terrence.customer.CustomerDTO;
import com.terrence.customer.CustomerDTOMapper;
import com.terrence.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMapper mapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager, CustomerDTOMapper mapper, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        Customer principal = (Customer) authentication.getPrincipal();
        CustomerDTO customerDTO = mapper.apply(principal);
        String token = jwtUtil.issueToken(customerDTO.username(), customerDTO.roles());
        return new AuthenticationResponse(token, customerDTO);
    }
}
