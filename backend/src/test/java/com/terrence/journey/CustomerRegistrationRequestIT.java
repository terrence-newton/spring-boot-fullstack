package com.terrence.journey;

import com.terrence.customer.Gender;

public record CustomerRegistrationRequestIT(
        String name,
        String email,
        Integer age,
        String gender
) {
}
