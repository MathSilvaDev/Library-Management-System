import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CreateBookRequest } from '../../api/book/dto/request/create-book-request';
import { BookService } from '../../api/book/service/book.service';
import { PublisherResponse } from '../../api/publisher/dto/publisher-response';
import { PublisherService } from '../../api/publisher/service/publisher.service';

type PageContent = { first: boolean | null; last: boolean | null };

@Component({
  selector: 'app-book-create',
  imports: [CommonModule, FormsModule],
  templateUrl: './book-create.html',
  styleUrl: './book-create.scss',
})
export class BookCreate {
  name = '';
  quantity: number | null = null;
  publishedIn = '';
  publisherName = '';
  searchedPublisherName = '';
  publishers: PublisherResponse[] = [];
  selectedPublisher: PublisherResponse | null = null;
  isSaving = false;
  message = '';
  error = '';
  publisherPage: number = 0;
  publisherPageContent: PageContent = { first: null, last: null };

  get selectablePublishers(): PublisherResponse[] {
    if (!this.selectedPublisher) {
      return this.publishers;
    }

    return this.publishers.filter((publisher) => publisher.id !== this.selectedPublisher?.id);
  }

  constructor(
    private bookService: BookService,
    private publisherService: PublisherService,
    private router: Router
  ) {}

  ngOnInit() {
    this.findPublishers();
  }

  searchPublishers() {
    this.searchedPublisherName = this.publisherName.trim();
    this.publisherPage = 0;
    this.findPublishers();
  }

  findPublishers(value: number = 0) {
    this.publisherPage += this.changePage(value, this.publisherPageContent.first, this.publisherPageContent.last);

    this.publisherService.findAllByName(this.searchedPublisherName, 'ALL', this.publisherPage).subscribe({
      next: (response) => {
        this.publisherPageContent = { first: response.first, last: response.last };
        this.publishers = response.content;
        this.showSelectedPublisherFirst();
      },
      error: () => {
        this.error = 'Could not load publishers.';
      },
    });
  }

  selectPublisher(publisher: PublisherResponse) {
    this.selectedPublisher = publisher;
    this.showSelectedPublisherFirst();
  }

  canCreate(): boolean {
    return Boolean(
      this.name.trim() && this.quantity && this.quantity > 0 && this.publishedIn && this.selectedPublisher
    );
  }

  create() {
    if (!this.canCreate() || !this.selectedPublisher || !this.quantity) return;

    const request: CreateBookRequest = {
      name: this.name.trim(),
      quantity: this.quantity,
      publishedIn: this.publishedIn as unknown as Date,
    };

    this.isSaving = true;
    this.message = '';
    this.error = '';

    this.bookService.create(this.selectedPublisher.id, request).subscribe({
      next: () => {
        this.message = 'Book created.';
        this.router.navigate(['/']);
        window.alert("Book created successfully.");
      },
      error: () => {
        this.error = 'Could not create book.';
        this.isSaving = false;
      },
    });
  }

  cancel() {
    this.router.navigate(['/']);
  }

  private changePage(value: number, first: boolean | null, last: boolean | null): number {
    if (first === null || last === null) return 0;
    if (first && value < 0) return 0;
    if (last && value > 0) return 0;

    return value;
  }

  private showSelectedPublisherFirst() {
    if (this.selectedPublisher) {
      this.publishers = [
        this.selectedPublisher,
        ...this.publishers.filter((publisher) => publisher.id !== this.selectedPublisher?.id)
      ];
    }
  }
}
