import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MyProfileComponent } from './my-profile.component';
import { ProfileServiceService } from '../../core/profile-service.service';
import { of } from 'rxjs';

describe('MyProfileComponent', () => {
  let component: MyProfileComponent;
  let fixture: ComponentFixture<MyProfileComponent>;
  let profileServiceSpy: jasmine.SpyObj<ProfileServiceService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('ProfileServiceService', ['getProfile']);
    
    TestBed.configureTestingModule({
      declarations: [MyProfileComponent],
      providers: [{ provide: ProfileServiceService, useValue: spy }]
    });

    fixture = TestBed.createComponent(MyProfileComponent);
    component = fixture.componentInstance;
    profileServiceSpy = TestBed.inject(ProfileServiceService) as jasmine.SpyObj<ProfileServiceService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call getProfile and set profile on successful response', () => {
    const mockProfile = {
      firstName: 'John',
      lastName: 'Doe',
      phone: '111111',
      uuid: 'uuid1'
    };
    
    // Ajustează spy-ul să returneze un CustomResponse valid
    profileServiceSpy.getProfile.and.returnValue(of({
      timestamp: new Date().toISOString(),
      message: 'Success',
      payload: mockProfile
    }));
    
    component.getProfile();
    
    expect(profileServiceSpy.getProfile).toHaveBeenCalled();
    expect(component.profile).toEqual(mockProfile);
  });

  it('should log an error on unsuccessful response', () => {
    const mockError = new Error('Test error');
    
    // Ajustează spy-ul să returneze un CustomResponse valid
    profileServiceSpy.getProfile.and.returnValue(of({
      timestamp: new Date().toISOString(),
      message: 'Error fetching profile',
      payload: null
    }));
    
    spyOn(console, 'error');
    
    component.getProfile();

    expect(profileServiceSpy.getProfile).toHaveBeenCalled();
    expect(console.error).toHaveBeenCalledWith('Error fetching profile:', mockError);
    expect(console.error).toHaveBeenCalledWith('Full error object:', { error: mockError });
  });
});
