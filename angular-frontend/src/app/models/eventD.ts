
export type EventDto = {
    id?: number;
    name: string;
    description: string;
    city: string;
    location: string;
    type: string;
    date: Date;
    image: string;
  
}
export type Events = EventDto[];
