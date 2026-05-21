package com.matheus.book.application.customer.service;

import com.matheus.book.application.customer.dto.request.CreateCustomerRequest;
import com.matheus.book.application.customer.dto.response.CustomerResponse;
import com.matheus.book.domain.customer.entity.Customer;
import com.matheus.book.domain.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    public List<CustomerResponse> findAllByName(String name){

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

    @Transactional
    public void delete(Long id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));

        if(!customer.getBooks().isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This customer cannot be deleted because they have books");
        }

        customerRepository.delete(customer);
    }

    private CustomerResponse toResponse(Customer customer){
        return new CustomerResponse(
                customer.getId(),
                customer.getName()
        );
    }
}
