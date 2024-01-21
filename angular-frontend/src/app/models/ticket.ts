import { EventDto } from "./eventD";
export class TicketDto  {
    id?: number;
    name?: string;
    price?: string;
    quantity?: number;
    eventID?: number;
}
export type Tickets = TicketDto[];