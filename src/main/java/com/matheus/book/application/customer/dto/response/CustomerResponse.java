package com.matheus.book.application.customer.dto.response;

import java.util.List;

public record CustomerResponse(
        Long id,
        String name,
        List<String> books
) { }
