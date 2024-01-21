import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginData } from '../../models/user';
import { AuthService } from '../../core/authService';
import { HOME_ENDPOINT } from 'src/app/utils/endpoints';
import { ToastService } from 'src/app/core/toastService';
import { LoggerService } from 'src/app/core/loggerService';
import { HttpStatusHandlerService } from 'src/app/core/statusHandlerService';
import { CustomResponse } from 'src/app/models/responses/CustomResponse';
import { RememberMeService } from 'src/app/core/rememberMeService';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  rememberMeData: LoginData | null = null;
  showPassword = false;
  loginForm: FormGroup = new FormGroup({});

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private toastr: ToastService,
    private authService: AuthService,
    private logger: LoggerService,
    private statusHandler: HttpStatusHandlerService,
    private rememberMeService: RememberMeService,
  ) {}

  ngOnInit(): void {
    // Retrieve remember-me data when the component initializes
    this.rememberMeData = this.rememberMeService.getRememberMeData();
    this.loginForm = this.initializeForm(this.rememberMeData);
  }

  initializeForm(rememberMeData: LoginData | null): FormGroup {
    return this.fb.group({
      email: [rememberMeData?.email || '', [Validators.required, Validators.email]],
      password: [rememberMeData?.password || '', [Validators.required, Validators.minLength(6)]],
      rememberMe: [!!rememberMeData]  // Set to true if rememberMeData is present, otherwise false
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const userLoginData = this.getUserLoginData();
      this.loginUser(userLoginData);
    } else {
      this.handleInvalidForm();
    }
  }

  getUserLoginData(): LoginData {
    return {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password,
    };
  }

  loginUser(userLoginData: LoginData) {
    this.logger.log(userLoginData);
    this.authService.loginUser(userLoginData).subscribe({
      next: (response: CustomResponse<any>) => this.handleLoginSuccess(response),
      error: (error) => this.handleLoginError(error)
    });
  }

  handleLoginSuccess(response: CustomResponse<any>) {
    if (response && response.timestamp && response.message !== undefined && response.payload !== undefined) {
      this.logger.log('Login successful:', response);
      this.toastr.showSuccess(response.message);

      // store cookie
      this.authService.login(response.payload);      

      // store remember me data
      if (this.loginForm.value.rememberMe) {
        this.rememberMeService.storeRememberMeData(this.getUserLoginData())
      }

      this.router.navigate([HOME_ENDPOINT]);
    } else {
      this.logger.error('Unexpected response structure:', response);
      this.toastr.showError('Unexpected response structure. Please try again later.');
      return;
    }
  }

  handleLoginError(error: any) {
    this.logger.error("Login failed", error)

    if (error && error.error) {
      const customResponse: CustomResponse<any> = error?.error;
      if (customResponse && customResponse.message) {
        this.toastr.showError(customResponse.message);
      } else {
        this.toastr.showError('Login failed. Please try again later.');
      }
    } else {
      this.toastr.showError('Login failed. Please try again later.');
    }

    // You can also use the HTTP status code in your logic
    const httpStatus = error?.status;
    this.statusHandler.handleHttpStatus(httpStatus);
    return;
  }

  handleInvalidForm() {
    const invalidControls = this.getInvalidControls(this.loginForm);
    this.displayInvalidFormError(invalidControls);
  }

  getInvalidControls(formGroup: FormGroup): {[key: string]: string[]} {
    const invalidControls: { [key: string]: string[] } = {};

    Object.keys(formGroup.controls).forEach(controlName => {
      const control = formGroup.get(controlName);

      if (control && control.invalid) {
        invalidControls[controlName] = [];

        Object.keys(control.errors!).forEach(errorName => {
          invalidControls[controlName].push(errorName);
        });
      }
    });

    return invalidControls;
  }

  displayInvalidFormError(invalidControls: { [key: string]: string[] }) {
    this.logger.log('Invalid Controls:', invalidControls);
    this.toastr.showError(`Please fill out the form correctly. Invalid ${Object.keys(invalidControls)[0]}: ${invalidControls[Object.keys(invalidControls)[0]].join(', ')}`)
    return;
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}
