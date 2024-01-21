import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { UsersComponent } from './users.component';
import { ProfileService } from 'src/app/core/profile.service';
import { of } from 'rxjs';
import { ProfileDto } from 'src/app/models/profile';
import { Router } from '@angular/router';

describe('UsersComponent', () => {
  let component: UsersComponent;
  let fixture: ComponentFixture<UsersComponent>;
  let profileServiceSpy: jasmine.SpyObj<ProfileService>;
  let router: Router;

  const mockProfiles: ProfileDto[] = [
    { id: 1, userUUID: 'uuid1', firstName: 'John', lastName: 'Doe', phoneNumber: '123456789' },
    { id: 2, userUUID: 'uuid2', firstName: 'Jane', lastName: 'Doe', phoneNumber: '987654321' }
  ];

  beforeEach(() => {
    const spy = jasmine.createSpyObj('ProfileService', ['getProfiles', 'deleteProfile', 'deleteUser']);

    TestBed.configureTestingModule({
      declarations: [UsersComponent],
      providers: [{ provide: ProfileService, useValue: spy }],
      imports: [RouterTestingModule],
    });

    fixture = TestBed.createComponent(UsersComponent);
    component = fixture.componentInstance;
    profileServiceSpy = TestBed.inject(ProfileService) as jasmine.SpyObj<ProfileService>;
    router = TestBed.inject(Router);

    profileServiceSpy.getProfiles.and.returnValue(of(mockProfiles));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load profiles on init', () => {
    expect(profileServiceSpy.getProfiles).toHaveBeenCalled();
    expect(component.profiles).toEqual(mockProfiles);
  });

  it('should redirect to login if no JWT token', fakeAsync(() => {
    spyOn(router, 'navigate');
  
    profileServiceSpy.getProfiles.and.returnValue(of([]));
    component.ngOnInit();
    tick();
  
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  }));

  it('should navigate to add user', () => {
    spyOn(router, 'navigate');

    component.navigateToAddUser();

    expect(router.navigate).toHaveBeenCalledWith(['/add-user']);
  });

  it('should delete user and refresh profiles', fakeAsync(() => {
    profileServiceSpy.deleteProfile.and.returnValue(of({}));
    profileServiceSpy.deleteUser.and.returnValue(of({}));

    const profileToDelete = mockProfiles[0];

    component.deleteUser(profileToDelete);
    tick();

    expect(profileServiceSpy.deleteProfile).toHaveBeenCalledWith(profileToDelete.id);
    expect(profileServiceSpy.deleteUser).toHaveBeenCalledWith(profileToDelete.userUUID);
    expect(component.profiles).toEqual([mockProfiles[1]]);
  }));
});
