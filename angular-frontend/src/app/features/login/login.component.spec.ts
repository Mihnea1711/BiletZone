import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../core/authService';
import { ToastService } from 'src/app/core/toastService';
import { LoggerService } from 'src/app/core/loggerService';
import { HttpStatusHandlerService } from 'src/app/core/statusHandlerService';
import { RememberMeService } from 'src/app/core/rememberMeService';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: jasmine.SpyObj<AuthService>;
  let toastServiceMock: jasmine.SpyObj<ToastService>;
  let loggerServiceMock: jasmine.SpyObj<LoggerService>;
  let statusHandlerMock: jasmine.SpyObj<HttpStatusHandlerService>;
  let rememberMeServiceMock: jasmine.SpyObj<RememberMeService>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, RouterTestingModule],
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: ToastService, useValue: toastServiceMock },
        { provide: LoggerService, useValue: loggerServiceMock },
        { provide: HttpStatusHandlerService, useValue: statusHandlerMock },
        { provide: RememberMeService, useValue: rememberMeServiceMock },
      ],
    });
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form on ngOnInit', () => {
    const rememberMeData = { email: 'test@example.com', password: 'password123' };
    rememberMeServiceMock.getRememberMeData.and.returnValue(rememberMeData);

    component.ngOnInit();

    expect(component.loginForm.value.email).toBe(rememberMeData.email);
    expect(component.loginForm.value.password).toBe(rememberMeData.password);
    expect(component.loginForm.value.rememberMe).toBeTruthy();
  });

  it('should create a valid form on initializeForm', () => {
    const rememberMeData = { email: 'test@example.com', password: 'password123' };
    const form = component.initializeForm(rememberMeData);

    expect(form.value.email).toBe(rememberMeData.email);
    expect(form.value.password).toBe(rememberMeData.password);
    expect(form.value.rememberMe).toBeTruthy();
    expect(form.valid).toBeTruthy();
  });

  it('should toggle showPassword property on togglePasswordVisibility', () => {
    const initialValue = component.showPassword;
    component.togglePasswordVisibility();
    expect(component.showPassword).toBe(!initialValue);
  });
});
