export interface LoginData {
    email: string;
    password: string;
}

export interface RegistrationData {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  password: string;
  role: string;
  isNotificationsEnabled: boolean;
}

export interface UserDto {
  uuid: string;
  email: string;
  password: string;
  role: string;
}

export interface ConfirmationRequest {
  confirmationToken: string;
}
