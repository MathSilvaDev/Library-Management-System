package com.matheus.book.application.customer.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EditCustomerRequest(
        @NotBlank
        String name
) { }
