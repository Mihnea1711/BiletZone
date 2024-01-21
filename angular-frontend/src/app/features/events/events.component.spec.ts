import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MatPaginatorModule } from '@angular/material/paginator';
import { of, throwError } from 'rxjs';
import { EventsComponent } from './events.component';
import { SearchService } from 'src/app/core/searchService';
import { LoggerService } from 'src/app/core/loggerService';
import { PaginationService } from 'src/app/core/paginationService';
import { EventDto } from 'src/app/models/event';
import { Router } from '@angular/router';

describe('EventsComponent', () => {
  let component: EventsComponent;
  let fixture: ComponentFixture<EventsComponent>;
  let mockSearchService: jasmine.SpyObj<SearchService>;
  let mockLoggerService: jasmine.SpyObj<LoggerService>;
  let mockPaginationService: jasmine.SpyObj<PaginationService>;
  let mockRouter: jasmine.SpyObj<Router>;

  const mockEvents: EventDto[] = [
    {
      id: 1,
      name: 'Concert Rock Fusion',
      description: 'Experimentează sunetele inovatoare ale muzicii rock fusion.',
      city: 'București',
      location: 'Sala Palatului',
      type: 'Concert',
      date: '2024-01-15T19:30:00',
      image: 'path/to/concert-image.jpg',
      isFavourite: false,
      hallMapID: { id: 1,
        rows: 10,
        columns: 15}
    },
    {
      id: 2,
      name: 'Conferință Tech Summit',
      description: 'Cele mai recente tendințe în tehnologie și inovație.',
      city: 'Cluj-Napoca',
      location: 'Centrul de Conferințe Transilvania',
      type: 'Conferință',
      date: '2024-02-10T09:00:00',
      image: 'path/to/conference-image.jpg',
      isFavourite: true,
      hallMapID: { id: 2,
        rows: 10,
        columns: 15 }
    },
  ];

  beforeEach(() => {
    mockSearchService = jasmine.createSpyObj('SearchService', ['searchEvents']);
    mockLoggerService = jasmine.createSpyObj('LoggerService', ['error']);
    mockPaginationService = jasmine.createSpyObj('PaginationService', [
      'setPageIndex',
      'setPageSize',
      'getPaginationParams'
    ]);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      declarations: [EventsComponent],
      imports: [RouterTestingModule, MatPaginatorModule],
      providers: [
        { provide: SearchService, useValue: mockSearchService },
        { provide: LoggerService, useValue: mockLoggerService },
        { provide: PaginationService, useValue: mockPaginationService },
        { provide: Router, useValue: mockRouter }
      ]
    });

    fixture = TestBed.createComponent(EventsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load data on initialization', () => {
    spyOn(component, 'loadData');
    component.ngOnInit();
    expect(component.loadData).toHaveBeenCalled();
  });

  it('should load subscribers and update paged items on search results', () => {
    mockSearchService.searchedItems$ = of(mockEvents);

    component.ngOnInit();

    expect(component.items).toEqual(mockEvents);
    expect(component.updatePagedItems).toHaveBeenCalled();
  });

  it('should handle error on search results', () => {
    const errorMessage = 'Test error';
    mockSearchService.searchedItems$ = throwError(errorMessage);

    component.ngOnInit();

    expect(mockLoggerService.error).toHaveBeenCalledWith('Error in searchedItems$', errorMessage);
    expect(component.items).toEqual([]);
  });

  it('should navigate to event details on event click', () => {
    const eventId = 123;
    component.onEventClick(eventId);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/forum', eventId]);
  });
});