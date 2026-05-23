package com.matheus.book.domain.customer.repository;

import com.matheus.book.domain.customer.entity.Customer;
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
    List<Customer> findAllByName(String name);

    @Query("""
        SELECT c FROM Customer c
        WHERE (:name IS NULL
            OR :name = ''
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND SIZE(c.books) > 0
    """)
    List<Customer> findWithBorrowedBooksByName(String name);

    @Query("""
        SELECT c FROM Customer c
        WHERE (:name IS NULL
            OR :name = ''
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND SIZE(c.books) = 0
    """)
    List<Customer> findWithoutBorrowedBooksByName(String name);
}
