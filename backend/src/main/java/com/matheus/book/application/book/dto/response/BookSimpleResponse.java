package com.matheus.book.application.book.dto.response;

public record BookSimpleResponse(
        Long id,
        String name,
        String publisherName,
        boolean available
) { }
