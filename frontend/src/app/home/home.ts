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

type PageContent = { first: boolean | null, last: boolean | null };

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
  searchedBookName = '';
  searchedCustomerName = '';
  searchedPublisherName = '';

  books: BookResponse[] = [];
  customers: CustomerResponse[] = [];
  publishers: PublisherResponse[] = [];

  totalBooks: number = 0;
  totalCustomers: number = 0;
  totalPublishers: number = 0;

  bookPageContent: PageContent = { first: null, last: null }
  customerPageContent: PageContent = { first: null, last: null }
  publisherPageContent: PageContent = { first: null, last: null }

  bookPage: number = 0;
  customerPage: number = 0;
  publisherPage: number = 0; 

  constructor(
    private bookService: BookService,
    private customerService: CustomerService,
    private publisherService: PublisherService,
    private router: Router
  ){}

  ngOnInit(){
    this.findAllInfo();
    this.loadSelectedMenu();
  }

  //menu
  selectMenu(menu: Menu){
    this.selectedMenu = menu;
    this.loadSelectedMenu();
  }

  searchSelectedMenu(){
    if(this.selectedMenu === 'BOOKS'){
      this.searchedBookName = this.bookName.trim();
    }

    if(this.selectedMenu === 'CUSTOMER'){
      this.searchedCustomerName = this.customerName.trim();
    }

    if(this.selectedMenu === 'PUBLISHER'){
      this.searchedPublisherName = this.publisherName.trim();
    }

    this.loadSelectedMenu();
  }

  loadSelectedMenu(){
    if(this.selectedMenu === 'BOOKS'){
      this.bookPage = 0;
      this.findAllBooksByName();
      return;
    }

    if(this.selectedMenu === 'CUSTOMER'){
      this.customerPage = 0;
      this.findAllCustomersByName();
      return;
    }

    this.publisherPage = 0;
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

  changePage(value: number, first: boolean | null, last: boolean | null): number{

    if(first === null || last === null) return 0

    if(first && value < 0) return 0;
    if(last && value > 0) return 0;

    return value;
  }

  changeSelectedPage(value: number) {
    if(this.selectedMenu === 'BOOKS'){
      this.findAllBooksByName(value);
      return;
    }

    if(this.selectedMenu === 'CUSTOMER'){
      this.findAllCustomersByName(value);
      return;
    }

    this.findAllPublishersByName(value);
  }

  selectedPageContent(): PageContent {
    if(this.selectedMenu === 'BOOKS') return this.bookPageContent;
    if(this.selectedMenu === 'CUSTOMER') return this.customerPageContent;
    return this.publisherPageContent;
  }

  findAllInfo(){
    this.findBookInfo();
    this.findCustomerInfo();
    this.findPublisherInfo();
  }

  selectBookFilter(filter: BookFilter){
    this.selectedBookFilter = filter;
    this.bookPage = 0;
    this.findAllBooksByName();
  }

  selectCustomerPublisherFilter(filter: CustomerPublisherFilter){
    this.selectedCustomerPublisherFilter = filter;

    if(this.selectedMenu === 'CUSTOMER'){
      this.customerPage = 0;
      this.findAllCustomersByName();
      return;
    }

    this.publisherPage = 0;
    this.findAllPublishersByName();
  }

  //book
  findBookInfo(){
    this.bookService.info().subscribe({
      next: (response) => {
        this.totalBooks = response;
      },
      error: () => {
        console.log("error: bookInfo")
      }
    });
  }

  findAllBooksByName(value: number = 0){
    const bookName = this.searchedBookName;

    this.bookPage +=
      this.changePage(value, this.bookPageContent.first, this.bookPageContent.last);

    this.bookService.findAllByName(bookName, this.selectedBookFilter, this.bookPage).subscribe({
      next: (response) => {
        const first = response.first
        const last = response.last
        
        this.bookPageContent = { first, last }
        this.books = response.content;
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
        this.findBookInfo();
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
  findCustomerInfo(){
    this.customerService.info().subscribe({
      next: (response) => {
        this.totalCustomers = response;
      },
      error: () => {
        console.log("error: customerInfo")
      }
    });
  }

  findAllCustomersByName(value: number = 0){

    const customerName = this.searchedCustomerName;

    this.customerPage +=
      this.changePage(value, this.customerPageContent.first, this.customerPageContent.last);

    this.customerService.findAllByName(
        customerName, this.selectedCustomerPublisherFilter, this.customerPage).subscribe({

      next: (response) => {
        const first = response.first
        const last = response.last

        this.customerPageContent = { first, last }
        this.customers = response.content;
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
        this.findCustomerInfo();
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
  findPublisherInfo(){
    this.publisherService.info().subscribe({
      next: (response) => {
        this.totalPublishers = response;
      },
      error: () => {
        console.log("error: publisherInfo")
      }
    });
  }

  findAllPublishersByName(value: number = 0){
    const publisherName = this.searchedPublisherName;

    this.publisherPage += this.changePage(value, this.publisherPageContent.first, this.publisherPageContent.last);

    this.publisherService.findAllByName(
      publisherName, this.selectedCustomerPublisherFilter, this.publisherPage).subscribe({

      next: (response) => {
        const first = response.first
        const last = response.last
        
        this.publisherPageContent = { first, last }
        this.publishers = response.content;

        
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
        this.findPublisherInfo();
      },
      error: () => {
        console.log("error: deletePublisher")
      }
    });
  }

}
