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

            when(publisherRepository.findAllByName(null))
                    .thenReturn(List.of(publisher));

            List<PublisherResponse> response =
                    publisherService.findAllByName(null, PublisherFilter.ALL);

            assertEquals(1, response.size());

            verify(publisherRepository).findAllByName(null);
        }

        @Test
        void shouldFilterByNameIfNameExists(){
            Publisher publisher = new Publisher(name);

            when(publisherRepository.findAllByName(name))
                    .thenReturn(List.of(publisher));

            List<PublisherResponse> response =
                    publisherService.findAllByName(name, PublisherFilter.ALL);

            assertEquals(1, response.size());

            verify(publisherRepository).findAllByName(name);

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
