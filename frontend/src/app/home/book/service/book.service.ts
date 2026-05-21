import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BookRequest } from '../dto/book-request';
import { BookResponse } from '../dto/book-response';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BookService {

  private readonly API_URL = '/api/books'

  constructor(private http: HttpClient){}

  create(publisherId: number, request: BookRequest): Observable<BookResponse>{
    return this.http.post<BookResponse>(`${this.API_URL}/${publisherId}`, {...request});
  }

  findAllByName(name: string): Observable<BookResponse[]>{
    return this.http.get<BookResponse[]>(`${this.API_URL}`, { 
      params: { name }
    });
  }

  findById(id: number): Observable<BookResponse>{
    return this.http.get<BookResponse>(`${this.API_URL}/${id}`)
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
