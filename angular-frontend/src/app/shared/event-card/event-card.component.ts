import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { LoggerService } from 'src/app/core/loggerService';
import { HttpStatusHandlerService } from 'src/app/core/statusHandlerService';
import { ToastService } from 'src/app/core/toastService';
import { EventDto, PatchFavouriteEvent } from 'src/app/models/event';
import { CustomResponse } from 'src/app/models/responses/CustomResponse';
import { DEFAULT_IMAGE_LINK, JWT_COOKIE_NAME } from 'src/app/utils/constants';
import { EVENT_PAGE_BUILDER, GATEWAY_ADD_EVENT_TO_FAVORITES, GATEWAY_DELETE_EVENT_FROM_FAVORITES, GATEWAY_PATCH_EVENT, LOGIN_ENDPOINT } from 'src/app/utils/endpoints';

@Component({
  selector: 'app-event-card',
  templateUrl: './event-card.component.html',
  styleUrls: ['./event-card.component.css']
})
export class EventCardComponent implements OnInit {  
  @Input() id: number = 0;
  @Input() name: string = '';
  @Input() image: string = '';
  @Input() type: string = '';
  @Input() isFavourite: boolean = false;

  // Define a pattern for a valid image link (you can adjust this as needed)
  private imageLinkPattern = /^(http(s?):\/\/)(www\.)?[\w-]+(\.[\w-]+)+([\w.,@?^=%&:/~+#-]*[\w@?^=%&/~+#-])?$/;
  private debounceTimeout: any = null;
  
  constructor(
    private router: Router,
    private http: HttpClient,
    private toastr: ToastService,  
    private logger: LoggerService,
    private statusHandler: HttpStatusHandlerService,
    private cookieService: CookieService,
  ) {}

  getJwtFromCookie(): string | null {
    return this.cookieService.get(JWT_COOKIE_NAME);
  }

  ngOnInit(): void {
    // Set default image link if not provided or is not valid
    if (!this.isValidImageLink(this.image)) {
      this.image = DEFAULT_IMAGE_LINK;
    }    
  }

  handleClick(event: Event) {
    // Get the id attribute of the clicked element
    const clickedElement = event.target as HTMLElement;
    const iconId = clickedElement.getAttribute('id');
  
    if (iconId == "fav-icon" || iconId == "fav-icon-container") {
      this.debounceToggleFavourites();
    } else {
      if (this.id !== 0) {
        // You can construct the route or path dynamically based on your requirements
        const eventDetailsRoute = EVENT_PAGE_BUILDER(encodeURIComponent(this.id.toString()));
        // Navigate to the specified route
        this.router.navigate([eventDetailsRoute]);
      }
    }
  }

  debounceToggleFavourites() {
    if (this.debounceTimeout !== null) {
      // If timeout is active, show toast and return   
      this.toastr.showError('Please wait before clicking again.');
      return;
    }

    if (this.isFavourite) {
      // Perform removeFromFavourites action
      this.removeFromFavourites();
    } else {
      // Perform addToFavourites action
      this.addtoFavourites();
    } 

    // Set timeout for 2 seconds
    this.debounceTimeout = setTimeout(() => {
      // Reset timeout
      clearTimeout(this.debounceTimeout);
      this.debounceTimeout = null;
    }, 2000);
  }
  
  private addtoFavourites() {
    const jwt = this.getJwtFromCookie()
    if (!jwt) {
      this.toastr.showError('Please log in to access this feature');
      this.router.navigate([LOGIN_ENDPOINT]);
      return;
    }
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${jwt}`
    });
    const toggleFavouriteEndpoint = GATEWAY_ADD_EVENT_TO_FAVORITES(encodeURIComponent(this.id.toString()));        
    this.http.post<CustomResponse<any>>(toggleFavouriteEndpoint, null, { headers })
    .subscribe({
      next: (response) => this.handleSuccess(response),
      error: (error) => this.handleError(error)
    });      
  }

  private removeFromFavourites() {
    const jwt = this.getJwtFromCookie()
    if (!jwt) {
      this.toastr.showError('Please log in to access this feature');
      this.router.navigate([LOGIN_ENDPOINT]); // Adjust the route path as needed.
      return;
    }
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${jwt}`
    });
    const removeFromFavouriteEndpoint = GATEWAY_DELETE_EVENT_FROM_FAVORITES(encodeURIComponent(this.id.toString()));  
    this.http.delete<CustomResponse<any>>(removeFromFavouriteEndpoint, { headers })
    .subscribe({
      next: (response) => this.handleSuccess(response),
      error: (error) => this.handleError(error)
    });      
  }

  private handleSuccess(response: CustomResponse<any>) {
    if (response && response.timestamp && response.message !== undefined && response.payload !== undefined) {
      this.isFavourite = !this.isFavourite;
      if(this.isFavourite) {
        this.toastr.showSuccess("Added to favorites");
        this.logger.log('Event added to favorites successfully:', response);
      } else {
        this.toastr.showError("Removed from favorites");
        this.logger.log('Event removed from favorites successfully:', response);
      }
    } else {
      this.logger.error('Unexpected response structure:', response);
      this.toastr.showError("An enexpected error occured. Please try again..")
    }
  }

  private handleError(error: any) {
    this.logger.error('Patch Event failed:', error);

    if (error && error.error) {
      const customResponse: CustomResponse<any> = error?.error;
      if (customResponse && customResponse.message) {
        this.toastr.showError(customResponse.message);
      } else {
        this.toastr.showError('Patch event failed. Please try again later.');
      }
    } else {
      this.toastr.showError('Patch event failed. Please try again later.');
    }

    // You can also use the HTTP status code in your logic
    const httpStatus = error?.status;
    this.statusHandler.handleHttpStatus(httpStatus);
  }

  // Custom validator function to check if the image link is valid
  private isValidImageLink(link: string): boolean {
    return this.imageLinkPattern.test(link);
  }
}
