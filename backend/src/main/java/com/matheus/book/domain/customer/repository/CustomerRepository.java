package com.matheus.book.domain.customer.repository;

import com.matheus.book.domain.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("""
        SELECT c FROM Customer c
        WHERE (:name IS NULL
            OR :name = ''
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    Page<Customer> findAllByName(String name, Pageable pageable);

    @Query("""
        SELECT c FROM Customer c
        WHERE (:name IS NULL
            OR :name = ''
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND SIZE(c.books) > 0
    """)
    Page<Customer> findWithBorrowedBooksByName(String name, Pageable pageable);

    @Query("""
        SELECT c FROM Customer c
        WHERE (:name IS NULL
            OR :name = ''
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND SIZE(c.books) = 0
    """)
    Page<Customer> findWithoutBorrowedBooksByName(String name, Pageable pageable);
}
