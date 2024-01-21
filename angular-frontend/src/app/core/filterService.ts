import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AFTER_THEN_PARAM, BEFORE_THAN_PARAM, CITY_PARAM, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, EVENT_TYPE_PARAM, MAX_PAGE_SIZE, PAGE_NUMBER_PARAM, PAGE_SIZE_PARAM } from '../utils/constants'
import { EventDto } from '../models/event';
import { GATEWAY_SEARCH_EVENTS } from '../utils/endpoints';
import { buildCompleteURL, buildQueryString, parseErrorResponse } from '../utils/utils';
import { CustomResponse } from '../models/responses/CustomResponse';
import { LoggerService } from './loggerService';
import { ToastService } from './toastService';
import { HttpStatusHandlerService } from './statusHandlerService';
import { PaginationService } from './paginationService';

@Injectable({
  providedIn: 'root',
})
export class FilterService {
  private filteredItemsSubject = new BehaviorSubject<EventDto[]>([]);
  filteredItems$ = this.filteredItemsSubject.asObservable();
  private endpoint = GATEWAY_SEARCH_EVENTS;

  constructor(
    private http: HttpClient,
    private logger: LoggerService,
    private statusHandler: HttpStatusHandlerService,
    private toastr: ToastService,
    private paginationService: PaginationService
  ) {}

  applyFilters(beforeThanDate: string, afterThanDate: string, city: string, eventType: string) { 
    const { pageIndex, pageSize } = this.paginationService.getPaginationParams();
    const queryParams = {
      ...(beforeThanDate && { [BEFORE_THAN_PARAM]: beforeThanDate }),
      ...(afterThanDate && { [AFTER_THEN_PARAM]: afterThanDate }),
      ...(city && { [CITY_PARAM]: city }),
      ...(eventType && { [EVENT_TYPE_PARAM]: eventType }),
      // [PAGE_NUMBER_PARAM]: (pageIndex !== undefined && pageIndex > 0) ? [pageIndex] : [DEFAULT_PAGE_NUMBER],
      // [PAGE_SIZE_PARAM]: (pageSize !== undefined && pageSize > 0 && pageSize < MAX_PAGE_SIZE) ? [pageSize] : [DEFAULT_PAGE_SIZE],
    };    
    const rawQueryString = buildQueryString(queryParams);
    const apiURL = buildCompleteURL(this.endpoint, rawQueryString);

    console.log(apiURL);

    this
      .http.get<CustomResponse<any>>(apiURL)
      .pipe(
          catchError(error => {
            this.logger.error('Error in applyFilters:', error);
            const customResponse = parseErrorResponse<any>(error);
            this.toastr.showError(customResponse.message || 'Failed to apply filters.');
            this.statusHandler.handleHttpStatus(error?.status);
            return [];
          })
      )
      .subscribe((filteredItems) => {
        this.logger.log('Filters applied successfully.');
        this.filteredItemsSubject.next(filteredItems.payload);
      });
  }
}
