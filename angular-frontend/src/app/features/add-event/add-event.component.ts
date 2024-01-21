import { identifierName } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { tick } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { EventServiceService } from 'src/app/core/event-service.service';
import { TicketService } from 'src/app/core/ticket.service';
import { EventDto } from 'src/app/models/eventD';
import { TicketDto } from 'src/app/models/ticket';

@Component({
  selector: 'app-add-event',
  templateUrl: './add-event.component.html',
  styleUrls: ['./add-event.component.css']
})
export class AddEventComponent {
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private eventService: EventServiceService,
    private ticketService: TicketService
  ) {}
  
  headers: string[] = ['Ticket', 'Price', 'Quantity'];
  data: string[][] = [];
  editingCells: boolean[][] = [];
  newTicket: string = '';
  newPrice: string = '';
  newQuantity: string = '';

  name: string = '';
  description: string = '';
  city: string = '';
  location: string = '';
  date: Date = new Date;
  type: string = '';
  image: string = '';
  tickets: TicketDto[] = [];
  eventID: number | undefined= undefined;
  

  addRow() {
    const newRow: string[] = [this.newTicket, this.newPrice, this.newQuantity];
    var ticket: TicketDto = new TicketDto;
    ticket.name=this.newTicket;
    ticket.price= this.newPrice;
    ticket.quantity = +this.newQuantity;
    this.tickets.push(ticket)
    this.data.push(newRow);
    this.editingCells.push(Array(this.headers.length).fill(false));
    this.clearNewRowInputs();
  }


  deleteRow(index: number): void {
    this.data.splice(index, 1);
    this.editingCells.splice(index, 1);
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

  async saveEvent() {
    var eventData : EventDto = {
      name: this.name,
      description: this.description,
      city: this.city,
      location: this.location,
      date: this.date,
      type: this.type,
      image: this.image,
    };


    console.log(eventData);
    console.log(this.tickets);
 
  await this.eventService.addEvent(eventData).subscribe(
  response => {
    console.log('Event added successfully:', response);
    
    this.eventID = response.payload?.id;
    this.tickets.map(ticket =>{
      ticket.eventID = this.eventID;
      console.log(ticket);
      this.ticketService.addTicket(ticket).subscribe( res =>{
        console.log("Ticket added succesfully:", res)
      });
    });
  });
  this.router.navigate(['/']);

}
}