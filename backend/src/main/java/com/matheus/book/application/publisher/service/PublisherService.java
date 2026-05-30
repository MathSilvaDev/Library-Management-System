package com.matheus.book.application.publisher.service;

import com.matheus.book.application.publisher.dto.request.CreatePublisherRequest;
import com.matheus.book.application.publisher.dto.request.EditPublisherRequest;
import com.matheus.book.application.publisher.dto.response.PublisherResponse;
import com.matheus.book.application.publisher.enums.PublisherFilter;
import com.matheus.book.domain.book.entity.Book;
import com.matheus.book.domain.publisher.entity.Publisher;
import com.matheus.book.domain.publisher.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
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

    public Page<PublisherResponse> findAllByName(String name, PublisherFilter filter,
                                                 int page, int size){
        Page<Publisher> publishers;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.asc("name").ignoreCase()
                )
        );

        switch (filter){
            case ALL -> publishers = publisherRepository.findAllByName(name, pageable);

            case WITH_BORROWED_BOOKS ->
                    publishers = publisherRepository
                            .findWithBorrowedBooksByName(name, pageable);

            case WITHOUT_BORROWED_BOOKS ->
                    publishers = publisherRepository
                            .findWithoutBorrowedBooksByName(name, pageable);

            default -> publishers = publisherRepository.findAll(pageable);
        }

        return publishers.map(this::toResponse);
    }

    public PublisherResponse findById(Long id){
        Publisher publisher = findPublisherById(id);

        return toResponse(publisher);
    }

    public int info(){
        return publisherRepository.findAll().size();
    }

    @Transactional
    public void editInfo(Long id, EditPublisherRequest request){
        Publisher publisher = findPublisherById(id);

        publisher.edit(request.name());
    }

    @Transactional
    public void delete(Long id){
        Publisher publisher = findPublisherById(id);

        if(!publisher.getBooks().isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This publisher cannot be deleted because they have books");
        }

        publisherRepository.delete(publisher);
    }

    private Publisher findPublisherById(Long id){
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Publisher not found"));
    }

    private PublisherResponse toResponse(Publisher publisher){
        return new PublisherResponse(
                publisher.getId(),
                publisher.getName()
        );
    }

}
