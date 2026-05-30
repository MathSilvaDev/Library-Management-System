import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { BookResponse } from '../../api/book/dto/response/book-response';
import { BookService } from '../../api/book/service/book.service';
import { CustomerResponse } from '../../api/customer/dto/response/customer-response';
import { CustomerService } from '../../api/customer/service/customer.service';

@Component({
  selector: 'app-book-detail',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './book-detail.html',
  styleUrl: './book-detail.scss',
})
export class BookDetail {
  id!: number;
  book: BookResponse | null = null;
  customers: CustomerResponse[] = [];
  customerName = '';
  showBorrowCustomers = false;
  loading = true;
  isLoadingCustomers = false;
  actionCustomerId: number | null = null;
  message = '';
  error = '';

  get borrowedCustomers() {
    return this.book?.customerSimpleResponses ?? [];
  }

  get availableCopies(): number {
    if (!this.book) return 0;

    return Math.max(this.book.quantity - this.book.borrowedQuantity, 0);
  }

  get canBorrow(): boolean {
    return this.availableCopies > 0;
  }

  constructor(
    private route: ActivatedRoute,
    private bookService: BookService,
    private customerService: CustomerService
  ) {}

  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.findBook();
  }

  findBook() {
    this.loading = true;
    this.error = '';

    this.bookService.findById(this.id).subscribe({
      next: (response) => {
        this.book = response;
        this.loading = false;
      },
      error: () => {
        this.error = 'Could not load book.';
        this.loading = false;
      },
    });
  }

  openBorrowCustomers() {
    this.showBorrowCustomers = true;

    if (!this.customers.length) {
      this.findCustomers();
    }
  }

  findCustomers() {
    this.isLoadingCustomers = true;

    this.customerService.findAllByName(this.customerName.trim(), 'ALL').subscribe({
      next: (response) => {
        this.customers = response;
        this.isLoadingCustomers = false;
      },
      error: () => {
        this.error = 'Could not load customers.';
        this.isLoadingCustomers = false;
      },
    });
  }

  borrowBook(customerId: number) {
    if (!this.canBorrow) return;

    this.actionCustomerId = customerId;
    this.message = '';
    this.error = '';

    this.bookService.borrowBook(this.id, customerId).subscribe({
      next: () => {
        this.message = 'Book borrowed.';
        this.actionCustomerId = null;
        this.findBook();
      },
      error: () => {
        this.error = 'Could not borrow this book.';
        this.actionCustomerId = null;
      },
    });
  }

  returnBook(customerId: number) {
    this.actionCustomerId = customerId;
    this.message = '';
    this.error = '';

    this.bookService.returnBook(this.id, customerId).subscribe({
      next: () => {
        this.message = 'Book returned.';
        this.actionCustomerId = null;
        this.findBook();
      },
      error: () => {
        this.error = 'Could not return this book.';
        this.actionCustomerId = null;
      },
    });
  }

  formatDate(value: Date | string): string {
    return String(value).slice(0, 10);
  }
}
