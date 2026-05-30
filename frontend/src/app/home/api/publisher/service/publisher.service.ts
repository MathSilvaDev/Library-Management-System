import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PublisherRequest } from '../dto/publisher-request';
import { Observable } from 'rxjs';
import { PublisherResponse } from '../dto/publisher-response';
import { PageResponse } from '../../page/page-response';

@Injectable({
  providedIn: 'root',
})
export class PublisherService {
  
  private readonly API_URL = '/api/publishers'

  constructor(private http: HttpClient){}

  create(request: PublisherRequest): Observable<PublisherResponse>{
    return this.http.post<PublisherResponse>(`${this.API_URL}`, {...request});
  }

  findAllByName(name: string, filter: string, page: number): Observable<PageResponse<PublisherResponse>>{
    return this.http.get<PageResponse<PublisherResponse>>(`${this.API_URL}`, { 
      params:{ name, filter, page }
    });
  }

  findById(id: number): Observable<PublisherResponse>{
    return this.http.get<PublisherResponse>(`${this.API_URL}/${id}`)
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
