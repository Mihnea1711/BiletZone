import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventDto } from '../models/eventD';
import { CustomResponse } from '../models/responses/CustomResponse';
import { GATEWAY_DELETE_EVENT } from '../utils/endpoints';

@Injectable({
  providedIn: 'root'
})
export class EventServiceService {
  private readonly endpoint: string = 'http://localhost:8081/main/events';

  constructor(private readonly httpClient: HttpClient) {}

  getEvent(id: number): Observable<CustomResponse<EventDto>> {
    const finalEndpoint = `${this.endpoint}/${id}`;
    return this.httpClient.get<CustomResponse<EventDto>>(finalEndpoint);
  }

  deleteEvent(id: number): void {
    //const finalEndpoint = GATEWAY_DELETE_EVENT(id);
    const finalEndpoint = `${this.endpoint}/${id}`;
    this.httpClient.delete(finalEndpoint).subscribe();
  }

  addEvent(eventData: EventDto): Observable<CustomResponse<EventDto>> {
    const finalEndpoint = this.endpoint;
    return this.httpClient.post<CustomResponse<EventDto>>(finalEndpoint, eventData);
  }

  updateEvent(id: number, eventData: EventDto): Observable<CustomResponse<EventDto>> {
    const finalEndpoint = `${this.endpoint}/${id}`;
    return this.httpClient.put<CustomResponse<EventDto>>(finalEndpoint, eventData);
  }
}
