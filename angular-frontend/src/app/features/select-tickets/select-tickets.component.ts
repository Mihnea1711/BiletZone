// select-tickets.component.ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-select-tickets',
  templateUrl: './select-tickets.component.html',
  styleUrls: ['./select-tickets.component.css']
})
export class SelectTicketsComponent {
  rows: number[] = [1, 2, 3, 4, 5, 7];
  seats: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  selectedSeats: string[] = [];
  ticketLimitReached: boolean = false;

  selectSeat(row: number, seat: number): void {
    const selectedSeat = `Rand ${row} - Loc ${seat}`;

      // Check if the maximum limit is reached
      if (this.selectedSeats.length >= 10 && !this.isSelected(row, seat)) {
        this.ticketLimitReached = true;
        return;
      }

    // Toggle seat selection
    if (this.selectedSeats.includes(selectedSeat)) {
      this.selectedSeats = this.selectedSeats.filter(s => s !== selectedSeat);
    } else {
      this.selectedSeats.push(selectedSeat);
    }
    
    // Reset the ticketLimitReached flag
    this.ticketLimitReached = false;
  }

  buyTickets(): void {
    console.log('Bought tickets for seats:', this.selectedSeats);
  }

  // New method to check if a seat is selected
  isSelected(row: number, seat: number): boolean {
    const selectedSeat = `Rand ${row} - Loc ${seat}`;
    return this.selectedSeats.includes(selectedSeat);
  }
}