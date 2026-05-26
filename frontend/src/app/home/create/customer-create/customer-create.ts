import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CustomerService } from '../../api/customer/service/customer.service';

@Component({
  selector: 'app-customer-create',
  imports: [FormsModule],
  templateUrl: './customer-create.html',
  styleUrl: './customer-create.scss',
})
export class CustomerCreate {
  name = '';
  isSaving = false;
  message = '';
  error = '';

  constructor(
    private customerService: CustomerService,
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

    this.customerService.create({ name: this.name.trim() }).subscribe({
      next: () => {
        this.message = 'Customer created.';
        this.router.navigate(['/']);
        window.alert("Customer created successfully.");
      },
      error: () => {
        this.error = 'Could not create customer.';
        this.isSaving = false;
      },
    });
  }

  cancel() {
    this.router.navigate(['/']);
  }
}
