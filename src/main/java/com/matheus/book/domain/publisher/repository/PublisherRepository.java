package com.matheus.book.domain.publisher.repository;

import com.matheus.book.domain.publisher.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByNameContainingIgnoreCase(String name);
}
