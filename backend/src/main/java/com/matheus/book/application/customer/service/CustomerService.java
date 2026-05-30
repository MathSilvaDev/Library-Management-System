package com.matheus.book.application.customer.service;

import com.matheus.book.application.book.dto.response.BookSimpleResponse;
import com.matheus.book.application.customer.dto.request.CreateCustomerRequest;
import com.matheus.book.application.customer.dto.request.EditCustomerRequest;
import com.matheus.book.application.customer.dto.response.CustomerResponse;
import com.matheus.book.application.customer.enums.CustomerFilter;
import com.matheus.book.domain.customer.entity.Customer;
import com.matheus.book.domain.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponse create(CreateCustomerRequest request){
        Customer customer = new Customer(request.name());

        customerRepository.save(customer);

        return toResponse(customer);
    }

    public Page<CustomerResponse> findAllByName(String name, CustomerFilter filter,
                                                int page, int size){
        Page<Customer> customers;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.asc("name").ignoreCase()
                )
        );

        switch (filter){
            case ALL -> customers = customerRepository.findAllByName(name, pageable);
            case WITH_BORROWED_BOOKS ->
                    customers = customerRepository.findWithBorrowedBooksByName(name, pageable);

            case WITHOUT_BORROWED_BOOKS ->
                    customers = customerRepository.findWithoutBorrowedBooksByName(name, pageable);

            default -> customers = customerRepository.findAll(pageable);
        }

        return customers.map(this::toResponse);
    }

    public CustomerResponse findById(Long id){
        Customer customer = findCustomerById(id);

        return toResponse(customer);
    }

    public int info(){
        return customerRepository.findAll().size();
    }

    @Transactional
    public void editInfo(Long id, EditCustomerRequest request){
        Customer customer = findCustomerById(id);

        customer.edit(request.name());
    }

    @Transactional
    public void delete(Long id){
        Customer customer = findCustomerById(id);

        if(!customer.getBooks().isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This customer cannot be deleted because they have books");
        }

        customerRepository.delete(customer);
    }

    private Customer findCustomerById(Long id){
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));
    }

    private CustomerResponse toResponse(Customer customer){
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getBooks()
                        .stream()
                        .map((book) -> new BookSimpleResponse(
                                book.getId(),
                                book.getName(),
                                book.getPublisher().getName(),
                                book.isAvailable()
                        )).toList()
        );
    }

}
