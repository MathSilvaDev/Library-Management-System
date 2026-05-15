package com.matheus.book.application.customer.service;

import com.matheus.book.application.customer.dto.request.CreateCustomerRequest;
import com.matheus.book.application.customer.dto.response.CreateCustomerResponse;
import com.matheus.book.application.customer.dto.response.CustomerResponse;
import com.matheus.book.domain.book.entity.Book;
import com.matheus.book.domain.customer.entity.Customer;
import com.matheus.book.domain.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CreateCustomerResponse create(CreateCustomerRequest request){
        Customer customer = new Customer(request.name());

        customerRepository.save(customer);

        return toCreateResponse(customer);
    }

    public List<CustomerResponse> findAll(){
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CustomerResponse> findByName(String name){
        return customerRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CreateCustomerResponse toCreateResponse(Customer customer){
        return new CreateCustomerResponse(
                customer.getId(),
                customer.getName()
        );
    }

    private CustomerResponse toResponse(Customer customer){
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getBooks()
                        .stream()
                        .map(Book::getName)
                        .toList()
        );
    }
}
