package com.matheus.book.application.customer.controller;

import com.matheus.book.application.customer.dto.request.CreateCustomerRequest;
import com.matheus.book.application.customer.dto.request.EditCustomerRequest;
import com.matheus.book.application.customer.dto.response.CustomerResponse;
import com.matheus.book.application.customer.enums.CustomerFilter;
import com.matheus.book.application.customer.service.CustomerService;
import com.matheus.book.application.publisher.dto.request.EditPublisherRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(
            @Valid @RequestBody CreateCustomerRequest request){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAllByName(
            @RequestParam(required = false) String name,
            @RequestParam CustomerFilter filter){

        return ResponseEntity.ok(
                customerService.findAllByName(name, filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable Long id){

        return ResponseEntity.ok(
                customerService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> editInfo(@PathVariable Long id,
                                         @Valid @RequestBody EditCustomerRequest request){
        customerService.editInfo(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        
        customerService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
