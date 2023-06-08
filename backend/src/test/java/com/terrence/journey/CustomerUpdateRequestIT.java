package com.terrence.journey;

import com.terrence.customer.Gender;

import java.util.Optional;

public record CustomerUpdateRequestIT(
        Optional<String> name,
        Optional<String> email,
        Optional<Integer> age,
        Optional<String> gender
) {
}
