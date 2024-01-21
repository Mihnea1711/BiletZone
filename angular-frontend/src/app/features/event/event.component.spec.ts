import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { EventComponent } from './event.component';
import { EventServiceService } from 'src/app/core/event-service.service';
import { TicketService } from 'src/app/core/ticket.service';
import { RouterTestingModule } from '@angular/router/testing';

describe('EventComponent', () => {
  let component: EventComponent;
  let fixture: ComponentFixture<EventComponent>;
  let router: Router;
  let eventService: EventServiceService;
  let ticketService: TicketService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventComponent],
      imports: [RouterTestingModule],
      providers: [
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => 1 } } } },
        EventServiceService,
        TicketService,
      ],
    });
    fixture = TestBed.createComponent(EventComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    eventService = TestBed.inject(EventServiceService);
    ticketService = TestBed.inject(TicketService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to edit event', () => {
    spyOn(router, 'navigate');
    component.id = 1;
    component.redirectToEditEvent();

    expect(router.navigate).toHaveBeenCalledWith(['/events/1/edit-event']);
  });

 

  it('should redirect to select tickets', () => {
    spyOn(router, 'navigate');
    component.redirectToSelectTickets();

    expect(router.navigate).toHaveBeenCalledWith(['/select-tickets']);
  });
});
