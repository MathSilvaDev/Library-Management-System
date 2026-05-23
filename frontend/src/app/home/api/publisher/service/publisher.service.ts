import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PublisherRequest } from '../dto/publisher-request';
import { Observable } from 'rxjs';
import { PublisherResponse } from '../dto/publisher-response';

@Injectable({
  providedIn: 'root',
})
export class PublisherService {
  
  private readonly API_URL = '/api/publishers'

  constructor(private http: HttpClient){}

  create(request: PublisherRequest): Observable<PublisherResponse>{
    return this.http.post<PublisherResponse>(`${this.API_URL}`, {...request});
  }

  findAllByName(name: string, filter: string): Observable<PublisherResponse[]>{
    return this.http.get<PublisherResponse[]>(`${this.API_URL}`, { 
      params:{ name, filter }
    });
  }

  delete(id: number): Observable<void>{
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

}
