package com.fin.customer_service.service;


import com.fin.customer_service.entity.Customer;
import com.fin.customer_service.repository.CustomerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    private static final String CUSTOMER_SERVICE = "customerService";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @CircuitBreaker(name = CUSTOMER_SERVICE, fallbackMethod = "fallbackCreateCustomer")
    public Customer createCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);

        // Send notification via Kafka
        kafkaTemplate.send("customer-created",
                "New customer created: " + savedCustomer.getCustomerName() +
                        " with ID: " + savedCustomer.getCustomerId());

        return savedCustomer;
    }

    @CircuitBreaker(name = CUSTOMER_SERVICE, fallbackMethod = "fallbackGetAllCustomers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @CircuitBreaker(name = CUSTOMER_SERVICE, fallbackMethod = "fallbackGetCustomerById")
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @CircuitBreaker(name = CUSTOMER_SERVICE, fallbackMethod = "fallbackUpdateCustomer")
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customer.setCustomerName(customerDetails.getCustomerName());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhoneNumber(customerDetails.getPhoneNumber());
        customer.setAddress(customerDetails.getAddress());
        customer.setDateOfBirth(customerDetails.getDateOfBirth());
        customer.setAccountStatus(customerDetails.getAccountStatus());

        return customerRepository.save(customer);
    }

    @CircuitBreaker(name = CUSTOMER_SERVICE, fallbackMethod = "fallbackDeleteCustomer")
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customerRepository.delete(customer);

        // Send notification via Kafka
        kafkaTemplate.send("customer-deleted",
                "Customer deleted: " + customer.getCustomerName() +
                        " with ID: " + customer.getCustomerId());
    }

    // Fallback methods
    public Customer fallbackCreateCustomer(Customer customer, Throwable t) {
        throw new RuntimeException("Customer service is unavailable. Please try again later.");
    }

    public List<Customer> fallbackGetAllCustomers(Throwable t) {
        throw new RuntimeException("Customer service is unavailable. Please try again later.");
    }

    public Optional<Customer> fallbackGetCustomerById(Long id, Throwable t) {
        throw new RuntimeException("Customer service is unavailable. Please try again later.");
    }

    public Customer fallbackUpdateCustomer(Long id, Customer customerDetails, Throwable t) {
        throw new RuntimeException("Customer service is unavailable. Please try again later.");
    }

    public void fallbackDeleteCustomer(Long id, Throwable t) {
        throw new RuntimeException("Customer service is unavailable. Please try again later.");
    }
}