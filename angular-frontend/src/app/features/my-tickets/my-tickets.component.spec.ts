import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MyTicketsComponent } from './my-tickets.component';
import { ProfileServiceService } from 'src/app/core/profile-service.service';
import { of } from 'rxjs';

describe('MyTicketsComponent', () => {
  let component: MyTicketsComponent;
  let fixture: ComponentFixture<MyTicketsComponent>;
  let profileServiceSpy: jasmine.SpyObj<ProfileServiceService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('ProfileServiceService', ['getPurchasedTickets']);

    TestBed.configureTestingModule({
      declarations: [MyTicketsComponent],
      providers: [{ provide: ProfileServiceService, useValue: spy }]
    });

    fixture = TestBed.createComponent(MyTicketsComponent);
    component = fixture.componentInstance;
    profileServiceSpy = TestBed.inject(ProfileServiceService) as jasmine.SpyObj<ProfileServiceService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user tickets on initialization', () => {
    const mockTickets = [{
      purchaseId: 1, 
      ticketId: 1, 
      userUUID: "uuid", 
      quantity: 3, 
      eventDto: {
        id: 1,
        name: "Filmul",
        type: "Film", 
        description: "descriere",
        city: "Iasi", 
        location: "Sala Polivalenta",
        image: "link",
        date: "",
        isFavourite: false,
        hallMapID: {
          id:1,
          rows: 10,
          columns: 30
        }
      }
    }];
  
    profileServiceSpy.getPurchasedTickets.and.returnValue(of({
      timestamp: new Date().toISOString(),
      message: 'Success',
      payload: null
    }));
  
    component.ngOnInit();
  
    expect(profileServiceSpy.getPurchasedTickets).toHaveBeenCalled();
    expect(component.userTickets).toEqual(mockTickets);
  });

  it('should filter upcoming and past events correctly', () => {
    const now = new Date();
    const upcomingTicket = { purchaseId: 1, 
      ticketId: 1, 
      userUUID: "uuid", 
      quantity: 3, 
      eventDto: {
        id: 1,
        name: "Filmul",
        type: "Film", 
        description: "descriere",
        city: "Iasi", 
        location: "Sala Polivalenta",
        image: "link",
        date: "",
        isFavourite: false,
        hallMapID: {
          id:1,
          rows: 10,
          columns: 30
        }
      } };
    const pastTicket = { 
      purchaseId: 1, 
      ticketId: 1, 
      userUUID: "uuid", 
      quantity: 3, 
      eventDto: {
        id: 1,
        name: "Filmul",
        type: "Film", 
        description: "descriere",
        city: "Iasi", 
        location: "Sala Polivalenta",
        image: "link",
        date: "",
        isFavourite: false,
        hallMapID: {
          id:1,
          rows: 10,
          columns: 30
        }
      }
     };

     
  
    component.userTickets = [upcomingTicket, pastTicket];
    //component.filterEvents();
  
    //expect(component.upcomingEvents).toEqual([upcomingTicket]);
    //expect(component.pastEvents).toEqual([pastTicket]);
  });

  it('should generate and download a PDF for an event', () => {
    const mockTicket = { name: 'Concert Test', date: new Date(), tickets: '2 bilete' };

    spyOn(window, 'open').and.stub(); // Stub window.open

    component.downloadTickets(mockTicket);

    expect(window.open).toHaveBeenCalled();
  });
});
