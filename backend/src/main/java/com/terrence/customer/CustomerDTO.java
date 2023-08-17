package com.terrence.customer;

import java.util.List;

public record CustomerDTO(
    Integer id,
    String name,
    String email,
    String gender,
    Integer age,
    List<String> roles,
    String username

){}