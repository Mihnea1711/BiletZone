import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomResponse } from '../models/responses/CustomResponse';
import { GATEWAY_ADD_MESSAGE, GATEWAY_GET_EVENT_MESSAGES  } from '../utils/endpoints'; 
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { JWT_COOKIE_NAME } from '../utils/constants'
import { LOGIN_ENDPOINT } from '../utils/endpoints';
import { ToastService } from './toastService';

export interface Message{
  id: number;
  userUUID: string;
  messageText: string;
  }

@Injectable({
  providedIn: 'root'
})
export class ForumService {

  constructor(private http: HttpClient,
        private toastr: ToastService,
        private cookieService: CookieService,
        private router: Router) {}

  private getJwtFromCookies(): string | null {
    return this.cookieService.get(JWT_COOKIE_NAME);
}

private redirectToLogin(): void {
    this.router.navigate([LOGIN_ENDPOINT]);
    this.toastr.showError('Please log in to access this feature.');
}

  getMessagesForEvent(eventId: string): Observable<CustomResponse<Message[]>> {
    const url = GATEWAY_GET_EVENT_MESSAGES(eventId);

    const jwt = this.getJwtFromCookies();
    if(!jwt)
    {
      this.redirectToLogin();
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${jwt}`
    });
    return this.http.get<CustomResponse<Message[]>>(url, {headers});

  }
  
  private extractSubFromToken(token: string): string {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const decodedToken = JSON.parse(atob(base64));

    return decodedToken ? decodedToken.sub : null;
  }

  addMessageForEvent(eventId: string, message: string): Observable<CustomResponse<any>> {
    const url = GATEWAY_ADD_MESSAGE(eventId);

    const jwt = this.getJwtFromCookies();
    let sub: string | null = null; 
    if(!jwt)
    {
      this.redirectToLogin();
    }
    else
    {
      sub = this.extractSubFromToken(jwt);
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${jwt}`
    });

    const body = { userUUID: sub, messageText: message }; // Adjust the body based on your backend requirements
    return this.http.post<CustomResponse<any>>(url, body, {headers});
  }
}
