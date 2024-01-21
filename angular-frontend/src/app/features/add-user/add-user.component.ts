import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RegistrationData } from '../.././models/user';
import { Router } from '@angular/router';
import { RegisterService } from '../../core/registerService';
import { ADMIN_ROLE } from 'src/app/utils/constants';
import { USERS_ENDPOINT, LOGIN_ENDPOINT } from 'src/app/utils/endpoints';
import { ToastService } from 'src/app/core/toastService';
import { CustomResponse } from 'src/app/models/responses/CustomResponse';
import { LoggerService } from 'src/app/core/loggerService';
import { HttpStatusHandlerService } from 'src/app/core/statusHandlerService';
import { CookieService } from 'ngx-cookie-service';
import { JWT_COOKIE_NAME } from 'src/app/utils/constants';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent {
  addUserForm: FormGroup = new FormGroup({});
  
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private toastr: ToastService,
    private registerService: RegisterService,
    private logger: LoggerService,
    private statusHandler: HttpStatusHandlerService,
    private cookieService: CookieService
  ) {
    this.addUserForm = this.initializeForm();
  }

  ngOnInit(): void {
    const jwt = this.getJwtFromCookies();
    if(!jwt)
    {
      this.router.navigate([LOGIN_ENDPOINT]);
      this.toastr.showError('Please log in to access this feature.');
    }
  }

  initializeForm(): FormGroup {
    return this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email, this.gmailValidator]],
      phoneNumber: ['', Validators.pattern(/^\d{10}$/)],
    }, { validator: this.passwordsMatchValidator });
  }

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

  private getJwtFromCookies(): string | null {
    return this.cookieService.get(JWT_COOKIE_NAME);
  }

  onSubmit() {
    if (this.addUserForm.valid) {
      const userRegistrationData = this.getUserRegistrationData();
      
      this.addUser(userRegistrationData);
    } else {
      this.handleInvalidForm();
    }
  }

  randomString(length: number, chars : string) {
    var result = '';
    for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
    return result;
}

  getUserRegistrationData(): RegistrationData {
    return {
      firstName: this.addUserForm.value.firstName,
      lastName: this.addUserForm.value.lastName,
      email: this.addUserForm.value.email,
      phoneNumber: this.addUserForm.value.phoneNumber,
      password: this.randomString(40, '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'),
      role: ADMIN_ROLE,
      isNotificationsEnabled: false,
    };
  }

  addUser(userRegistrationData: RegistrationData) {
    this.registerService.registerUser(userRegistrationData).subscribe({
      next: (response: CustomResponse<any>) => this.handleRegistrationSuccess(response),
      error: (error) => this.handleRegistrationError(error)
    });
  }

  handleRegistrationSuccess(response: CustomResponse<any>) {
    this.logger.log(response);
    if (response && response.timestamp && response.message !== undefined && response.payload !== undefined) {
      this.logger.log('Add user was successful:', response);
      this.toastr.showSuccess(response.message);
      this.router.navigate([USERS_ENDPOINT]);
    } else {
      this.logger.error('Unexpected response structure:', response);
      this.toastr.showError('Unexpected response structure. Please try again later.');
    }
  }

  handleRegistrationError(error: any) {
    this.logger.error('Add user failed:', error);

    if (error && error.error) {
      const customResponse: CustomResponse<any> = error?.error;
      if (customResponse && customResponse.message) {
        this.toastr.showError(customResponse.message);
      } else {
        this.toastr.showError('Add user failed. Please try again later.');
      }
    } else {
      this.toastr.showError('Add user failed. Please try again later.');
    }

      // You can also use the HTTP status code in your logic
      const httpStatus = error?.status;
      this.statusHandler.handleHttpStatus(httpStatus);
  }

  handleInvalidForm() {
    const emailControl = this.addUserForm.get('email');
    if (emailControl && emailControl.hasError('gmailAddress')) {
      this.toastr.showError('Invalid email address. Only Gmail addresses are allowed.');
      return;
    }
  
    const invalidControls = this.getInvalidControls(this.addUserForm);
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
}
