import { Component } from '@angular/core';
import { DatePipe } from '@angular/common';

import { DATE_FORMAT, default_cities, default_eventTypes } from "../../utils/constants"
import { FilterService } from 'src/app/core/filterService';


@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  // selectedMinPrice: number = 100; // Bind to the input field using [(ngModel)]
  // selectedMaxPrice: number = 200; // Bind to the input field using [(ngModel)]
  selectedBeforeThanDate: Date | null = null; // Initially, no date is selected
  selectedAfterThanDate: Date | null = null; // Initially, no date is selected  
  selectedCity: string = ''; // Bind to the select field using [(ngModel)]
  selectedEventType: string = ''; // Bind to the select field using [(ngModel)]

  constructor(
    private filterService: FilterService,
    private datePipe: DatePipe
  ) {}

  cities = default_cities;
  eventTypes = default_eventTypes;

  onInputBlur() {
    // Example: Perform validation when an input loses focus  
    // Handle input blur logic if needed
  }

  formatDate(date: Date | null): string {
    // Format the selectedBeforeThanDate
    return this.datePipe.transform(date, DATE_FORMAT) || '';
  }

  applyFilters() {
    // Assuming you have selectedCity and selectedEventType properties

    const formattedBeforeThanDate = this.formatDate(this.selectedBeforeThanDate);
    const formattedAfterThanDate = this.formatDate(this.selectedAfterThanDate);    

    this.filterService.applyFilters(formattedBeforeThanDate, formattedAfterThanDate, this.selectedCity, this.selectedEventType);    
  }
}
