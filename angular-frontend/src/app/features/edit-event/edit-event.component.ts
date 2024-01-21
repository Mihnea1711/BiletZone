import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventServiceService } from 'src/app/core/event-service.service';
import { TicketService } from 'src/app/core/ticket.service';
import { EventDto } from 'src/app/models/eventD';
import { CustomResponse } from 'src/app/models/responses/CustomResponse';
import { TicketDto } from 'src/app/models/ticket';


@Component({
  selector: 'app-edit-event',
  templateUrl: './edit-event.component.html',
  styleUrls: ['./edit-event.component.css']
})
export class EditEventComponent implements OnInit {

  
  headers: string[] = ['Ticket', 'Price', 'Quantity'];
  data: string[][] = [];
  editingCells: boolean[][] = [];
  newTicket: string = '';
  newPrice: string = '';
  newQuantity: string = '';
  id!: number;
  items: (TicketDto[] | null)= [];
  name?: string;
  description?: string;
  location?: string;
  city?: string;
  date?: Date;
  image?: string;
  type?: string;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private eventService: EventServiceService,
    private ticketService: TicketService
  ){}
  
  ngOnInit(): void {
    this.id = +(this.route.snapshot.paramMap.get('eventID') as string);
    console.log(this.id) 
    this.eventService.getEvent(this.id).subscribe((event) =>{
      console.log(event.payload)
      this.setEvent(event)
    });
    this.ticketService.getTickets(this.id).subscribe((ticket) =>{
      console.log(ticket.payload)
      this.setTicket(ticket)
    })
  }

  private setEvent(event: CustomResponse<EventDto> | null){
    this.name=event?.payload?.name;
    this.date=event?.payload?.date;
    this.location=event?.payload?.location;
    this.city=event?.payload?.city;
    this.description=event?.payload?.description;
    this.image =event?.payload?.image;
    this.type =  event?.payload?.type;
  }

  private setTicket(tickets:CustomResponse<TicketDto[]>){
    this.items=tickets.payload
  }

    addRow() {
      const newRow: string[] = [this.newTicket, this.newPrice, this.newQuantity];
      this.data.push(newRow);
      this.editingCells.push(Array(this.headers.length).fill(false));
      this.clearNewRowInputs();
    }
  
  
    deleteRow(index: number): void {
      //this.items != this.items?.filter(item => item.id !== index);
      console.log(this.items);
      console.log(index);
      this.ticketService.deleteTicket(index);
      this.data.splice(index, 1);
      this.editingCells.splice(index, 1);
      
      setTimeout(() => {
        //this.router.navigate(['/']);
      }, 100);
    }
    
  
    editingCell(row: number, col: number): boolean {
      return this.editingCells[row][col];
    }
  
    startEditingCell(row: number, col: number): void {
      this.editingCells[row][col] = true;
    }
  
    stopEditingCell(row: number, col: number): void {
      this.editingCells[row][col] = false;
    }
  
    private clearNewRowInputs(): void {
      this.  newTicket = '';
      this.newPrice = '';
      this.newQuantity = '';
    }
    redirectToEvent() {
      this.router.navigate(['/event']); 
    }
  }

