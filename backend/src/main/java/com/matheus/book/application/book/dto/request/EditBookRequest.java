package com.matheus.book.application.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EditBookRequest(
        @NotBlank
        String name,

        @NotNull
        Integer quantity,

        LocalDate publishedIn,
        Long publisherId
) { }
