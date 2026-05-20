package com.matheus.book.application.book.dto.response;

import java.time.LocalDate;

public record BookResponse(
        Long id,
        String name,
        Integer quantity,
        Integer borrowedQuantity,
        String publisherName,
        LocalDate publishedIn
) { }
