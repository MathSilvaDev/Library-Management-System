import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { BookResponse } from '../../api/book/dto/response/book-response';
import { BookService } from '../../api/book/service/book.service';
import { CustomerResponse } from '../../api/customer/dto/response/customer-response';
import { CustomerService } from '../../api/customer/service/customer.service';

@Component({
  selector: 'app-customer-detail',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './customer-detail.html',
  styleUrl: './customer-detail.scss',
})
export class CustomerDetail {
  id!: number;
  customer: CustomerResponse | null = null;
  books: BookResponse[] = [];
  bookName = '';
  showBorrowBooks = false;
  loading = true;
  isLoadingBooks = false;
  actionBookId: number | null = null;
  message = '';
  error = '';

  get borrowedBooks() {
    return this.customer?.bookSimpleResponses ?? [];
  }

  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService,
    private bookService: BookService
  ) {}

  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.findCustomer();
  }

  findCustomer() {
    this.loading = true;
    this.error = '';

    this.customerService.findById(this.id).subscribe({
      next: (response) => {
        this.customer = response;
        this.loading = false;
      },
      error: () => {
        this.error = 'Could not load customer.';
        this.loading = false;
      },
    });
  }

  openBorrowBooks() {
    this.showBorrowBooks = true;

    if (!this.books.length) {
      this.findBooks();
    }
  }

  findBooks() {
    this.isLoadingBooks = true;

    this.bookService.findAllByName(this.bookName.trim(), 'ALL').subscribe({
      next: (response) => {
        this.books = response;
        this.isLoadingBooks = false;
      },
      error: () => {
        this.error = 'Could not load books.';
        this.isLoadingBooks = false;
      },
    });
  }

  availableCopies(book: BookResponse): number {
    return Math.max(book.quantity - book.borrowedQuantity, 0);
  }

  isBookAvailable(book: BookResponse): boolean {
    return this.availableCopies(book) > 0;
  }

  borrowBook(book: BookResponse) {
    if (!this.isBookAvailable(book)) return;

    this.actionBookId = book.id;
    this.message = '';
    this.error = '';

    this.bookService.borrowBook(book.id, this.id).subscribe({
      next: () => {
        this.message = 'Book borrowed.';
        this.actionBookId = null;
        this.findCustomer();
        this.findBooks();
      },
      error: () => {
        this.error = 'Could not borrow this book.';
        this.actionBookId = null;
      },
    });
  }

  returnBook(bookId: number) {
    this.actionBookId = bookId;
    this.message = '';
    this.error = '';

    this.bookService.returnBook(bookId, this.id).subscribe({
      next: () => {
        this.message = 'Book returned.';
        this.actionBookId = null;
        this.findCustomer();
        this.findBooks();
      },
      error: () => {
        this.error = 'Could not return this book.';
        this.actionBookId = null;
      },
    });
  }
}
