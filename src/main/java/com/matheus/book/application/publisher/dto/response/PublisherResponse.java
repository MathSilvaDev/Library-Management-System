package com.matheus.book.application.publisher.dto.response;

import java.util.List;

public record PublisherResponse(
        Long id,
        String name,
        List<String> books
) { }
