import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PublisherService } from '../../api/publisher/service/publisher.service';
import { PublisherRequest } from '../../api/publisher/dto/publisher-request';

@Component({
  selector: 'app-publisher-create',
  imports: [FormsModule],
  templateUrl: './publisher-create.html',
  styleUrl: './publisher-create.scss',
})
export class PublisherCreate {
  name = '';
  isSaving = false;
  message = '';
  error = '';

  constructor(
    private publisherService: PublisherService,
    private router: Router
  ) {}

  canCreate(): boolean {
    return Boolean(this.name.trim());
  }

  create() {
    if (!this.canCreate()) return;

    this.isSaving = true;
    this.message = '';
    this.error = '';

    this.publisherService.create({ name: this.name.trim() }).subscribe({
      next: () => {
        this.message = 'Publisher created.';
        this.router.navigate(['/']);
      },
      error: () => {
        this.error = 'Could not create publisher.';
        this.isSaving = false;
      },
    });
  }

  cancel() {
    this.router.navigate(['/']);
  }
}
