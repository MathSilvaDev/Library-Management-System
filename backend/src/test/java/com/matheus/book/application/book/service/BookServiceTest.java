package com.matheus.book.application.book.service;

import com.matheus.book.application.book.dto.request.CreateBookRequest;
import com.matheus.book.application.book.dto.response.BookResponse;
import com.matheus.book.domain.book.entity.Book;
import com.matheus.book.domain.book.repository.BookRepository;
import com.matheus.book.domain.customer.entity.Customer;
import com.matheus.book.domain.customer.repository.CustomerRepository;
import com.matheus.book.domain.publisher.entity.Publisher;
import com.matheus.book.domain.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private BookService bookService;

    @Nested
    class Create{

        @Test
        void shouldThrowPublisherNotFound(){
            Long publisherId = 1L;

            CreateBookRequest request = new CreateBookRequest(
                    "book",
                    1,
                    null
            );

            when(publisherRepository.findById(publisherId))
                    .thenReturn(Optional.empty());

            assertThrows(ResponseStatusException.class, () ->
                    bookService.create(publisherId, request));

            verify(publisherRepository).findById(publisherId);
            verify(bookRepository, never()).save(any(Book.class));
        }

        @Test
        void shouldCreateSuccessfully(){
            Publisher publisher = new Publisher("publisher");
            CreateBookRequest request = new CreateBookRequest(
                    "book",
                    1,
                    null
            );

            Book book = new Book("book", publisher, null, 1);

            when(publisherRepository.findById(publisher.getId()))
                    .thenReturn(Optional.of(publisher));

            when(bookRepository.save(any(Book.class)))
                    .thenReturn(book);

            BookResponse response = bookService.create(publisher.getId(), request);

            assertEquals(request.name(), response.name());
            assertEquals(request.quantity(), response.quantity());

            verify(publisherRepository).findById(publisher.getId());
            verify(bookRepository).save(any(Book.class));
        }
    }

    @Nested
    class FindByName{

        @Test
        void shouldFindAllIfNameIsNull(){
            Publisher publisher = new Publisher("publisher");
            Book book = new Book("book", publisher, null, 1);

            when(bookRepository.findAll())
                    .thenReturn(List.of(book));

            List<BookResponse> response =
                    bookService.findByName(null);

            assertEquals(1, response.size());

            verify(bookRepository).findAll();
        }

        @Test
        void shouldFilterByNameIfNameExists(){
            Publisher publisher = new Publisher("publisher");
            Book book = new Book("book", publisher, null, 1);

            when(bookRepository.findByNameContainingIgnoreCase("book"))
                    .thenReturn(List.of(book));

            List<BookResponse> response =
                    bookService.findByName("book");

            assertEquals(1, response.size());

            verify(bookRepository).findByNameContainingIgnoreCase("book");

        }
    }

    @Nested
    class Delete{

        @Test
        void shouldThrowIfBookDoesNotExist(){
            Long id = 1L;

            when(bookRepository.findById(id))
                    .thenReturn(Optional.empty());

            assertThrows(ResponseStatusException.class,
                    () -> bookService.delete(id));

            verify(bookRepository).findById(id);
            verify(bookRepository, never()).delete(any(Book.class));
        }

        @Test
        void shouldThrowIfBookHasCustomers(){
            Long id = 1L;

            Customer customer = new Customer("customer");
            Publisher publisher = new Publisher("publisher");
            Book book = new Book("book", publisher, null, 1);

            book.getCustomers().add(customer);

            when(bookRepository.findById(id))
                    .thenReturn(Optional.of(book));

            assertThrows(ResponseStatusException.class,
                    () -> bookService.delete(id));

            verify(bookRepository).findById(id);
            verify(bookRepository, never()).delete(book);
        }

        @Test
        void shouldDeleteSuccessfully(){
            Long id = 1L;

            Publisher publisher = new Publisher("publisher");
            Book book = new Book ("book", publisher, null, 1);

            when(bookRepository.findById(id))
                    .thenReturn(Optional.of(book));

            bookService.delete(id);

            verify(bookRepository).findById(id);
            verify(bookRepository).delete(book);
        }
    }

    @Nested
    class BorrowBook{

        @Test
        void shouldThrowIfBookNotFound(){
            Long bookId = 1L;
            Long customerId = 2L;

            when(bookRepository.findById(bookId))
                    .thenReturn(Optional.empty());

            assertThrows(ResponseStatusException.class,
                    () -> bookService.borrowBook(bookId, customerId));

            verify(bookRepository).findById(bookId);
            verify(customerRepository, never()).findById(customerId);
        }

        @Test
        void shouldThrowIfCustomerNotFound(){
            Long bookId = 1L;
            Long customerId = 2L;

            Publisher publisher = new Publisher("publisher");
            Book book = new Book("book", publisher, null, 3);

            when(bookRepository.findById(bookId))
                    .thenReturn(Optional.of(book));

            when(customerRepository.findById(customerId))
                    .thenReturn(Optional.empty());

            assertThrows(ResponseStatusException.class,
                    () -> bookService.borrowBook(bookId, customerId));

            verify(bookRepository).findById(bookId);
            verify(customerRepository).findById(customerId);
        }

        @Test
        void shouldThrowIfBookIsNotAvailable(){
            Long bookId = 1L;
            Long customerId = 2L;

            Customer customer = new Customer("customer");
            Publisher publisher = new Publisher("publisher");
            Book book = new Book("book", publisher, null, 0);

            when(bookRepository.findById(bookId))
                    .thenReturn(Optional.of(book));

            when(customerRepository.findById(customerId))
                    .thenReturn(Optional.of(customer));

            assertThrows(ResponseStatusException.class,
                    () -> bookService.borrowBook(bookId, customerId));

            verify(bookRepository).findById(bookId);
            verify(customerRepository).findById(customerId);
        }

        @Test
        void shouldBorrowBookSuccessfully(){
            Long bookId = 1L;
            Long customerId = 2L;

            Customer customer = new Customer("customer");
            Publisher publisher = new Publisher("publisher");
            Book book = new Book("book", publisher, null, 3);

            when(bookRepository.findById(bookId))
                    .thenReturn(Optional.of(book));

            when(customerRepository.findById(customerId))
                    .thenReturn(Optional.of(customer));

            bookService.borrowBook(bookId, customerId);

            verify(bookRepository).findById(bookId);
            verify(customerRepository).findById(customerId);
        }
    }

    @Nested
    class ReturnBook{

        @Test
        void shouldThrowIfBookNotFound(){
            Long customerId = 1L;

            when(bookRepository.findByIdAndCustomers_Id(null, customerId))
                    .thenReturn(Optional.empty());

            assertThrows(ResponseStatusException.class,
                    () -> bookService.returnBook(null, customerId));

            verify(bookRepository).findByIdAndCustomers_Id(null, customerId);
        }

        @Test
        void shouldThrowIfCustomerNotFound(){
            Long bookId = 1L;

            when(bookRepository.findByIdAndCustomers_Id(bookId, null))
                    .thenReturn(Optional.empty());

            assertThrows(ResponseStatusException.class,
                    () -> bookService.returnBook(bookId, null));

            verify(bookRepository).findByIdAndCustomers_Id(bookId, null);
        }

        @Test
        void shouldThrowIfCustomerHasNotBorrowedBook(){
            Long bookId = 1L;
            Long customerId = 2L;

            Customer customer = new Customer("customer");
            Publisher publisher = new Publisher("publisher");
            Book book = new Book("book", publisher, null, 1);

            when(bookRepository.findByIdAndCustomers_Id(bookId, customerId))
                    .thenReturn(Optional.of(book));

            when(customerRepository.findById(customerId))
                    .thenReturn(Optional.of(customer));

            assertThrows(ResponseStatusException.class,
                    () -> bookService.returnBook(bookId, customerId));

            verify(bookRepository).findByIdAndCustomers_Id(bookId, customerId);
            verify(customerRepository).findById(customerId);
        }

        @Test
        void shouldReturnBookSuccessfully(){
            Long bookId = 1L;
            Long customerId = 2L;

            Customer customer = new Customer("customer");
            Publisher publisher = new Publisher("publisher");
            Book book = new Book("book", publisher, null, 1);

            book.borrowBook(customer);

            when(bookRepository.findByIdAndCustomers_Id(bookId, customerId))
                    .thenReturn(Optional.of(book));

            when(customerRepository.findById(customerId))
                    .thenReturn(Optional.of(customer));


            bookService.returnBook(bookId, customerId);

            verify(bookRepository).findByIdAndCustomers_Id(bookId, customerId);
            verify(customerRepository).findById(customerId);
        }
    }

}