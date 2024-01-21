import { Component } from '@angular/core';
import { AuthService } from '../../core/authService';
import { SearchService } from '../../core/searchService';
import { FilterService } from 'src/app/core/filterService';
import { ProfileService } from 'src/app/core/profile.service';
import { ADMIN_ROLE, JWT_COOKIE_NAME } from 'src/app/utils/constants';
import { CookieService } from 'ngx-cookie-service';
import { parseJwt } from 'src/app/utils/utils';
import { ActivatedRoute, Router } from '@angular/router';
import { HOME_ENDPOINT } from 'src/app/utils/endpoints';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  searchQueryValue: string = '';
  isAdmin: boolean = false;
  isOnHomePage: boolean = false;
  private tokenKey = JWT_COOKIE_NAME;

  constructor(
    private authService: AuthService, 
    private searchService: SearchService,
    private profileService: ProfileService,
    private cookieService: CookieService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.checkAdminStatus();
    this.checkURLForHome();
  }

    // Get the token from the cookie
  getToken(): string | undefined {
    return this.cookieService.get(this.tokenKey);
  }

  private checkAdminStatus(): void {
    const token = this.getToken();
    if (token) {
      const decodedToken = parseJwt(token);
      this.isAdmin = !!decodedToken && decodedToken.role === ADMIN_ROLE;
    }
  }

  private checkURLForHome(): void {
    const currentRoute = window.location.pathname;   
    this.isOnHomePage = (currentRoute === HOME_ENDPOINT)
  }

  isLoggedIn() {
    return this.authService.isLoggedInUser();
  }

  onLogout() {
    this.authService.logout();
  }

  onSearch() {
    this.searchService.searchEvents(this.searchQueryValue);  
  }

  onFavorites() {    
    this.router.navigate([HOME_ENDPOINT])
    this.searchService.searchFavoriteEvents();
  }

  onUsers(){

    this.profileService.getProfiles();
  }
}
