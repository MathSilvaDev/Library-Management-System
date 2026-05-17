package com.matheus.book.application.customer.service;

import com.matheus.book.application.customer.dto.request.CreateCustomerRequest;
import com.matheus.book.application.customer.dto.response.CustomerResponse;
import com.matheus.book.domain.customer.entity.Customer;
import com.matheus.book.domain.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponse create(CreateCustomerRequest request){
        Customer customer = new Customer(request.name());

        customerRepository.save(customer);

        return toResponse(customer);
    }

    public List<CustomerResponse> findByName(String name){

        List<Customer> customers;

        if(name == null || name.isBlank()){
            customers = customerRepository.findAll();
        }else{
            customers = customerRepository.findByNameContainingIgnoreCase(name);
        }

        return customers.stream()
                .map(this::toResponse)
                .toList();
    }

    private CustomerResponse toResponse(Customer customer){
        return new CustomerResponse(
                customer.getId(),
                customer.getName()
        );
    }
}
