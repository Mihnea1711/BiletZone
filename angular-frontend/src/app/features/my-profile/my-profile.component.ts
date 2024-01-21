import { Component, OnInit } from '@angular/core';
import { ProfileServiceService } from '../../core/profile-service.service';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.css']
})

export class MyProfileComponent implements OnInit {
  profile: any;

  constructor(private profileService: ProfileServiceService) { }

  ngOnInit(): void {
    // Call the getProfile method when the component is initialized
    this.getProfile();
  }

  getProfile(): void {
    this.profileService.getProfile().subscribe(
      (response) => {
        console.log('Profile response:', response);  // Mesaj pentru a afișa răspunsul primit
        // Adjust this based on the actual response structure
        this.profile = response.payload;
      },
      (error) => {
        console.error('Error fetching profile:', error);
        // Log the entire error object
        console.error('Full error object:', error);
      }
    );
  }
}
