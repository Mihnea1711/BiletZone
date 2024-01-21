import { HallMapDto } from "./hallmap";

export interface EventDto {
    id: number;
    name: string;
    description: string;
    city: string;
    location: string;
    type: string;
    date: string;
    image: string;
    isFavourite: boolean;
    hallMapID: HallMapDto;
}

export interface PatchFavouriteEvent {
    isFavourite: boolean;
}