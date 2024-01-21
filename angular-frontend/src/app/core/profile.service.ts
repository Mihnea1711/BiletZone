import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { GATEWAY_GET_ALL_PROFILES, GATEWAY_DELETE_PROFILE, GATEWAY_DELETE_USER} from '../utils/endpoints';
import { ProfileDto } from 'src/app/models/profile';
import { tap } from 'rxjs/operators'; 
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { JWT_COOKIE_NAME } from '../utils/constants'
import { LOGIN_ENDPOINT } from '../utils/endpoints';
import { parseErrorResponse } from '../utils/utils';
import { LoggerService } from './loggerService';
import { HttpStatusHandlerService } from './statusHandlerService';
import { ToastService } from './toastService';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private http: HttpClient,
        private logger: LoggerService,
        private statusHandler: HttpStatusHandlerService,
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

private handleHttpError(error: any, errorMessage: string): Observable<never> {
    this.logger.error(errorMessage, error);
    const customResponse = parseErrorResponse<any>(error);
    this.toastr.showError(customResponse.message || errorMessage);
    this.statusHandler.handleHttpStatus(error?.status);
    return throwError([]);
}

  getProfiles(): Observable<ProfileDto[]> {
    const url = GATEWAY_GET_ALL_PROFILES;

    const jwt = this.getJwtFromCookies();
    if(!jwt)
    {
      this.redirectToLogin();
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${jwt}`
    });
    return this.http.get<ProfileDto[]>(url, {headers}).pipe(
      tap(response => console.log('Profiles response:', response))
    );
  }

  deleteProfile(id: number): Observable<any> {
    const url = GATEWAY_DELETE_PROFILE(id);
    const jwt = this.getJwtFromCookies();
    
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${jwt}`
    });
    if(!jwt)
    {
      this.redirectToLogin();
    }
    console.log(headers)
    return this.http.delete(url, {headers});
  }

  deleteUser(userUUID: string) : Observable<any>{
    const url = GATEWAY_DELETE_USER(userUUID);
    const jwt = this.getJwtFromCookies();

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${jwt}`
    });
    if(!jwt)
    {
      this.redirectToLogin();
    }
    return this.http.delete(url, {headers});
  }
}
