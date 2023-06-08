package com.terrence.customer;

import java.util.Arrays;

public enum Gender {
    MALE ("Male"),
    FEMALE ("Female");

    private final String display;

    Gender(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return "Gender{" +
                "display='" + display + '\'' +
                '}';
    }

    public static Gender fromText(String text){
        return Arrays.stream(Gender.values())
                .filter(g -> g.toString().equals(text))
                .findFirst().orElseThrow(() -> new IllegalArgumentException());
    }
}
