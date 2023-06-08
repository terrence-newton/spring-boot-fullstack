package com.terrence.customer;

import java.util.Optional;

public record CustomerUpdateRequest(
        Optional<String> name,
        Optional<String> email,
        Optional<Integer> age,
        Optional<String> gender
) {
}
