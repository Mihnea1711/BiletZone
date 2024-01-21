import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegistrationData } from '../models/user';
import { GATEWAY_REGISTER_URL } from '../utils/endpoints';
import { CustomResponse } from '../models/responses/CustomResponse';

interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  password: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private apiUrl = GATEWAY_REGISTER_URL;

  constructor(private http: HttpClient) {}

  registerUser(userData: RegistrationData): Observable<CustomResponse<any>> {
    const headers = new HttpHeaders({
      // Add headers if needed, e.g., 'Content-Type': 'application/json'
    });

    return this.http.post<CustomResponse<any>>(this.apiUrl, userData, { headers })
  }
}
