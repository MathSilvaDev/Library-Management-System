package com.matheus.book.application.publisher.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreatePublisherRequest(
        @NotBlank
        String name
) { }
