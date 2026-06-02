package com.matheus.book.application.publisher.service;

import com.matheus.book.application.publisher.dto.request.CreatePublisherRequest;
import com.matheus.book.application.publisher.dto.response.PublisherResponse;
import com.matheus.book.application.publisher.enums.PublisherFilter;
import com.matheus.book.domain.book.entity.Book;
import com.matheus.book.domain.publisher.entity.Publisher;
import com.matheus.book.domain.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    private final String name = "john";

    @Nested
    class Create{

        @Test
        void shouldCreateSuccessfully(){

            CreatePublisherRequest request =
                    new CreatePublisherRequest(name);

            Publisher publisher = new Publisher(request.name());

            when(publisherRepository.save(any(Publisher.class)))
                    .thenReturn(publisher);

            PublisherResponse response = publisherService.create(request);

            assertEquals(request.name(), response.name());

            verify(publisherRepository).save(any(Publisher.class));
        }
    }

    @Nested
    class FindByName{

        @Test
        void shouldFindAllIfNameIsNull(){
            Publisher publisher = new Publisher(name);
            Page<Publisher> page = new PageImpl<>(List.of(publisher));

            when(publisherRepository.findAllByName(isNull(), any(Pageable.class)))
                    .thenReturn(page);

            Page<PublisherResponse> response =
                    publisherService.findAllByName(null, PublisherFilter.ALL, 0 ,20);

            assertEquals(1, response.getContent().size());

            verify(publisherRepository).findAllByName(isNull(), any(Pageable.class));
        }

        @Test
        void shouldFilterByNameIfNameExists(){
            Publisher publisher = new Publisher(name);
            Page<Publisher> page = new PageImpl<>(List.of(publisher));

            when(publisherRepository.findAllByName(eq(name), any(Pageable.class)))
                    .thenReturn(page);

            Page<PublisherResponse> response =
                    publisherService.findAllByName(name, PublisherFilter.ALL, 0 , 20);

            assertEquals(1, response.getContent().size());

            verify(publisherRepository).findAllByName(eq(name), any(Pageable.class));

        }
    }

    @Nested
    class Delete{

        @Test
        void shouldThrowIfPublisherDoesNotExist(){

            Long id = 1L;

            when(publisherRepository.findById(id))
                    .thenReturn(Optional.empty());

            assertThrows(ResponseStatusException.class,
                    () -> publisherService.delete(id));

            verify(publisherRepository).findById(id);
            verify(publisherRepository, never()).delete(any(Publisher.class));
        }

        @Test
        void shouldThrowIfPublisherHasBooks(){
            Long id = 1L;
            Publisher publisher = new Publisher(name);
            Book book = new Book("book", publisher, null, 1);
            publisher.getBooks().add(book);

            when(publisherRepository.findById(id))
                    .thenReturn(Optional.of(publisher));

            assertThrows(ResponseStatusException.class,
                    () -> publisherService.delete(id));

            verify(publisherRepository).findById(id);
            verify(publisherRepository, never()).delete(publisher);
        }

        @Test
        void shouldDeleteSuccessfully(){
            Long id = 1L;
            Publisher publisher = new Publisher(name);

            when(publisherRepository.findById(id))
                    .thenReturn(Optional.of(publisher));

            publisherService.delete(id);

            verify(publisherRepository).findById(id);
            verify(publisherRepository).delete(publisher);
        }
    }

}
