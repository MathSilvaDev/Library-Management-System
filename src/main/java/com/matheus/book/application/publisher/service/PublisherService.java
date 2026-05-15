package com.matheus.book.application.publisher.service;

import com.matheus.book.application.publisher.dto.request.CreatePublisherRequest;
import com.matheus.book.application.publisher.dto.response.CreatePublisherResponse;
import com.matheus.book.application.publisher.dto.response.PublisherResponse;
import com.matheus.book.domain.book.entity.Book;
import com.matheus.book.domain.publisher.entity.Publisher;
import com.matheus.book.domain.publisher.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public CreatePublisherResponse create(CreatePublisherRequest request){
        Publisher publisher = new Publisher(request.name());

        publisherRepository.save(publisher);

        return toCreateResponse(publisher);
    }

    public List<PublisherResponse> findAll(){
        return publisherRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PublisherResponse> findByName(String name){
        return publisherRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CreatePublisherResponse toCreateResponse(Publisher publisher){
        return new CreatePublisherResponse(
                publisher.getId(),
                publisher.getName()
        );
    }

    private PublisherResponse toResponse(Publisher publisher){
        return new PublisherResponse(
                publisher.getId(),
                publisher.getName(),
                publisher.getBooks()
                        .stream()
                        .map(Book::getName)
                        .toList()
        );
    }

}
