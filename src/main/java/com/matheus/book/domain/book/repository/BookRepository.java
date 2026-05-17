package com.matheus.book.domain.book.repository;

import com.matheus.book.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByNameContainingIgnoreCase(String name);

    Optional<Book> findByIdAndCustomers_Id(Long id, Long customerId);


}
