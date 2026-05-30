package com.matheus.book.application.book.service;

import com.matheus.book.application.book.dto.request.CreateBookRequest;
import com.matheus.book.application.book.dto.request.EditBookRequest;
import com.matheus.book.application.book.dto.response.BookResponse;
import com.matheus.book.application.book.enums.BookFilter;
import com.matheus.book.application.customer.dto.response.CustomerSimpleResponse;
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

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CustomerRepository customerRepository;

    public BookResponse create(Long publisherId, CreateBookRequest request){

        Publisher publisher = findPublisherById(publisherId);
        Book book = new Book(
                request.name(),
                publisher,
                request.publishedIn(),
                request.quantity()
        );

        bookRepository.save(book);

        return toResponse(book);
    }

    public List<BookResponse> findAllByName(String name, BookFilter filter){

        List<Book> books;

        switch (filter){
            case ALL -> books = bookRepository.findAllByName(name);
            case AVAILABLE -> books = bookRepository.findAvailableByName(name);
            case UNAVAILABLE -> books = bookRepository.findUnavailableByName(name);
            default -> books = bookRepository.findAll();

        }

        return books.stream()
                .sorted(Comparator.comparing(
                                Book::getName,
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
                .map(this::toResponse)
                .toList();
    }

    public BookResponse findById(Long id){
        Book book = findBookById(id);

        return toResponse(book);
    }

    @Transactional
    public void editInfo(Long bookId, EditBookRequest request){
        Book book = findBookById(bookId);
        Publisher publisher = findPublisherById(request.publisherId());

        book.edit(
                request.name(),
                publisher,
                request.publishedIn(),
                request.quantity()
        );
    }

    @Transactional
    public void delete(Long id){
        Book book = findBookById(id);

        if(!book.getCustomers().isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "The book cannot be deleted because it is currently borrowed");
        }

        bookRepository.delete(book);
    }

    @Transactional
    public void borrowBook(Long bookId ,Long customerId){

        Book book = findBookById(bookId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));

        book.borrowBook(customer);
    }

    @Transactional
    public void returnBook(Long bookId, Long customerId){

        Book book = bookRepository.findByIdAndCustomers_Id(bookId, customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Book or customer not found"
                ));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        book.returnBook(customer);
    }

    private Book findBookById(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Book not found"));
    }

    private Publisher findPublisherById(Long id){
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Publisher not found"));
    }

    private BookResponse toResponse(Book book){
        return new BookResponse(
                book.getId(),
                book.getName(),
                book.getQuantity(),
                book.getCustomers().size(),
                book.getPublisher().getName(),
                book.getPublishedIn(),
                book.getPublisher().getId(),
                book.getCustomers()
                        .stream()
                        .map(customer -> new CustomerSimpleResponse(
                                customer.getId(),
                                customer.getName()
                        )).toList()
        );
    }

}
