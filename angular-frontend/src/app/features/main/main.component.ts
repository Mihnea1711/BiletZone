import { Component, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

import { FilterService } from 'src/app/core/filterService';
import { SearchService } from 'src/app/core/searchService';
import {EventDto} from '../../models/event';
import { LoggerService } from 'src/app/core/loggerService';
import { PaginationService } from 'src/app/core/paginationService';
import { ADMIN_ROLE, DEFAULT_PAGE_SIZE_LIST, JWT_COOKIE_NAME } from 'src/app/utils/constants';
import { CookieService } from 'ngx-cookie-service';
import { parseJwt } from 'src/app/utils/utils';
import { ADD_EVENT_PAGE } from 'src/app/utils/endpoints';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})

export class MainComponent implements OnDestroy {
  items: EventDto[] = [];
  pagedItems: EventDto[] = [];
  private unsubscribe$ = new Subject<void>();
  private tokenKey = JWT_COOKIE_NAME;
  isAdmin: boolean = false;

  isSidebarOpen = false;
  pageSizeOptions: number[] = DEFAULT_PAGE_SIZE_LIST

  constructor(
    private filterService: FilterService,
    private searchService: SearchService,
    private logger: LoggerService,
    private router: Router,
    private paginationService: PaginationService,
    private cookieService: CookieService
  ) {}

  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  getPageSize(): number {
    return this.paginationService.getPaginationParams().pageSize;
  }

  ngOnInit(): void {
    this.loadSubscribers();
    this.loadData();
    this.checkAdminStatus();
  }

  private checkAdminStatus(): void {
    const token = this.getToken();
    if (token) {
      const decodedToken = parseJwt(token);
      this.isAdmin = !!decodedToken && decodedToken.role === ADMIN_ROLE;
      // this.isAdmin = true;
    }
  }

  private loadData(): void {
    this.searchService.searchEvents('');
  }

  private loadSubscribers(): void {
    this.searchService.searchedItems$.pipe(
      takeUntil(this.unsubscribe$),
      catchError((error) => {
        this.logger.error('Error in searchedItems$', error);
        return [];
      })
    ).subscribe((searchResults) => {
      this.items = searchResults;
      this.updatePagedItems();
    });

    this.filterService.filteredItems$.pipe(
      takeUntil(this.unsubscribe$),
      catchError((error) => {
        this.logger.error('Error in filteredItems$', error);
        return [];
      })
    ).subscribe((filteredResults) => {
      this.items = filteredResults;
      this.updatePagedItems();
    });
  }

  onPageChange(event: any): void {
    this.paginationService.setPageIndex(event.pageIndex);
    this.paginationService.setPageSize(event.pageSize);
    this.updatePagedItems();
  }

    // Get the token from the cookie
  getToken(): string | undefined {
      return this.cookieService.get(this.tokenKey);
  }

  onAddButtonClick() {
    this.router.navigate([ADD_EVENT_PAGE]);
  }

  private updatePagedItems(): void {
    const {pageSize, pageIndex} = this.paginationService.getPaginationParams()
    const startIndex = pageIndex * pageSize;
    const endIndex = startIndex + pageSize;
    this.pagedItems = this.items.slice(startIndex, endIndex);
    return;    
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }
}
