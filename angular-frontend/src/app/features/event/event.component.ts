import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { EventServiceService } from 'src/app/core/event-service.service';
import ITicket from 'src/app/models/ticket.model';
import { EventDto } from 'src/app/models/eventD';
import { CustomResponse } from 'src/app/models/responses/CustomResponse';
import { TicketService } from 'src/app/core/ticket.service';
import { TicketDto } from 'src/app/models/ticket';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {
  event!: EventDto; 
  headerElement: Array<keyof ITicket> = ['ticket', 'pricing', 'quantity'];
  id!: number;
  items: (TicketDto[] | null)= [];
  name?: string;
  description?: string;
  location?: string;
  city?: string;
  date?: Date;
  image?: string;


  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private eventService: EventServiceService,
    private ticketService: TicketService
  ) {}

  ngOnInit() {
    //console.log(this.route.snapshot.paramMap.get('eventID'))
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
  }

  private setTicket(tickets:CustomResponse<TicketDto[]>){
    this.items=tickets.payload
  }

  redirectToEditEvent() { 
    this.router.navigate(['/events/'+this.id+'/edit-event']);
  }
  
  deleteEvent(){
    this.eventService.deleteEvent(this.id);
    setTimeout(() => {
      this.router.navigate(['/']);
    }, 100);
  }

  redirectToSelectTickets() {
    this.router.navigate(['/select-tickets']);
  }
}
