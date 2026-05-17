package com.matheus.book.application.book.dto.response;

import java.time.LocalDate;

public record BookResponse(
        Long id,
        String name,
        Integer quantity,
        String publisherName,
        LocalDate publishedIn
) { }
