import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RegistrationData } from '../.././models/user';
import { Router } from '@angular/router';
import { RegisterService } from '../../core/registerService';
import { USER_ROLE } from 'src/app/utils/constants';
import { LOGIN_ENDPOINT } from 'src/app/utils/endpoints';
import { ToastService } from 'src/app/core/toastService';
import { CustomResponse } from 'src/app/models/responses/CustomResponse';
import { LoggerService } from 'src/app/core/loggerService';
import { HttpStatusHandlerService } from 'src/app/core/statusHandlerService';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup = new FormGroup({});
  showPassword = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private toastr: ToastService,
    private registerService: RegisterService,
    private logger: LoggerService,
    private statusHandler: HttpStatusHandlerService
  ) {
    this.registerForm = this.initializeForm();
  }

  initializeForm(): FormGroup {
    return this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email, this.gmailValidator]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      phoneNumber: ['', Validators.pattern(/^\d{10}$/)],
      receiveNotifications: [false]
    }, { validator: this.passwordsMatchValidator });
  }

  // Custom validator function
  passwordsMatchValidator(group: FormGroup) {
    const password = group.get('password')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;

    return password === confirmPassword ? null : { passwordsNotMatch: true };
  }

  // Custom validator function for checking Gmail addresses
  gmailValidator(control: AbstractControl): { [key: string]: any } | null {
    const email: string = control.value;
    const isGmail = email.toLowerCase().endsWith('@gmail.com');
    return isGmail ? null : { 'gmailAddress': true };
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const userRegistrationData = this.getUserRegistrationData();
      console.log(userRegistrationData);
      
      this.registerUser(userRegistrationData);
    } else {
      this.handleInvalidForm();
    }
  }

  getUserRegistrationData(): RegistrationData {
    return {
      firstName: this.registerForm.value.firstName,
      lastName: this.registerForm.value.lastName,
      email: this.registerForm.value.email,
      phoneNumber: this.registerForm.value.phoneNumber,
      password: this.registerForm.value.password,
      role: USER_ROLE,
      isNotificationsEnabled: this.registerForm.value.receiveNotifications,
    };
  }

  registerUser(userRegistrationData: RegistrationData) {
    this.registerService.registerUser(userRegistrationData).subscribe({
      next: (response: CustomResponse<any>) => this.handleRegistrationSuccess(response),
      error: (error) => this.handleRegistrationError(error)
    });
  }

  handleRegistrationSuccess(response: CustomResponse<any>) {
    this.logger.log(response);
    if (response && response.timestamp && response.message !== undefined && response.payload !== undefined) {
      this.logger.log('Registration successful:', response);
      this.toastr.showSuccess(response.message);
      this.router.navigate([LOGIN_ENDPOINT]);
    } else {
      this.logger.error('Unexpected response structure:', response);
      this.toastr.showError('Unexpected response structure. Please try again later.');
    }
  }

  handleRegistrationError(error: any) {
    this.logger.error('Registration failed:', error);

    if (error && error.error) {
      const customResponse: CustomResponse<any> = error?.error;
      if (customResponse && customResponse.message) {
        this.toastr.showError(customResponse.message);
      } else {
        this.toastr.showError('Registration failed. Please try again later.');
      }
    } else {
      this.toastr.showError('Registration failed. Please try again later.');
    }

      // You can also use the HTTP status code in your logic
      const httpStatus = error?.status;
      this.statusHandler.handleHttpStatus(httpStatus);
  }

  handleInvalidForm() {
    if (this.registerForm.hasError('passwordsNotMatch')) {
      this.toastr.showError('Passwords do not match');
      return;
    }
  
    const emailControl = this.registerForm.get('email');
    if (emailControl && emailControl.hasError('gmailAddress')) {
      this.toastr.showError('Invalid email address. Only Gmail addresses are allowed.');
      return;
    }
  
    const invalidControls = this.getInvalidControls(this.registerForm);
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
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}


