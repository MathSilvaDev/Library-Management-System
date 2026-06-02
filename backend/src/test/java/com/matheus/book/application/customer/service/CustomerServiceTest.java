package com.matheus.book.application.customer.service;

import com.matheus.book.application.customer.dto.request.CreateCustomerRequest;
import com.matheus.book.application.customer.dto.response.CustomerResponse;
import com.matheus.book.application.customer.enums.CustomerFilter;
import com.matheus.book.domain.book.entity.Book;
import com.matheus.book.domain.customer.entity.Customer;
import com.matheus.book.domain.customer.repository.CustomerRepository;
import com.matheus.book.domain.publisher.entity.Publisher;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private final String name = "john";

    @Nested
    class Create{

        @Test
        void shouldCreateSuccessfully(){

            CreateCustomerRequest request =
                    new CreateCustomerRequest(name);

            Customer customer = new Customer(request.name());

            when(customerRepository.save(any(Customer.class)))
                    .thenReturn(customer);

            CustomerResponse response = customerService.create(request);

            assertEquals(request.name(), response.name());

            verify(customerRepository).save(any(Customer.class));
        }
    }

    @Nested
    class FindByName{

        @Test
        void shouldFindAllIfNameIsNull(){
            Customer customer = new Customer(name);

            Page<Customer> page = new PageImpl<>(List.of(customer));

            when(customerRepository.findAllByName(
                    isNull(),
                    any(Pageable.class)))
                    .thenReturn(page);

            Page<CustomerResponse> response =
                    customerService.findAllByName(null, CustomerFilter.ALL, 0, 20);

            assertEquals(1, response.getContent().size());

            verify(customerRepository).findAllByName(
                    isNull(),
                    any(Pageable.class)
            );
        }

        @Test
        void shouldFilterByNameIfNameExists(){
            Customer customer = new Customer(name);
            Page<Customer> page = new PageImpl<>(List.of(customer));

            when(customerRepository.findAllByName(eq(name), any(Pageable.class)))
                    .thenReturn(page);

            Page<CustomerResponse> response =
                    customerService.findAllByName(name, CustomerFilter.ALL, 0, 20);

            assertEquals(1, response.getContent().size());

            verify(customerRepository).findAllByName(
                    eq(name),
                    any(Pageable.class));
        }
    }

    @Nested
    class Delete{

        @Test
        void shouldThrowIfCustomerDoesNotExist(){

            Long id = 1L;

            when(customerRepository.findById(id))
                    .thenReturn(Optional.empty());

            assertThrows(ResponseStatusException.class,
                    () -> customerService.delete(id));

            verify(customerRepository).findById(id);
            verify(customerRepository, never()).delete(any(Customer.class));
        }

        @Test
        void shouldThrowIfCustomerHasBooks(){
            Long id = 1L;

            Customer customer = new Customer(name);
            Publisher publisher = new Publisher("publisher");
            Book book = new Book("book", publisher, null, 1);

            customer.getBooks().add(book);

            when(customerRepository.findById(id))
                    .thenReturn(Optional.of(customer));

            assertThrows(ResponseStatusException.class,
                    () -> customerService.delete(id));

            verify(customerRepository).findById(id);
            verify(customerRepository, never()).delete(customer);
        }

        @Test
        void shouldDeleteSuccessfully(){
            Long id = 1L;
            Customer customer = new Customer(name);

            when(customerRepository.findById(id))
                    .thenReturn(Optional.of(customer));

            customerService.delete(id);

            verify(customerRepository).findById(id);
            verify(customerRepository).delete(customer);
        }
    }
}
