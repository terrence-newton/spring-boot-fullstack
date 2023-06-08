package com.terrence.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age,
        String gender
) {
}
