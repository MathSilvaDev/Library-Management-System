package com.matheus.book.application.publisher.controller;

import com.matheus.book.application.publisher.dto.request.CreatePublisherRequest;
import com.matheus.book.application.publisher.dto.response.CreatePublisherResponse;
import com.matheus.book.application.publisher.dto.response.PublisherResponse;
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
    public ResponseEntity<CreatePublisherResponse> create(
            @Valid @RequestBody CreatePublisherRequest request){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(publisherService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<PublisherResponse>> findAll(){
        return ResponseEntity.ok(
                publisherService.findAll()
        );
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<PublisherResponse>> findByName(@PathVariable String name){
        return ResponseEntity.ok(
                publisherService.findByName(name)
        );
    }
}
