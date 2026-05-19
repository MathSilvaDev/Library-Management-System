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

    }

    @Nested
    class ReturnBook{

    }

}