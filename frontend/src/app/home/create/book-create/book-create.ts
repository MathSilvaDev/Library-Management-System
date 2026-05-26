import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CreateBookRequest } from '../../api/book/dto/request/create-book-request';
import { BookService } from '../../api/book/service/book.service';
import { PublisherResponse } from '../../api/publisher/dto/publisher-response';
import { PublisherService } from '../../api/publisher/service/publisher.service';

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
  publishers: PublisherResponse[] = [];
  selectedPublisher: PublisherResponse | null = null;
  isSaving = false;
  message = '';
  error = '';

  constructor(
    private bookService: BookService,
    private publisherService: PublisherService,
    private router: Router
  ) {}

  ngOnInit() {
    this.findPublishers();
  }

  findPublishers() {
    this.publisherService.findAllByName(this.publisherName.trim(), 'ALL').subscribe({
      next: (response) => {
        this.publishers = response;
      },
      error: () => {
        this.error = 'Could not load publishers.';
      },
    });
  }

  selectPublisher(publisher: PublisherResponse) {
    this.selectedPublisher = publisher;
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
}
