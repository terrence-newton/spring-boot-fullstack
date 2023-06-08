package com.terrence;

import com.github.javafaker.Faker;
import com.terrence.customer.Customer;
import com.terrence.customer.CustomerRepository;
import com.terrence.customer.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main (String[] args) {
        SpringApplication.run(Main.class,args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
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
                    random.nextInt(16,99),
                    gender
            );

            //List<Customer> customers = List.of(alex, jamila);
            //customerRepository.saveAll(customers);
            customerRepository.save(customer);
        };
    }

}
