package com.matheus.book.domain.customer.entity;

import com.matheus.book.domain.book.entity.Book;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "customers")
    private List<Book> books = new ArrayList<>();

    public Customer(String name){
        this.name = name;
    }

    public void edit(String name){
        this.name = name;
    }
}
