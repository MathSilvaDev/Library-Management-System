import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateBookRequest } from '../dto/request/create-book-request';
import { BookResponse } from '../dto/response/book-response';
import { Observable } from 'rxjs';
import { EditBookRequest } from '../dto/request/edit-book-request';

@Injectable({
  providedIn: 'root',
})
export class BookService {

  private readonly API_URL = '/api/books'

  constructor(private http: HttpClient){}

  create(publisherId: number, request: CreateBookRequest): Observable<BookResponse>{
    return this.http.post<BookResponse>(`${this.API_URL}/${publisherId}`, {...request});
  }

  findAllByName(name: string, filter: string): Observable<BookResponse[]>{
    return this.http.get<BookResponse[]>(`${this.API_URL}`, { 
      params: { name, filter }
    });
  }

  findById(id: number): Observable<BookResponse>{
    return this.http.get<BookResponse>(`${this.API_URL}/${id}`)
  }

  editInfo(id: number, request: EditBookRequest): Observable<void>{
    return this.http.patch<void>(`${this.API_URL}/${id}`, { ...request })
  }

  delete(id: number): Observable<void>{
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  borrowBook(bookId: number, customerId: number): Observable<void>{
    return this.http.post<void>(`${this.API_URL}/${bookId}/borrow/${customerId}`, {});
  }

  returnBook(bookId: number, customerId: number): Observable<void>{
    return this.http.post<void>(`${this.API_URL}/${bookId}/return/${customerId}`, {});
  }
}
