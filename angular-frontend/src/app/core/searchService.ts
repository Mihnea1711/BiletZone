import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, forkJoin, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { JWT_COOKIE_NAME, NAME_PARAM } from '../utils/constants'
import { GATEWAY_GET_FAVORITE_EVENTS, GATEWAY_SEARCH_EVENTS, HOME_ENDPOINT, LOGIN_ENDPOINT } from '../utils/endpoints';
import { buildCompleteURL, buildQueryString, parseErrorResponse } from '../utils/utils';
import { CustomResponse } from '../models/responses/CustomResponse';
import { EventDto } from '../models/event';
import { LoggerService } from './loggerService';
import { HttpStatusHandlerService } from './statusHandlerService';
import { ToastService } from './toastService';
import { PaginationService } from './paginationService';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
    private searchedItemsSubject = new BehaviorSubject<EventDto[]>([]);
    searchedItems$: Observable<EventDto[]> = this.searchedItemsSubject.asObservable();
    private search_events_endpoint: string = GATEWAY_SEARCH_EVENTS;

    constructor(
        private http: HttpClient,
        private logger: LoggerService,
        private statusHandler: HttpStatusHandlerService,
        private toastr: ToastService,
        private paginationService: PaginationService,
        private cookieService: CookieService,
        private router: Router,
    ) {}

    private getJwtFromCookies(): string | null {
        return this.cookieService.get(JWT_COOKIE_NAME);
    }

    private redirectToLogin(): void {
        this.router.navigate([LOGIN_ENDPOINT]);
        this.toastr.showError('Please log in to access this feature.');
    }

    private handleHttpError(error: any, errorMessage: string): Observable<never> {
        this.logger.error(errorMessage, error);
        const customResponse = parseErrorResponse<any>(error);
        this.toastr.showError(customResponse.message || errorMessage);
        this.statusHandler.handleHttpStatus(error?.status);
        return throwError([]);
    }

    private mapEventsWithFavorites(allEvents: EventDto[], favoriteEvents: EventDto[]): EventDto[] {
        const favoriteEventIds = new Set(favoriteEvents.map(item => item.id));
        return allEvents.map(item => ({
            ...item,
            isFavourite: favoriteEventIds.has(item.id),
        }));
    }

    private logAndNextItems(items: EventDto[], successMessage: string): void {
        this.searchedItemsSubject.next(items);
        this.logger.log(successMessage);
    }

    searchEvents(query: string): void {
        const { pageIndex, pageSize } = this.paginationService.getPaginationParams();
        const queryParams = {
            ...(query ? { [NAME_PARAM]: query } : {}),
        };
        const rawQueryString = buildQueryString(queryParams);
        const apiURL = buildCompleteURL(this.search_events_endpoint, rawQueryString);    

        const jwt = this.getJwtFromCookies();
        if (!jwt) {
            this.http.get<CustomResponse<EventDto[]>>(apiURL)
                .pipe(
                    catchError(error => this.handleHttpError(error, 'Error in searchEvents:'))
                )
                .subscribe((searchedItems) => {
                    if (searchedItems.payload != null)
                        this.logAndNextItems(searchedItems.payload, 'Search successful.')
                });
            return;
        }        

        const headers = new HttpHeaders({
            'Authorization': `Bearer ${jwt}`
        });
   
        const favorites_endpoint = GATEWAY_GET_FAVORITE_EVENTS;
        forkJoin([
            this.http.get<CustomResponse<EventDto[]>>(favorites_endpoint, { headers }),
            this.http.get<CustomResponse<EventDto[]>>(apiURL)
                .pipe(
                    catchError(error => this.handleHttpError(error, 'Error in searchEvents:'))
                )
        ]).subscribe(([favoriteItems, searchedItems]) => {
            if (favoriteItems.payload != null && searchedItems.payload != null) {
                const convertedItems = this.mapEventsWithFavorites(searchedItems.payload, favoriteItems.payload);
                this.logAndNextItems(convertedItems, 'Search successful.');
            }
        });
    }

    searchFavoriteEvents(): void {    
        const jwt = this.getJwtFromCookies();
        if (!jwt) {
            this.redirectToLogin();
            return;
        }        
        const headers = new HttpHeaders({
          'Authorization': `Bearer ${jwt}`
        });
        
        const favorites_endpoint = GATEWAY_GET_FAVORITE_EVENTS;

        this.http.get<CustomResponse<EventDto[]>>(favorites_endpoint, { headers })
            .pipe(
                catchError(error => this.handleHttpError(error, 'Error in searchFavoriteEvents:'))
            )
            .subscribe((searchedItems: CustomResponse<EventDto[]>) => {
                if (searchedItems.payload != null) {
                    const convertedItems: EventDto[] = searchedItems.payload.map(item => ({
                        id: item.id,
                        name: item.name,
                        description: item.description,
                        city: item.city,
                        location: item.location,
                        type: item.type,
                        date: item.date,
                        image: item.image,
                        isFavourite: true,
                        hallMapID: item.hallMapID,
                    }));
                    this.logAndNextItems(convertedItems, 'Search for favorite events successful.');
                }
            });
    }
}
