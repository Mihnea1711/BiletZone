// my-tickets.component.ts
import { Component } from '@angular/core';
import { PurchasedTicketDto } from 'src/app/models/purchasedTicket';
import { ProfileServiceService } from 'src/app/core/profile-service.service';
import { CustomResponse } from 'src/app/models/responses/CustomResponse';
import { jsPDF } from 'jspdf';
interface Ticket {
  row: number;
  seat: number;
}

interface Event {
  name: string;
  date: Date;
  tickets: string;
}

@Component({
  selector: 'app-my-tickets',
  templateUrl: './my-tickets.component.html',
  styleUrls: ['./my-tickets.component.css']
})


export class MyTicketsComponent {
  

  /*allEvents: Event[] = [
    { 
      name: 'Concert Iuliana Beregoi', 
      date: new Date('2023-12-15'), 
      tickets: [
        { row: 1, seat: 3 },
        { row: 2, seat: 7 },
       
      ],
    },
    { 
      name: 'Concert Stefan Hrusca', 
      date: new Date('2023-12-20'), 
      tickets: [
        { row: 3, seat: 2 },
        { row: 4, seat: 5 },
        
      ],
    },
    { 
      name: 'Concert Alternosfera', 
      date: new Date('2023-12-3'), 
      tickets: [
        { row: 3, seat: 2 },
        { row: 4, seat: 5 },
        
      ],
    },
  ];*/

 constructor(private profileService: ProfileServiceService) {}
  userTickets: PurchasedTicketDto[] = [];
  upcomingEvents: Event[] = [];
  pastEvents: Event[] = [];

  showUpcomingEvents = true;

  ngOnInit() {
    this.filterEvents();
    this.profileService.getPurchasedTickets().subscribe((response: CustomResponse<any[]>) => {
      if (response.payload) {
        this.userTickets = response.payload;
        console.log('User Tickets:', this.userTickets);
      }
    });
  }

  toggleEvents(showUpcoming: boolean): void {
    this.showUpcomingEvents = showUpcoming;
    this.filterEvents();
  }

  downloadTickets(event: Event): void {
    // Utilizează jsPDF pentru a genera un document PDF
    const pdf = new jsPDF();
    pdf.text(`Nume eveniment: ${event.name}`, 10, 10);
    pdf.text(`Data: ${event.date}`, 10, 20);
    pdf.text(`Bilete: ${event.tickets}`, 10, 30);

    // Descarcă documentul PDF
    pdf.save('bilete.pdf');
  }

  private filterEvents(): void {
    const now = new Date();
    this.upcomingEvents = this.userTickets.filter(ticket => new Date(ticket.eventDto?.date || '1970-01-01') > now)
                                         .map(ticket => this.getEventFromTicket(ticket));
    this.pastEvents = this.userTickets.filter(ticket => new Date(ticket.eventDto?.date || '1970-01-01') <= now)
                                     .map(ticket => this.getEventFromTicket(ticket));
  }

  getEventFromTicket(ticket: PurchasedTicketDto): Event {
    return {
      name: ticket.eventDto?.name || 'Unknown Event',
      date: new Date(ticket.eventDto?.date || '1970-01-01'),
      tickets: `${ticket.quantity} bilete`,
    };
  }
}
