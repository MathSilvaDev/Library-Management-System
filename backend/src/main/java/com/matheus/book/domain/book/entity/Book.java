package com.matheus.book.domain.book.entity;

import com.matheus.book.domain.customer.entity.Customer;
import com.matheus.book.domain.publisher.entity.Publisher;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    private LocalDate publishedIn;

    private Integer quantity;

    @ManyToMany
    @JoinTable(
            name = "tb_book_customer",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private List<Customer> customers = new ArrayList<>();


    public Book(String name, Publisher publisher, LocalDate publishedIn, Integer quantity){
        this.name = name;
        this.publisher = publisher;
        this.publishedIn = publishedIn;
        setQuantity(quantity);
    }

    public void borrowBook(Customer customer){
        if(!isAvailable()){
            throw new IllegalStateException("The book isn't available");
        }
        customers.add(customer);

        quantity--;
    }

    public void returnBook(Customer customer){
        if(!customers.contains(customer)){
            throw new IllegalStateException("This customer doesn't have this book");
        }

        customers.remove(customer);

        quantity++;
    }

    private boolean isAvailable(){
        return quantity > 0;
    }

    private void setQuantity(Integer quantity){
        this.quantity = quantity == null ? 0 : quantity;
    }
}
