package com.matheus.book.application.publisher.service;

import com.matheus.book.application.publisher.dto.request.CreatePublisherRequest;
import com.matheus.book.application.publisher.dto.response.PublisherResponse;
import com.matheus.book.application.publisher.enums.PublisherFilter;
import com.matheus.book.domain.publisher.entity.Publisher;
import com.matheus.book.domain.publisher.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherResponse create(CreatePublisherRequest request){
        Publisher publisher = new Publisher(request.name());

        publisherRepository.save(publisher);

        return toResponse(publisher);
    }

    public List<PublisherResponse> findAllByName(String name, PublisherFilter filter){

        List<Publisher> publishers;

        switch (filter){
            case ALL -> publishers = publisherRepository.findAllByName(name);
            case WITH_BORROWED_BOOKS ->
                    publishers = publisherRepository.findWithBorrowedBooksByName(name);

            case WITHOUT_BORROWED_BOOKS ->
                    publishers = publisherRepository.findWithoutBorrowedBooksByName(name);

            default -> publishers = publisherRepository.findAll();
        }

        return publishers.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long id){
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Publisher not found"));

        if(!publisher.getBooks().isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This publisher cannot be deleted because they have books");
        }

        publisherRepository.delete(publisher);
    }

    private PublisherResponse toResponse(Publisher publisher){
        return new PublisherResponse(
                publisher.getId(),
                publisher.getName()
        );
    }

}
