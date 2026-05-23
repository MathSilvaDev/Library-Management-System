import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BookService } from './api/book/service/book.service';
import { CustomerService } from './api/customer/service/customer.service';
import { PublisherService } from './api/publisher/service/publisher.service';
import { BookRequest } from './api/book/dto/book-request';
import { BookResponse } from './api/book/dto/book-response';
import { CustomerResponse } from './api/customer/dto/customer-response';
import { PublisherResponse } from './api/publisher/dto/publisher-response';

@Component({
  selector: 'app-home',
  imports: [ FormsModule, CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {

  bookName = '';
  customerName = '';
  publisherName = '';

  books: BookResponse[] = [];
  customers: CustomerResponse[] = [];
  publishers: PublisherResponse[] = [];

  constructor(
    private bookService: BookService,
    private customerService: CustomerService,
    private publisherService: PublisherService
  ){}

  ngOnInit(){
    this.findAllBooksByName();
  }

  //book
  findAllBooksByName(){
    const bookName = this.bookName.trim();

    this.bookService.findAllByName(bookName).subscribe({
      next: (response) => {
        this.books = response;
      },
      error: () => {
        console.log("error: findAllBookByName")
      }
    });
  }

  deleteBook(id: number){
    this.bookService.delete(id).subscribe({
      next: () => {
        this.books = this.books.filter((book) => 
          book.id !== id
        );
      },
      error: () => {
        console.log("error: deleteBook")
      }
    });
  }

  availableBook(max: number, rest: number): boolean{
    const value = max - rest;
    return value > 0
  }

  //customer
  findAllCustomersByName(){
    const customerName = this.customerName.trim();

    this.customerService.findAllByName(customerName).subscribe({
      next: (response) => {
        this.customers = response;
      },
      error: () => {
        console.log("error: findAllCustomerByName");
      }
    });
  }

  deleteCustomer(id: number){
    this.customerService.delete(id).subscribe({
      next: () => {
        this.customers = this.customers.filter((customer) => 
          customer.id !== id
        );
      },
      error: () => {
        console.log("error: deleteCustomer")
      }
    });
  }

  //publisher
  findAllPublishersByName(){
    const publisherName = this.publisherName.trim();

    this.publisherService.findAllByName(publisherName).subscribe({
      next: (response) => {
        this.publishers = response;
      },
      error: () => {
        console.log("error: findAllPublisherByName");
      }
    });
  }

  deletePublisher(id: number){
    this.publisherService.delete(id).subscribe({
      next: () => {
        this.publishers = this.publishers.filter((publisher) => 
          publisher.id !== id
        );
      },
      error: () => {
        console.log("error: deletePublisher")
      }
    });
  }
  
}
