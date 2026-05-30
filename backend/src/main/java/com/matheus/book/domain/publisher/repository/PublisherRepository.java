package com.matheus.book.domain.publisher.repository;

import com.matheus.book.domain.publisher.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    @Query("""
        SELECT p FROM Publisher p
        WHERE (:name IS NULL
            OR :name = ''
            OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    Page<Publisher> findAllByName(String name, Pageable pageable);

    @Query("""
        SELECT p FROM Publisher p
        WHERE (:name IS NULL
            OR :name = ''
            OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND EXISTS (
            SELECT b FROM Book b
            WHERE b.publisher = p
            AND SIZE(b.customers) > 0
        )
    """)
    Page<Publisher> findWithBorrowedBooksByName(String name, Pageable pageable);

    @Query("""
        SELECT p FROM Publisher p
        WHERE (:name IS NULL
            OR :name = ''
            OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND NOT EXISTS (
            SELECT b FROM Book b
            WHERE b.publisher = p
            AND SIZE(b.customers) > 0
        )
    """)
    Page<Publisher> findWithoutBorrowedBooksByName(String name, Pageable pageable);
}
