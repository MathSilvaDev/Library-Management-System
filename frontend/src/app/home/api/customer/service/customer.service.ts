import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CustomerRequest } from '../dto/customer-request';
import { CustomerResponse } from '../dto/customer-response';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private readonly API_URL = '/api/customers'

  constructor(private http: HttpClient){}

  create(request: CustomerRequest): Observable<CustomerResponse>{
    return this.http.post<CustomerResponse>(`${this.API_URL}`, {...request});
  }

  findAllByName(name: string, filter: string): Observable<CustomerResponse[]>{
    return this.http.get<CustomerResponse[]>(`${this.API_URL}`, { 
      params: { name, filter}
    });
  }

  findById(id: number): Observable<CustomerResponse>{
    return this.http.get<CustomerResponse>(`${this.API_URL}/${id}`)
  }

  delete(id: number): Observable<void>{
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
