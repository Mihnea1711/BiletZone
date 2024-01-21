import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LoggerService {
  log(message?: any, ...optionalParams: any[]) {
    console.log(message, ...optionalParams);
  }

  error(message: string, ...optionalParams: any[]) {
    console.error(message, ...optionalParams);
  }

  // Add more logging methods as needed
}
