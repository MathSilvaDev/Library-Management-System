import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { BookResponse } from '../../api/book/dto/response/book-response';
import { BookService } from '../../api/book/service/book.service';
import { CustomerResponse } from '../../api/customer/dto/response/customer-response';
import { CustomerService } from '../../api/customer/service/customer.service';

type PageContent = { first: boolean | null; last: boolean | null };

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
  borrowedCustomerName = '';
  searchedCustomerName = '';
  searchedBorrowedCustomerName = '';
  showBorrowCustomers = false;
  loading = true;
  isLoadingCustomers = false;
  actionCustomerId: number | null = null;
  message = '';
  error = '';
  customerPage: number = 0;
  borrowedCustomersPage: number = 0;
  readonly borrowedCustomersPageSize: number = 20;
  customerPageContent: PageContent = { first: null, last: null };

  get borrowedCustomers() {
    return this.book?.customerSimpleResponses ?? [];
  }

  get filteredBorrowedCustomers() {
    const search = this.searchedBorrowedCustomerName.toLowerCase();

    if (!search) return this.borrowedCustomers;

    return this.borrowedCustomers.filter((customer) =>
      customer.name.toLowerCase().includes(search)
    );
  }

  get paginatedBorrowedCustomers() {
    const start = this.borrowedCustomersPage * this.borrowedCustomersPageSize;

    return this.filteredBorrowedCustomers.slice(start, start + this.borrowedCustomersPageSize);
  }

  get borrowedCustomersPageContent(): PageContent {
    return {
      first: this.borrowedCustomersPage === 0,
      last: this.borrowedCustomersPage >= this.lastBorrowedCustomersPage,
    };
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
        this.normalizeBorrowedCustomersPage();
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

  searchCustomers() {
    this.searchedCustomerName = this.customerName.trim();
    this.customerPage = 0;
    this.findCustomers();
  }

  findCustomers(value: number = 0) {
    this.isLoadingCustomers = true;
    this.customerPage += this.changePage(value, this.customerPageContent.first, this.customerPageContent.last);

    this.customerService.findAllByName(this.searchedCustomerName, 'ALL', this.customerPage).subscribe({
      next: (response) => {
        this.customerPageContent = { first: response.first, last: response.last };
        this.customers = response.content;
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

  searchBorrowedCustomers() {
    this.searchedBorrowedCustomerName = this.borrowedCustomerName.trim();
    this.borrowedCustomersPage = 0;
  }

  changeBorrowedCustomersPage(value: number) {
    this.borrowedCustomersPage += this.changePage(
      value,
      this.borrowedCustomersPageContent.first,
      this.borrowedCustomersPageContent.last
    );
  }

  private normalizeBorrowedCustomersPage() {
    this.borrowedCustomersPage = Math.min(this.borrowedCustomersPage, this.lastBorrowedCustomersPage);
  }

  private get lastBorrowedCustomersPage(): number {
    return Math.max(Math.ceil(this.filteredBorrowedCustomers.length / this.borrowedCustomersPageSize) - 1, 0);
  }

  private changePage(value: number, first: boolean | null, last: boolean | null): number {
    if (first === null || last === null) return 0;
    if (first && value < 0) return 0;
    if (last && value > 0) return 0;

    return value;
  }
}
