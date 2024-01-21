import { Injectable } from '@angular/core';
import { GATEWAY_EDIT_PROFILE_URL, GATEWAY_GET_PROFILE_URL, GATEWAY_GET_PURCHASED_TICKETS_URL } from '../utils/endpoints';
import { Observable } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { CustomResponse } from '../models/responses/CustomResponse';
import { CookieService } from 'ngx-cookie-service';
import { JWT_COOKIE_NAME } from '../utils/constants';
import { ToastService } from './toastService';
import { Router } from '@angular/router';
import { LOGIN_ENDPOINT } from '../utils/endpoints';
import { PurchasedTicketDto } from '../models/purchasedTicket'
import { ProfileDto } from '../models/profile';
@Injectable({
  providedIn: 'root'
})
export class ProfileServiceService {
 
  //private apiUrlEditProfile = GATEWAY_EDIT_PROFILE_URL;
  private apiUrlGetProfile=GATEWAY_GET_PROFILE_URL;
  private apiUrlGetPurchasedTickets = GATEWAY_GET_PURCHASED_TICKETS_URL;
  private apiUrlEditProfile = GATEWAY_EDIT_PROFILE_URL;

  constructor(private http: HttpClient, 
    private cookieService: CookieService,
    private toaster: ToastService,
    private router: Router) { }
   
  getJwtFromCookies(): string | undefined {
    return this.cookieService.get(JWT_COOKIE_NAME);
  }

  private redirectToLogin(): void {
    this.router.navigate([LOGIN_ENDPOINT]);
    this.toaster.showError('Please log in to access this feature.');
}

  getProfile(): Observable<CustomResponse<any>> {
    const jwt = this.getJwtFromCookies();
    if(!jwt)
    {
      this.redirectToLogin();
    }

    const headers = new HttpHeaders({ 
      'Authorization': `Bearer ${jwt}`
    });

    return this.http.get<CustomResponse<any>>(this.apiUrlGetProfile, { headers });
  }

  getPurchasedTickets(): Observable<CustomResponse<PurchasedTicketDto[]>> {
    const jwt = this.getJwtFromCookies();
    if (!jwt) {
      this.redirectToLogin();
    }
  
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${jwt}`
    });
  
    return this.http.get<CustomResponse<PurchasedTicketDto[]>>(this.apiUrlGetPurchasedTickets, { headers });
  }

  editProfile(profileData: any): Observable<CustomResponse<any>> {
    const jwt = this.getJwtFromCookies();
    if (!jwt) {
      this.redirectToLogin();
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${jwt}`,
    });

    return this.http.put<CustomResponse<any>>(this.apiUrlEditProfile, profileData, { headers });
  }
}
