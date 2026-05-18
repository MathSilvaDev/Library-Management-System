package com.matheus.book.application.book.service;

import com.matheus.book.domain.book.repository.BookRepository;
import com.matheus.book.domain.customer.repository.CustomerRepository;
import com.matheus.book.domain.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

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

    }

    @Nested
    class FindByName{

    }

    @Nested
    class Delete{

    }

    @Nested
    class BorrowBook{

    }

    @Nested
    class ReturnBook{

    }

}