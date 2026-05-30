import { Routes } from '@angular/router';
import { Home } from './home/home';
import { BookCreate } from './home/create/book-create/book-create';
import { BookEdit } from './home/edit/book-edit/book-edit';
import { CustomerCreate } from './home/create/customer-create/customer-create';
import { CustomerEdit } from './home/edit/customer-edit/customer-edit';
import { PublisherCreate } from './home/create/publisher-create/publisher-create';
import { PublisherEdit } from './home/edit/publisher-edit/publisher-edit';
import { BookDetail } from './home/detail/book-detail/book-detail';
import { CustomerDetail } from './home/detail/customer-detail/customer-detail';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'books/create', component: BookCreate },
  { path: 'books/:id/edit', component: BookEdit },
  { path: 'books/:id', component: BookDetail },
  { path: 'customers/create', component: CustomerCreate },
  { path: 'customers/:id/edit', component: CustomerEdit },
  { path: 'customers/:id', component: CustomerDetail },
  { path: 'publishers/create', component: PublisherCreate },
  { path: 'publishers/:id/edit', component: PublisherEdit },
  { path: '**', component: Home },
];
