package com.matheus.book.domain.book.repository;

import com.matheus.book.domain.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIdAndCustomers_Id(Long id, Long customerId);

    @Query("""
        SELECT b FROM Book b
        WHERE (:name IS NULL
            OR :name = ''
            OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    Page<Book> findAllByName(String name, Pageable pageable);

    @Query("""
        SELECT b FROM Book b
        WHERE (:name IS NULL
            OR :name = ''
            OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND b.quantity > SIZE(b.customers)
    """)
    Page<Book> findAvailableByName(String name, Pageable pageable);

    @Query("""
        SELECT b FROM Book b
        WHERE (:name IS NULL
            OR :name = ''
            OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND b.quantity <= SIZE(b.customers)
    """)
    Page<Book> findUnavailableByName(String name, Pageable pageable);

}
