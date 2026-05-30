import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CustomerRequest } from '../dto/request/customer-request';
import { CustomerResponse } from '../dto/response/customer-response';
import { Observable } from 'rxjs';
import { PageResponse } from '../../page/page-response';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private readonly API_URL = '/api/customers'

  constructor(private http: HttpClient){}

  create(request: CustomerRequest): Observable<CustomerResponse>{
    return this.http.post<CustomerResponse>(`${this.API_URL}`, {...request});
  }

  findAllByName(name: string, filter: string, page: number): Observable<PageResponse<CustomerResponse>>{
    return this.http.get<PageResponse<CustomerResponse>>(`${this.API_URL}`, { 
      params: { name, filter, page}
    });
  }

  findById(id: number): Observable<CustomerResponse>{
    return this.http.get<CustomerResponse>(`${this.API_URL}/${id}`)
  }

  info(): Observable<number>{
    return this.http.get<number>(`${this.API_URL}/info`);
  }

  editInfo(id: number, name: string): Observable<void>{
    return this.http.patch<void>(`${this.API_URL}/${id}`, { name })
  }

  delete(id: number): Observable<void>{
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
