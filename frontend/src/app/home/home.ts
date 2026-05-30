import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { BookService } from './api/book/service/book.service';
import { CustomerService } from './api/customer/service/customer.service';
import { PublisherService } from './api/publisher/service/publisher.service';
import { BookResponse } from './api/book/dto/response/book-response';
import { CustomerResponse } from './api/customer/dto/response/customer-response';
import { PublisherResponse } from './api/publisher/dto/publisher-response';

type Menu = 'BOOKS' | 'CUSTOMER' | 'PUBLISHER';
type BookFilter = 'ALL' | 'AVAILABLE' | 'UNAVAILABLE';
type CustomerPublisherFilter = 'ALL' | 'WITHOUT_BORROWED_BOOKS' | 'WITH_BORROWED_BOOKS';

@Component({
  selector: 'app-home',
  imports: [ FormsModule, CommonModule, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {

  selectedBookFilter: BookFilter = 'ALL';
  selectedCustomerPublisherFilter: CustomerPublisherFilter = 'ALL';
  selectedMenu: Menu = 'BOOKS';

  bookName = '';
  customerName = '';
  publisherName = '';

  books: BookResponse[] = [];
  customers: CustomerResponse[] = [];
  publishers: PublisherResponse[] = [];

  constructor(
    private bookService: BookService,
    private customerService: CustomerService,
    private publisherService: PublisherService,
    private router: Router
  ){}

  ngOnInit(){
    this.findAllBooksByName();
  }

  //menu
  selectMenu(menu: Menu){
    this.selectedMenu = menu;
    this.searchSelectedMenu();
  }

  searchSelectedMenu(){
    if(this.selectedMenu === 'BOOKS'){
      this.findAllBooksByName();
      return;
    }

    if(this.selectedMenu === 'CUSTOMER'){
      this.findAllCustomersByName();
      return;
    }

    this.findAllPublishersByName();
  }

  get createLabel(): string {
    if(this.selectedMenu === 'BOOKS'){
      return 'New Book';
    }

    if(this.selectedMenu === 'CUSTOMER'){
      return 'New Customer';
    }

    return 'New Publisher';
  }

  createSelectedMenu(){
    this.router.navigate([this.createRoute()]);
  }

  createRoute(): string {
    if(this.selectedMenu === 'BOOKS'){
      return '/books/create';
    }

    if(this.selectedMenu === 'CUSTOMER'){
      return '/customers/create';
    }

    return '/publishers/create';
  }

  selectBookFilter(filter: BookFilter){
    this.selectedBookFilter = filter;
    this.findAllBooksByName();
  }

  selectCustomerPublisherFilter(filter: CustomerPublisherFilter){
    this.selectedCustomerPublisherFilter = filter;

    if(this.selectedMenu === 'CUSTOMER'){
      this.findAllCustomersByName();
      return;
    }

    this.findAllPublishersByName();
  }

  //book
  findAllBooksByName(){
    const bookName = this.bookName.trim();

    this.bookService.findAllByName(bookName, this.selectedBookFilter).subscribe({
      next: (response) => {
        this.books = response;
      },
      error: () => {
        console.log("error: findAllBookByName")
      }
    });
  }

  deleteBook(id: number){

    if(!window.confirm("Delete this Book?")) return;

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

  openBook(id: number) {
    this.router.navigate(['/books', id]);
  }

  //customer
  findAllCustomersByName(){
    const customerName = this.customerName.trim();

    this.customerService.findAllByName(customerName, this.selectedCustomerPublisherFilter).subscribe({
      next: (response) => {
        this.customers = response;
      },
      error: () => {
        console.log("error: findAllCustomerByName");
      }
    });
  }

  deleteCustomer(id: number){

    if(!window.confirm("Delete this Customer?")) return;

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

  openCustomer(id: number) {
    this.router.navigate(['/customers', id]);
  }

  //publisher
  findAllPublishersByName(){
    const publisherName = this.publisherName.trim();

    this.publisherService.findAllByName(publisherName, this.selectedCustomerPublisherFilter).subscribe({
      next: (response) => {
        this.publishers = response;
      },
      error: () => {
        console.log("error: findAllPublisherByName");
      }
    });
  }

  deletePublisher(id: number){

    if(!window.confirm("Delete this Publisher?")) return;

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
