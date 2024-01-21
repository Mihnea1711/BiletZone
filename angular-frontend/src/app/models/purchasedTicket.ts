import { EventDto } from "./event";

export interface PurchasedTicketDto {
  purchaseId: number;
  ticketId: number;
  userUUID: string;
  quantity: number;
  eventDto: EventDto
}