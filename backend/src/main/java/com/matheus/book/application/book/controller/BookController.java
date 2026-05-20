package com.matheus.book.application.book.controller;

import com.matheus.book.application.book.dto.request.CreateBookRequest;
import com.matheus.book.application.book.dto.response.BookResponse;
import com.matheus.book.application.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/{publisherId}")
    public ResponseEntity<BookResponse> create(@PathVariable Long publisherId,
                                               @Valid @RequestBody CreateBookRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.create(publisherId, request));
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> findByName(
            @RequestParam(required = false) String name){

        return ResponseEntity.ok(
                bookService.findByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id){

        return ResponseEntity.ok(
                bookService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bookId}/borrow/{customerId}")
    public ResponseEntity<Void> borrowBook(@PathVariable Long bookId,
                                           @PathVariable Long customerId){
        bookService.borrowBook(bookId, customerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bookId}/return/{customerId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long bookId,
                                           @PathVariable Long customerId){
        bookService.returnBook(bookId,customerId);
        return ResponseEntity.noContent().build();
    }
}
