package com.matheus.book.application.book.controller;

import com.matheus.book.application.book.dto.request.CreateBookRequest;
import com.matheus.book.application.book.dto.request.EditBookRequest;
import com.matheus.book.application.book.dto.response.BookResponse;
import com.matheus.book.application.book.enums.BookFilter;
import com.matheus.book.application.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<BookResponse>> findAllByName(
            @RequestParam(required = false) String name,
            @RequestParam BookFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){

        return ResponseEntity.ok(
                bookService.findAllByName(name, filter, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id){

        return ResponseEntity.ok(
                bookService.findById(id));
    }

    @GetMapping("/info")
    public ResponseEntity<Integer> info(){
        return ResponseEntity.ok(
                bookService.info());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> editInfo(@PathVariable Long id,
                                         @Valid @RequestBody EditBookRequest request){
        bookService.editInfo(id, request);

        return ResponseEntity.noContent().build();
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
