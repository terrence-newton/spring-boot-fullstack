package com.terrence.customer.auth;

import com.terrence.customer.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO
) {
}
