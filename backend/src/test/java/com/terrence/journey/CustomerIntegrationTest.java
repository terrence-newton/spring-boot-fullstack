package com.terrence.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.terrence.customer.*;
import com.terrence.customer.auth.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customers";
    private static final String LOGIN_URI = "/api/v1/auth/login";
    private static final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();

    @Test
    void canRegisterACustomer() {

        // create reg request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@amigoscode.com";
        int age = RANDOM.nextInt(1,100);
        String gender = Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email, "password", age, gender.toString()
        );

        // send a post request
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all customers from
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        int id = allCustomers.stream()
                .filter(c -> c.email().equals(email))
                .findFirst()
                .orElseThrow()
                .id();

        // make sure that customer is present
        CustomerDTO expectedCustomerDTO = new CustomerDTO(
                id, name, email, gender, age,List.of("ROLE_USER"), email
        );

        assertThat(allCustomers)
                .contains(expectedCustomerDTO);

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .isEqualTo(expectedCustomerDTO);

    }

    @Test
    void canDeleteCustomer() {
        // create reg request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@amigoscode.com.com";
        int age = RANDOM.nextInt(1,100);
        String gender = Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email, "password", age, gender.toString()
        );
        // send a post request
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all customers from
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();


        // get customer by id
        int id = allCustomers.stream()
                .filter(c -> c.email().equals(email))
                .findFirst()
                .orElseThrow()
                .id();

        String viewerToken = getViewerToken();

        // delete customer
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", viewerToken))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {

        // create reg request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@amigoscode.com";
        int age = RANDOM.nextInt(1,100);
        String gender = Gender.MALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email, "password", age, gender.toString()
        );

        // send a post request
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all customers from
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that customer is present


        // update customer

        int id = allCustomers.stream()
                .filter(c -> c.email().equals(email))
                .findFirst()
                .orElseThrow()
                .id();

        String updateName = fakerName.fullName();
        String updateEmail = fakerName.lastName() + "-" + UUID.randomUUID() + "@amigoscode.com";
        int updateAge = RANDOM.nextInt(1,100);
        String updateGender = gender;
        CustomerUpdateRequest update = new CustomerUpdateRequest(
                Optional.of(updateName),
                Optional.of(updateEmail),
                Optional.of(updateAge),
                Optional.of(updateGender)
        );

        CustomerDTO expectedCustomerDTO = new CustomerDTO(
                id,
                updateName,
                updateEmail,
                updateGender,
                updateAge,
                List.of("ROLE_USER"),
                updateEmail
        );

        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .body(Mono.just(update), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id

        String viewerToken = getViewerToken();

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", viewerToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .isEqualTo(expectedCustomerDTO);

    }

        private String getViewerToken() {

        // create reg request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@amigoscode.com.com";
        int age = RANDOM.nextInt(1,100);
        String gender = Gender.MALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email, "password", age, gender.toString()
        );

        // send a post request
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        return jwtToken;
    }
}
