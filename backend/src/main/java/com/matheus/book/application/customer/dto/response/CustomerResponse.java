package com.matheus.book.application.customer.dto.response;

import com.matheus.book.application.book.dto.response.BookSimpleResponse;

import java.util.List;

public record CustomerResponse(
        Long id,
        String name,
        List<BookSimpleResponse> bookSimpleResponses
) { }
