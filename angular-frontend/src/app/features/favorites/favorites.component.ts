import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { LoggerService } from 'src/app/core/loggerService';
import { PaginationService } from 'src/app/core/paginationService';
import { EventDto } from 'src/app/models/event';
import { DEFAULT_PAGE_SIZE_LIST } from 'src/app/utils/constants';

@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.css']
})
export class FavoritesComponent {
  items: EventDto[] = [];
  pagedItems: EventDto[] = [];

  pageSizeOptions: number[] = DEFAULT_PAGE_SIZE_LIST

  constructor(
    private logger: LoggerService,
    private router: Router,
    private paginationService: PaginationService,
    private cookieService: CookieService
  ) {}

  getPageSize(): number {
    return this.paginationService.getPaginationParams().pageSize;
  }

  onPageChange(event: any): void {
    this.paginationService.setPageIndex(event.pageIndex);
    this.paginationService.setPageSize(event.pageSize);
    this.updatePagedItems();
  }

  private updatePagedItems(): void {
    const {pageSize, pageIndex} = this.paginationService.getPaginationParams()
    const startIndex = pageIndex * pageSize;
    const endIndex = startIndex + pageSize;
    this.pagedItems = this.items.slice(startIndex, endIndex);
    return;    
  }
}


