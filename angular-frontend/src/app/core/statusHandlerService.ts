// http-status-handler.service.ts
import { Injectable } from '@angular/core';
import { ToastService } from './toastService';

@Injectable({
  providedIn: 'root',
})
export class HttpStatusHandlerService {
  constructor(private toastr: ToastService) {}

  handleHttpStatus(status: number): void {
    switch (status) {
      case 400:
        this.handleBadRequest();
        break;
      case 401:
        this.handleUnauthorized();
        break;
      case 403:
        this.handleForbidden();
        break;
      case 404:
        this.handleNotFound();
        break;
      case 405:
        this.handleMethodNotAllowed();
        break;
      case 408:
        this.handleRequestTimeout();
        break;
      case 409:
        this.handleConflict();
        break;
      case 422:
        this.handleUnprocessableEntity();
        break;
      case 429:
        this.handleTooManyRequests();
        break;
      case 500:
        this.handleInternalServerError();
        break;
      case 501:
        this.handleNotImplemented();
        break;
      case 502:
        this.handleBadGateway();
        break;
      case 503:
        this.handleServiceUnavailable();
        break;
      // Add more cases as needed
      default:
        this.handleDefault();
        break;
    }
  }

  // Include the corresponding methods for each status code
  private handleBadRequest(): void {
    this.toastr.showError('Bad Request error.');
  }

  private handleUnauthorized(): void {
    this.toastr.showError('Unauthorized error.');
    // Redirect to login or handle as needed
  }

  private handleForbidden(): void {
    this.toastr.showError('Forbidden error.');
    // Redirect to login or handle as needed
  }

  private handleNotFound(): void {
    this.toastr.showError('Not Found error.');
    // Redirect or handle as needed
  }

  private handleMethodNotAllowed(): void {
    this.toastr.showError('Method Not Allowed error.');
  }

  private handleRequestTimeout(): void {
    this.toastr.showError('Request Timeout error.');
  }

  private handleConflict(): void {
    this.toastr.showError('Conflict error.');
  }

  private handleUnprocessableEntity(): void {
    this.toastr.showError('Unprocessable Entity error.');
  }

  private handleTooManyRequests(): void {
    this.toastr.showError('Too Many Requests error. Please try again later.');
    // Implement rate-limiting logic if needed
  }

  private handleInternalServerError(): void {
    this.toastr.showError('Internal Server Error.');
    // Redirect or handle as needed
  }

  private handleNotImplemented(): void {
    this.toastr.showError('Not Implemented error.');
  }

  private handleBadGateway(): void {
    this.toastr.showError('Bad Gateway error.');
  }

  private handleServiceUnavailable(): void {
    this.toastr.showError('Service Unavailable error.');
    // Implement retry logic if needed
  }

  private handleDefault(): void {
    this.toastr.showError('An unexpected error occurred.');
  }
}
