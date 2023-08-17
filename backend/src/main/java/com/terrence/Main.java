package com.terrence;

import com.github.javafaker.Faker;
import com.terrence.customer.Customer;
import com.terrence.customer.CustomerRepository;
import com.terrence.customer.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class Main {

    public static void main (String[] args) {
        SpringApplication.run(Main.class,args);
    }

    @Bean
    CommandLineRunner runner(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Faker faker = new Faker();
            Random random = new Random();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String gender;
            if(Math.random() < .5) {
                gender = Gender.MALE;
            } else {
                gender = Gender.FEMALE;
            }
            Customer customer = new Customer(
                    firstName + " " + lastName,
                    firstName.toLowerCase() + "." + lastName.toLowerCase() + "@amigoscode.com",
                    passwordEncoder.encode(UUID.randomUUID().toString()),
                    random.nextInt(16,99),
                    gender
            );

            customerRepository.save(customer);
        };
    }

}
