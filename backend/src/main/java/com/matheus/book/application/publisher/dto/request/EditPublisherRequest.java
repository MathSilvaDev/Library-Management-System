package com.matheus.book.application.publisher.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EditPublisherRequest(
        @NotBlank
        String name
) { }
