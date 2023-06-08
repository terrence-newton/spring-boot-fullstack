package com.terrence.customer;

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
}
