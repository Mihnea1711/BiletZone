import { TestBed, ComponentFixture } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { AddUserComponent } from './add-user.component';
import { RegisterService } from '../../core/registerService';
import { ToastService } from '../../core/toastService';
import { ADMIN_ROLE } from 'src/app/utils/constants';
import { HttpClient } from '@angular/common/http';

describe('AddUserComponent', () => {
  let fixture: ComponentFixture<AddUserComponent>;
  let component: AddUserComponent;
  let registerServiceSpy: jasmine.SpyObj<RegisterService>;
  let routerSpy: jasmine.SpyObj<Router>;
  let toastrServiceSpy: jasmine.SpyObj<ToastService>;

  beforeEach(() => {
    registerServiceSpy = jasmine.createSpyObj('RegisterService', ['registerUser']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    toastrServiceSpy = jasmine.createSpyObj('ToastrService', ['showSuccess', 'showError']);

    TestBed.configureTestingModule({
      declarations: [AddUserComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: Router, useValue: routerSpy },
        { provide: ToastService, useValue: toastrServiceSpy },
        { provide: RegisterService, useValue: registerServiceSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AddUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form correctly', () => {
    const formControls = component.addUserForm.controls;
    expect(formControls['firstName'].value).toEqual('Ana');
    expect(formControls['lastName'].value).toEqual('Maria');
    // ... adaugă aici teste pentru celelalte câmpuri ale formularului
  });

  it('should call registerUser when form is submitted', () => {
    // Presupunând că vei adăuga datele necesare pentru formular înainte de submit
    const userRegistrationData = { firstName: "Ana",
      lastName: "Maria",
      email: "ana@gmail.com",
      phoneNumber: "0722721144",
      password: "parola",
      role: ADMIN_ROLE,
      isNotificationsEnabled: false, };
    component.addUserForm.setValue(userRegistrationData);

    // Simulează apelul metodei onSubmit
    component.onSubmit();

    // Verifică dacă metoda registerUser a fost apelată cu datele corecte
    expect(registerServiceSpy.registerUser).toHaveBeenCalledWith(userRegistrationData);
  });

  it('should handle registration success correctly', () => {
    // Simulează un răspuns de succes
    const successResponse = { timestamp: 'timestamp', message: 'Success message', payload: {} };
    registerServiceSpy.registerUser.and.returnValue(of(successResponse));

    // Simulează apelul metodei onSubmit
    component.onSubmit();

    // Verifică dacă toastr.showSuccess a fost apelată
    expect(toastrServiceSpy.showSuccess).toHaveBeenCalledWith(successResponse.message);

    // Verifică dacă router.navigate a fost apelată pentru a redirecționa către USERS_ENDPOINT
    expect(routerSpy.navigate).toHaveBeenCalledWith(['USERS_ENDPOINT']);
  });

  it('should handle registration error correctly', () => {
    // Simulează un răspuns de eroare
    const errorResponse = { error: { message: 'Error message' } };
    registerServiceSpy.registerUser.and.returnValue(throwError(errorResponse));

    // Simulează apelul metodei onSubmit
    component.onSubmit();

    // Verifică dacă toastr.showError a fost apelată
    expect(toastrServiceSpy.showError).toHaveBeenCalledWith(errorResponse.error.message);

  });
});