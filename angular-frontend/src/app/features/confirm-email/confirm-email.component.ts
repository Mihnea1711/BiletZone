import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ConfirmationRequest } from 'src/app/models/user';
import { TOKEN_QUERY_PARAM } from 'src/app/utils/constants';
import { GATEWAY_CONFIRMATION_MAIL, HOME_ENDPOINT } from 'src/app/utils/endpoints';
import { LoggerService } from 'src/app/core/loggerService';
import { HttpStatusHandlerService } from 'src/app/core/statusHandlerService';
import { ToastService } from 'src/app/core/toastService';
import { CustomResponse } from 'src/app/models/responses/CustomResponse';
import { parseErrorResponse } from 'src/app/utils/utils';

@Component({
  selector: 'app-confirm-email',
  templateUrl: './confirm-email.component.html',
  styleUrls: ['./confirm-email.component.css']
})
export class ConfirmEmailComponent implements OnInit {
  readonly apiURL: string = GATEWAY_CONFIRMATION_MAIL; // Set your actual endpoint here
  confirmationToken: string = '';

  constructor(
    private route: ActivatedRoute, 
    private router: Router,
    private http: HttpClient,
    private logger: LoggerService,
    private statusHandler: HttpStatusHandlerService,
    private toastr: ToastService,
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.confirmationToken = params[TOKEN_QUERY_PARAM];
      this.confirmEmail();
    });
  }

  confirmEmail() {
    const confirmationRequest: ConfirmationRequest = { confirmationToken: this.confirmationToken };

    this.http.patch<CustomResponse<any>>(this.apiURL, confirmationRequest)
      .subscribe({
        next: (response) => {
          this.handleConfirmationSuccess(response);
        },
        error: (error) => {
          this.handleConfirmationError(error);
        },
      });
    
    this.router.navigate([HOME_ENDPOINT]);
  }

  private handleConfirmationSuccess(response: CustomResponse<any>): void {
    console.log('Email confirmation successful:', response);
    if (response && response.timestamp && response.message !== undefined && response.payload !== undefined) {
      this.logger.log('Email confirmation successful:', response);
      this.toastr.showSuccess(response.message);
    } else {
      this.logger.error('Unexpected response structure:', response);
      this.toastr.showError('Unexpected response structure. Please try again later.');
    }
    return;
  }

  private handleConfirmationError(error: any): void {
    console.error('Email confirmation failed:', error);
    const customResponse = parseErrorResponse<any>(error);
    this.toastr.showError(customResponse.message || 'Email confirmation failed.');
    this.statusHandler.handleHttpStatus(error?.status);
    return;
  }
}
