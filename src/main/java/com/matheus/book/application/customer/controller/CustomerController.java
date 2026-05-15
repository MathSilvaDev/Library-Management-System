package com.matheus.book.application.customer.controller;

import com.matheus.book.application.customer.dto.request.CreateCustomerRequest;
import com.matheus.book.application.customer.dto.response.CreateCustomerResponse;
import com.matheus.book.application.customer.dto.response.CustomerResponse;
import com.matheus.book.application.customer.service.CustomerService;
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
    public ResponseEntity<CreateCustomerResponse> create(
            @Valid @RequestBody CreateCustomerRequest request){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll(){
        return ResponseEntity.ok(
                customerService.findAll()
        );
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<CustomerResponse>> findByName(@PathVariable String name){
        return ResponseEntity.ok(
                customerService.findByName(name)
        );
    }
}
