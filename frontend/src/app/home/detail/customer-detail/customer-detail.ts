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
  borrowedBookName = '';
  searchedBookName = '';
  searchedBorrowedBookName = '';
  showBorrowBooks = false;
  loading = true;
  isLoadingBooks = false;
  actionBookId: number | null = null;
  message = '';
  error = '';
  page: number = 0;
  borrowedBooksPage: number = 0;
  readonly borrowedBooksPageSize: number = 20;
  bookPageContent: PageContent = { first: null, last: null };

  get borrowedBooks() {
    return this.customer?.bookSimpleResponses ?? [];
  }

  get filteredBorrowedBooks() {
    const search = this.searchedBorrowedBookName.toLowerCase();

    if (!search) return this.borrowedBooks;

    return this.borrowedBooks.filter((book) =>
      book.name.toLowerCase().includes(search) ||
      book.publisherName.toLowerCase().includes(search)
    );
  }

  get paginatedBorrowedBooks() {
    const start = this.borrowedBooksPage * this.borrowedBooksPageSize;

    return this.filteredBorrowedBooks.slice(start, start + this.borrowedBooksPageSize);
  }

  get borrowedBooksPageContent(): PageContent {
    return {
      first: this.borrowedBooksPage === 0,
      last: this.borrowedBooksPage >= this.lastBorrowedBooksPage,
    };
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
        this.normalizeBorrowedBooksPage();
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

  searchBooks() {
    this.searchedBookName = this.bookName.trim();
    this.page = 0;
    this.findBooks();
  }

  findBooks(value: number = 0) {
    this.isLoadingBooks = true;
    this.page += this.changePage(value, this.bookPageContent.first, this.bookPageContent.last);

    this.bookService.findAllByName(this.searchedBookName, 'ALL', this.page).subscribe({
      next: (response) => {
        this.bookPageContent = { first: response.first, last: response.last };
        this.books = response.content;
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

  searchBorrowedBooks() {
    this.searchedBorrowedBookName = this.borrowedBookName.trim();
    this.borrowedBooksPage = 0;
  }

  changeBorrowedBooksPage(value: number) {
    this.borrowedBooksPage += this.changePage(
      value,
      this.borrowedBooksPageContent.first,
      this.borrowedBooksPageContent.last
    );
  }

  private normalizeBorrowedBooksPage() {
    this.borrowedBooksPage = Math.min(this.borrowedBooksPage, this.lastBorrowedBooksPage);
  }

  private get lastBorrowedBooksPage(): number {
    return Math.max(Math.ceil(this.filteredBorrowedBooks.length / this.borrowedBooksPageSize) - 1, 0);
  }

  private changePage(value: number, first: boolean | null, last: boolean | null): number {
    if (first === null || last === null) return 0;
    if (first && value < 0) return 0;
    if (last && value > 0) return 0;

    return value;
  }
}
