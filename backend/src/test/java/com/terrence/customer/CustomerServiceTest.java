package com.terrence.customer;

import com.terrence.exception.DuplicateResourceException;
import com.terrence.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;
    private CustomerService underTest;
    private final CustomerDTOMapper mapper = new CustomerDTOMapper();
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO, passwordEncoder, mapper);
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();

        //Then
        verify(customerDAO).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", "password", 19, Gender.MALE
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //When
        CustomerDTO actual = underTest.getCustomer(id);

        //Then
        assertThat(actual).isEqualTo(mapper.apply(customer));
    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        int id = 10;

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void CanAddCustomer() {
        //Given
        CustomerRegistrationRequest customerRegistrationRequest =
                new CustomerRegistrationRequest(
                "Alex", "alex@gmail.com", "password", 19, Gender.MALE
        );

        String passwordHash = "@$%#$gnkgf9042";

        when(passwordEncoder.encode(customerRegistrationRequest.password())).thenReturn(passwordHash);

        when(customerDAO.existsCustomerWithEmail(customerRegistrationRequest.email()))
                .thenReturn(false);

        //When
        underTest.addCustomer(customerRegistrationRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(customerRegistrationRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerRegistrationRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerRegistrationRequest.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(customerRegistrationRequest.gender());
        assertThat(capturedCustomer.getPassword()).isEqualTo(passwordHash);

    }

    @Test
    void willThrowWhenAddCustomerEmailAlreadyExists() {
        //Given
        CustomerRegistrationRequest customerRegistrationRequest =
                new CustomerRegistrationRequest(
                        "Alex", "alex@gmail.com", "password", 19, Gender.MALE
                );

        when(customerDAO.existsCustomerWithEmail(customerRegistrationRequest.email()))
                .thenReturn(true);

        //When
        //Then
        assertThatThrownBy(() -> underTest.addCustomer(customerRegistrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already exists.");
        verify(customerDAO, never()).insertCustomer(any());
    }

    @Test
    void canRemoveCustomer() {
        //Given
        int id = 10;
        when(customerDAO.existsCustomerWithId(id)).thenReturn(true);

        //When
        underTest.removeCustomer(id);

        //Then
        verify(customerDAO).deleteCustomer(id);
    }

    @Test
    void willThrowWhenRemoveCustomerIdDoesNotExist() {
        //Given
        int id = 10;
        when(customerDAO.existsCustomerWithId(id)).thenReturn(false);

        //When
        //Then
        assertThatThrownBy(() -> underTest.removeCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
        verify(customerDAO, never()).deleteCustomer(any());
    }

    @Test
    void willThrowWhenUpdateCustomerNotFound() {
        //Given
        int id = 10;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                Optional.of("Alex"),
                Optional.of("alex@gmail.com"),
                Optional.of(19),
                Optional.of(Gender.FEMALE)
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void canUpdateCustomerName() {
        //Given

        int id = 10;

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                Optional.of("Alex"),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        Customer originalCustomer = new Customer(
                10,
                "George",
                "alex@gmail.com",
                "password", 21,
                Gender.MALE
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(originalCustomer));

        //When
        underTest.updateCustomer(id, customerUpdateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(customerUpdateRequest.name().get());
        assertThat(capturedCustomer.getEmail()).isEqualTo(originalCustomer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(originalCustomer.getAge());
        assertThat(capturedCustomer.getGender()).isEqualTo(originalCustomer.getGender());
    }

    @Test
    void canUpdateCustomerEmail() {
        //Given

        int id = 10;

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                Optional.empty(),
                Optional.of("alex@hotmail.com"),
                Optional.empty(),
                Optional.empty()
        );

        Customer originalCustomer = new Customer(
                10,
                "George",
                "alex@gmail.com",
                "password", 21,
                Gender.FEMALE
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(originalCustomer));
        when(customerDAO.existsCustomerWithEmail(customerUpdateRequest.email().get()))
                .thenReturn(false);

        //When
        underTest.updateCustomer(id, customerUpdateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(originalCustomer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerUpdateRequest.email().get());
        assertThat(capturedCustomer.getAge()).isEqualTo(originalCustomer.getAge());
        assertThat(capturedCustomer.getGender()).isEqualTo(originalCustomer.getGender());
    }

    @Test
    void canUpdateCustomerAge() {
        //Given

        int id = 10;

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.of(23),
                Optional.empty()
        );

        Customer originalCustomer = new Customer(
                10,
                "George",
                "alex@gmail.com",
                "password", 21,
                Gender.FEMALE
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(originalCustomer));

        //When
        underTest.updateCustomer(id, customerUpdateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(originalCustomer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(originalCustomer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerUpdateRequest.age().get());
        assertThat(capturedCustomer.getGender()).isEqualTo(originalCustomer.getGender());
    }

    @Test
    void canUpdateCustomerGender() {
        //Given

        int id = 10;

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(Gender.MALE)
        );

        Customer originalCustomer = new Customer(
                10,
                "George",
                "alex@gmail.com",
                "password", 21,
                Gender.FEMALE
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(originalCustomer));

        //When
        underTest.updateCustomer(id, customerUpdateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(originalCustomer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(originalCustomer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(originalCustomer.getAge());
        assertThat(capturedCustomer.getGender()).isEqualTo(customerUpdateRequest.gender().get());
    }

    @Test
    void willThrowWhenUpdateCustomerNoChanges() {
        //Given
        int id = 10;

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                Optional.of("George"),
                Optional.of("alex@gmail.com"),
                Optional.of(21),
                Optional.of(Gender.FEMALE)
        );

        Customer originalCustomer = new Customer(
                10,
                "George",
                "alex@gmail.com",
                "password", 21,
                Gender.FEMALE
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(originalCustomer));

        //When
        //Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("no changes made.");
        verify(customerDAO, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenUpdateCustomerEmpty() {
        //Given
        int id = 10;

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        Customer originalCustomer = new Customer(
                10,
                "George",
                "alex@gmail.com",
                "password", 21,
                Gender.FEMALE
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(originalCustomer));

        //When
        //Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("no changes made.");
        verify(customerDAO, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenUpdateCustomerEmailAlreadyExists() {
        //Given
        int id = 10;

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                Optional.empty(),
                Optional.of("steve@gmail.com"),
                Optional.empty(),
                Optional.empty()
        );

        Customer originalCustomer = new Customer(
                10,
                "George",
                "alex@gmail.com",
                "password", 21,
                Gender.MALE
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(originalCustomer));
        when(customerDAO.existsCustomerWithEmail(customerUpdateRequest.email().get()))
                .thenReturn(true);

        //When
        //Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already exists.");
        verify(customerDAO, never()).updateCustomer(any());
    }
}