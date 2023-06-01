package com.terrence.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                select id, name, email, age 
                from customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        var sql = """
                select id, name, email, age 
                from customer
                where id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper,customerId).stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name,email,age)
                VALUES(?,?,?)
                """;
        jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
                select id, name, email, age 
                from customer
                where name = ?
                """;
        return !jdbcTemplate.query(sql, customerRowMapper,email).isEmpty();
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        var sql = """
                delete from customer
                where id = ?
                """;
        jdbcTemplate.update(sql,customerId);
    }

    @Override
    public boolean existsCustomerWithId(Integer customerId) {
        var sql = """
                select id, name, email, age 
                from customer
                where id = ?
                """;
        return !jdbcTemplate.query(sql, customerRowMapper,customerId).isEmpty();
    }

    @Override
    public void updateCustomer(Customer customer) {
        if(customer.getName() != null) {
            var sql = "update customer set name = ? where id = ?";
            jdbcTemplate.update(sql, customer.getName(), customer.getId());
        }
        if(customer.getEmail() != null) {
            var sql = "update customer set email = ? where id = ?";
            jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
        }
        if(customer.getAge() != null) {
            var sql = "update customer set age = ? where id = ?";
            jdbcTemplate.update(sql, customer.getAge(), customer.getId());
        }
    }
}
