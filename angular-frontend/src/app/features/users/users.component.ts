import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProfileService } from 'src/app/core/profile.service';

import { AuthService } from 'src/app/core/authService';
import { CookieService } from 'ngx-cookie-service';
import { JWT_COOKIE_NAME } from 'src/app/utils/constants';
import { ToastService } from 'src/app/core/toastService';
import { LOGIN_ENDPOINT } from 'src/app/utils/endpoints';
import { ProfileDto } from 'src/app/models/profile';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit{
  profiles: ProfileDto[] = [];

  isMobile: boolean = false; // Poate fi detectat mai avansat pentru dispozitive mobile

  constructor(private profileService: ProfileService, 
    private router: Router, 
    private authService: AuthService, 
    private cookieService: CookieService,
    private toastr: ToastService) 
    {
      // Simplificare: detectăm dacă este o afișare pe dispozitiv mobil
      this.isMobile = window.innerWidth <= 768;

      // Adăugăm un eveniment pentru a reacționa la modificările de dimensiuni ale ferestrei
      window.addEventListener('resize', () => {
        this.isMobile = window.innerWidth <= 768;
      });
  }

  private getJwtFromCookies(): string | null {
    return this.cookieService.get(JWT_COOKIE_NAME);
}

private redirectToLogin(): void {
    this.router.navigate([LOGIN_ENDPOINT]);
    this.toastr.showError('Please log in to access this feature.');
}

  ngOnInit(): void {
    const jwt = this.getJwtFromCookies();
    if(!jwt)
    {
      this.redirectToLogin();
    }
    else {
      this.loadProfiles();
    }
  }

  private loadProfiles(): void {
    this.profileService.getProfiles().subscribe((profiles) => {
      this.profiles = profiles;
    }, (error) => {
      console.error('Error fetching profiles:', error);
      // Tratați eroarea aici, afișând un mesaj pentru utilizator sau luând alte măsuri necesare.
    });
  }

  deleteUser(profile: ProfileDto): void {
    this.profileService.deleteProfile(profile.id).subscribe(() => {
      this.profiles = this.profiles.filter(p => p.id !== profile.id);
    });
    this.profileService.deleteUser(profile.userUUID).subscribe(() => {
      this.profiles = this.profiles.filter(p => p.userUUID !== profile.userUUID);
    });
  }

  navigateToAddUser() {
    this.router.navigate(['/add-user']);
  }

}
