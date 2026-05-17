package com.matheus.book.application.book.service;

import com.matheus.book.application.book.dto.request.CreateBookRequest;
import com.matheus.book.application.book.dto.response.BookResponse;
import com.matheus.book.domain.book.entity.Book;
import com.matheus.book.domain.book.repository.BookRepository;
import com.matheus.book.domain.customer.entity.Customer;
import com.matheus.book.domain.customer.repository.CustomerRepository;
import com.matheus.book.domain.publisher.entity.Publisher;
import com.matheus.book.domain.publisher.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CustomerRepository customerRepository;

    public BookResponse create(Long publisherId, CreateBookRequest request){

        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Publisher not found"));

        Book book = new Book(
                request.name(),
                publisher,
                request.publishedIn(),
                request.quantity()
        );

        bookRepository.save(book);

        return toResponse(book);
    }

    public List<BookResponse> findByName(String name){

        List<Book> books;

        if(name == null || name.isBlank()){
            books = bookRepository.findAll();
        }else{
            books = bookRepository.findByNameContainingIgnoreCase(name);
        }

        return books.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Book not found"));

        if(!book.getCustomers().isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "The book cannot be deleted because it is currently borrowed");
        }

        bookRepository.delete(book);
    }

    @Transactional
    public void borrowBook(Long bookId ,Long customerId){

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Book not found"));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));

        book.borrowBook(customer);
    }

    @Transactional
    public void returnBook(Long bookId, Long customerId){

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));

        Book book = bookRepository.findByIdAndCustomers_Id(bookId, customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Book not found"
                ));

        book.returnBook(customer);
    }

    private BookResponse toResponse(Book book){
        return new BookResponse(
                book.getId(),
                book.getName(),
                book.getQuantity(),
                book.getPublisher().getName(),
                book.getPublishedIn()
        );
    }

}
