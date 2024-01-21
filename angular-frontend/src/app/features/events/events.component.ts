import { Component, OnDestroy } from '@angular/core';
import { EventDto } from 'src/app/models/event';
import { LoggerService } from 'src/app/core/loggerService';
import { PaginationService } from 'src/app/core/paginationService';
import { SearchService } from 'src/app/core/searchService';
import { Subject } from 'rxjs';
import { takeUntil, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnDestroy{
  items: EventDto[] = [];

  pagedItems: EventDto[] = [];
  private unsubscribe$ = new Subject<void>();

  pageSize = 5; // set your desired page size
  pageSizeOptions: number[] = [5, 10, 25]; // set your desired page size options

  constructor(
    private logger: LoggerService,
    private searchService: SearchService,
    private paginationService: PaginationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadSubscribers();
    this.loadData()
  }

  public loadData(): void {
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
  }

  onPageChange(event: any): void {
    this.paginationService.setPageIndex(event.pageIndex);
    this.paginationService.setPageSize(event.pageSize);
    this.updatePagedItems();
  }

  getPageSize(): number {
    return this.paginationService.getPaginationParams().pageSize;
  }

  public updatePagedItems(): void {
    // this.searchService.searchEvents('');
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

  onEventClick(eventId: number): void {
    // Navigare către /forum/idEveniment
    this.router.navigate(['/forum', eventId]);
  }
}
