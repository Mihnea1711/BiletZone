// auth.service.ts
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { LoginData } from '../models/user';
import { Observable } from 'rxjs';
import { CustomResponse } from '../models/responses/CustomResponse';
import { GATEWAY_LOGIN_URL, HOME_ENDPOINT } from '../utils/endpoints';
import { RememberMeService } from './rememberMeService';
import { JWT_COOKIE_ALLOWED_ENDPOINTS, JWT_COOKIE_NAME, JWT_TOKEN_EXPIRATION_DAYS } from '../utils/constants';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
    private tokenKey = JWT_COOKIE_NAME;
    private apiUrl = GATEWAY_LOGIN_URL;

    constructor(
        private http: HttpClient,
        private cookieService: CookieService,
        private rememberMeService: RememberMeService,
        private router: Router
    ) {}

    // Example function to simulate user login
    login(token: string) {
        // Store the token in the cookie
        this.cookieService.set(this.tokenKey, token, JWT_TOKEN_EXPIRATION_DAYS, JWT_COOKIE_ALLOWED_ENDPOINTS);
        return;
    }
    
    loginUser(userData: LoginData): Observable<CustomResponse<any>> {
        const headers = new HttpHeaders({
          // Add headers if needed, e.g., 'Content-Type': 'application/json'
        });
        return this.http.post<CustomResponse<any>>(this.apiUrl, userData, { headers })
    }

    // Example function to simulate user logout
    logout() {
        // Clear the token cookie
        this.cookieService.delete(this.tokenKey);
        // Clear Remember Me data
        this.rememberMeService.clearRememberMeData();
        window.location.reload();
        return;
    }

    // Check if the user is logged in
    isLoggedInUser() {
        // console.log("COOOKIE " + this.cookieService.check(this.tokenKey));      
        
        return this.cookieService.check(this.tokenKey);  
    }

    // Get the token from the cookie
    getToken(): string | undefined {
        return this.cookieService.get(this.tokenKey);
    }
}
