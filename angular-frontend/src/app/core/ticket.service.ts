import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError } from 'rxjs';
import { TicketDto } from '../models/ticket';
import { CustomResponse } from '../models/responses/CustomResponse';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  private readonly endpoint: string = 'http://localhost:8081/main';

  constructor(private readonly httpClient: HttpClient) {}

  getTickets(id: number): Observable<CustomResponse<TicketDto[]>> {
    const finalEndpoint = `${this.endpoint}/${id}/tickets`;
    return this.httpClient.get<CustomResponse<TicketDto[]>>(finalEndpoint);
  }

  addTicket(ticket: TicketDto): Observable<CustomResponse<TicketDto>>{
    const finalEndpoint = `${this.endpoint}/ticket`;
    return this.httpClient.post<CustomResponse<TicketDto>>(finalEndpoint, ticket);
  }
  deleteTicket(id: number) {
    const finalEndpoint = `${this.endpoint}/ticket/${id}`;
    this.httpClient.delete(finalEndpoint).subscribe();
  }
}