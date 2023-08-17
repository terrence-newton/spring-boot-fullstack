package com.terrence.customer.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
