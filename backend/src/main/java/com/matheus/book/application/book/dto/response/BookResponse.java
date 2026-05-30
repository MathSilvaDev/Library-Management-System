package com.matheus.book.application.book.dto.response;

import com.matheus.book.application.customer.dto.response.CustomerSimpleResponse;

import java.time.LocalDate;
import java.util.List;

public record BookResponse(
        Long id,
        String name,
        Integer quantity,
        Integer borrowedQuantity,
        String publisherName,
        LocalDate publishedIn,
        Long publisherId,
        List<CustomerSimpleResponse> customerSimpleResponses
) { }
