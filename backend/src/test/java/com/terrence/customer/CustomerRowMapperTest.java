package com.terrence.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        //Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);

        Customer expected = new Customer(
          1,"Alex","alex@gmail.com", "password", 21, Gender.FEMALE
        );

        when(resultSet.getInt("id")).thenReturn(expected.getId());
        when(resultSet.getString("name")).thenReturn(expected.getName());
        when(resultSet.getString("email")).thenReturn(expected.getEmail());
        when(resultSet.getString("password")).thenReturn(expected.getPassword());
        when(resultSet.getInt("age")).thenReturn(expected.getAge());
        when(resultSet.getString("gender")).thenReturn(expected.getGender().toString());

        //When
        Customer actual = customerRowMapper.mapRow(resultSet,1);

        //Then
        assertThat(actual).isEqualTo(expected);
    }

}