import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { AddEventComponent } from './add-event.component';
import { EventServiceService } from 'src/app/core/event-service.service';
import { TicketService } from 'src/app/core/ticket.service';


describe('AddEventComponent', () => {
  let component: AddEventComponent;
  let fixture: ComponentFixture<AddEventComponent>;
  let router: Router;
  let eventService: EventServiceService;
  let ticketService: TicketService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddEventComponent],
      imports: [RouterTestingModule, FormsModule],
      providers: [
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => 1 } } } },
        EventServiceService,
        TicketService,
      ],
    });
    fixture = TestBed.createComponent(AddEventComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    eventService = TestBed.inject(EventServiceService);
    ticketService = TestBed.inject(TicketService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should add a row and clear new row inputs', () => {
    component.newTicket = 'Ticket1';
    component.newPrice = '10';
    component.newQuantity = '5';

    component.addRow();

    expect(component.data.length).toBe(1);
    expect(component.tickets.length).toBe(1);
    expect(component.editingCells.length).toBe(1);
    expect(component.newTicket).toBe('');
    expect(component.newPrice).toBe('');
    expect(component.newQuantity).toBe('');
  });

  it('should delete a row', () => {
    component.data = [['Ticket1', '10', '5'], ['Ticket2', '20', '10']];
    component.editingCells = [[false, false, false], [false, false, false]];

    component.deleteRow(1);

    expect(component.data.length).toBe(1);
    expect(component.editingCells.length).toBe(1);
    expect(component.data[0]).toEqual(['Ticket1', '10', '5']);
    expect(component.editingCells[0]).toEqual([false, false, false]);
  });

  it('should start and stop editing cell', () => {
    component.editingCells = [[false, false, false], [false, false, false]];

    component.startEditingCell(1, 2);

    expect(component.editingCell(1, 2)).toBe(true);

    component.stopEditingCell(1, 2);

    expect(component.editingCell(1, 2)).toBe(false);
  });
});
