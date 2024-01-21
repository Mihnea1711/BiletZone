import { HallMapDto } from "./hallmap";
import { TicketDto } from "./ticket";

export type EventAddDto = {
    id?: number;
    name: string;
    description: string;
    city: string;
    location: string;
    type: string;
    date: Date;
    image: string;
    hallMapID: HallMapDto;
    tickets: TicketDto[];
}
export type Events = EventAddDto[];
