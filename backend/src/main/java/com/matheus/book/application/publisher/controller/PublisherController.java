package com.matheus.book.application.publisher.controller;

import com.matheus.book.application.publisher.dto.request.CreatePublisherRequest;
import com.matheus.book.application.publisher.dto.response.PublisherResponse;
import com.matheus.book.application.publisher.enums.PublisherFilter;
import com.matheus.book.application.publisher.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @PostMapping
    public ResponseEntity<PublisherResponse> create(
            @Valid @RequestBody CreatePublisherRequest request){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(publisherService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<PublisherResponse>> findAllByName(
            @RequestParam(required = false) String name,
            @RequestParam PublisherFilter filter){

        return ResponseEntity.ok(
                publisherService.findAllByName(name, filter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        publisherService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
