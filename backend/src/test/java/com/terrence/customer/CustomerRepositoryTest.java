package com.terrence.customer;

import com.terrence.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println(applicationContext.getBeanDefinitionCount());
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
                ,Gender.MALE
        );

        underTest.save(customer);

        //When
        boolean actual = underTest.existsCustomerByEmail(customer.getEmail());

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void NotExistsCustomerByEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        boolean actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20,
                Gender.MALE
        );

        underTest.save(customer);

        int id = underTest.findAll().stream()
                        .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst()
                .orElseThrow()
                .getId();

        //When
        boolean actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void notExistsCustomerById() {
        //Given
        int id = -1;

        //When
        boolean actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isFalse();
    }
}