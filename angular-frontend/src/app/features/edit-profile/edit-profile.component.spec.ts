import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EditProfileComponent } from './edit-profile.component';
import { ProfileServiceService } from 'src/app/core/profile-service.service';
import { of } from 'rxjs';

describe('EditProfileComponent', () => {
  let component: EditProfileComponent;
  let fixture: ComponentFixture<EditProfileComponent>;
  let profileServiceSpy: jasmine.SpyObj<ProfileServiceService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('ProfileServiceService', ['editProfile']);

    TestBed.configureTestingModule({
      declarations: [EditProfileComponent],
      providers: [{ provide: ProfileServiceService, useValue: spy }]
    });

    fixture = TestBed.createComponent(EditProfileComponent);
    component = fixture.componentInstance;
    profileServiceSpy = TestBed.inject(ProfileServiceService) as jasmine.SpyObj<ProfileServiceService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call updateProfile and log success on successful response', () => {
    spyOn(console, 'log'); // Spy on console.log to check if it's called
    const mockProfile = {
      firstName: 'John',
      lastName: 'Doe',
      phone: '111111',
      uuid: 'uuid1'
    };
    profileServiceSpy.getProfile.and.returnValue(of({
      timestamp: new Date().toISOString(),
      message: 'Success',
      payload: mockProfile
    }));

    component.updateProfile();

    expect(profileServiceSpy.editProfile).toHaveBeenCalled();
    expect(console.log).toHaveBeenCalledWith('Profilul a fost actualizat cu succes');
  });

  it('should call updateProfile and log error on unsuccessful response', () => {
    const mockError = new Error('Test error');
    spyOn(console, 'error'); // Spy on console.error to check if it's called
    const mockProfile = {
      firstName: 'John',
      lastName: 'Doe',
      phone: '111111',
      uuid: 'uuid1'
    };
    profileServiceSpy.getProfile.and.returnValue(of({
      timestamp: new Date().toISOString(),
      message: 'Success',
      payload: mockProfile
    }));

    component.updateProfile();

    expect(profileServiceSpy.editProfile).toHaveBeenCalled();
    expect(console.error).toHaveBeenCalledWith('Eroare la actualizarea profilului', mockError);
  });
});
