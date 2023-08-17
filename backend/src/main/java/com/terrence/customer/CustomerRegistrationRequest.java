package com.terrence.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        String password,
        Integer age,
        String gender
) {
}
