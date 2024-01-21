 // edit-profile.component.ts

import { Component, OnInit } from '@angular/core';
import { ProfileServiceService } from 'src/app/core/profile-service.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})

export class EditProfileComponent implements OnInit {

  user: any = {};
  newPassword: string = '';

  constructor(
    private profileService: ProfileServiceService
  ) {}

  ngOnInit() {
   
  }
  
  updateProfile(): void {
    // Actualizează doar câmpurile pe care vrei să le trimiți înapoi către backend
    const updatedData = {
      id: 0,
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      phoneNumber: this.user.phone,
      userUUID:"0"

    };
    
    // Utilizează serviciul pentru a actualiza profilul
    this.profileService.editProfile(updatedData).subscribe(
      () => {
        console.log('Profilul a fost actualizat cu succes');
      },
      (error) => {
        console.error('Eroare la actualizarea profilului', error);
      }
    );
  }
}