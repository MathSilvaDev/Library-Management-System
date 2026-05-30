package com.matheus.book.application.customer.controller;

import com.matheus.book.application.customer.dto.request.CreateCustomerRequest;
import com.matheus.book.application.customer.dto.request.EditCustomerRequest;
import com.matheus.book.application.customer.dto.response.CustomerResponse;
import com.matheus.book.application.customer.enums.CustomerFilter;
import com.matheus.book.application.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<CustomerResponse>> findAllByName(
            @RequestParam(required = false) String name,
            @RequestParam CustomerFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){

        return ResponseEntity.ok(
                customerService.findAllByName(name, filter, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable Long id){

        return ResponseEntity.ok(
                customerService.findById(id));
    }

    @GetMapping("/info")
    public ResponseEntity<Integer> info(){
        return ResponseEntity.ok(
                customerService.info());
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
