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
  // bookName: string = '';
  // bookQuantity: number = 0;
  // publishedIn: Date | null = null;

  books: BookResponse[] = [];
  customers: CustomerResponse[] = [];
  publishers: PublisherResponse[] = [];

  constructor(
    private bookService: BookService,
    private customerService: CustomerService,
    private publisherService: PublisherService
  ){}

  ngOnInit(){
    this.findAllBookByName();
  }

  // createBook(publisherId: number){
  //   const bookName = this.bookName.trim();
  //   const bookQuantity = this.bookQuantity;
  //   const publishedIn = new Date(publisherId);

  //   const bookRequest: BookRequest = {
  //     name: bookName, 
  //     quantity: bookQuantity, 
  //     publishedIn
  //   }

  //   this.bookService.create(publisherId, bookRequest).subscribe({
  //     next: (response) => {
  //       this.books.push(response);
  //     },
  //     error: (err) => {
  //       console.log("error to createBook");
  //     }
  //   });
  // }

  findAllBookByName(){
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

  findAllCustomerByName(){
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

  findAllPublisherByName(){
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

  
}
