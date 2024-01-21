export const REGISTER_ENDPOINT = "/register";
export const LOGIN_ENDPOINT = "/login";
export const HOME_ENDPOINT = "/home";

export const EVENT_PAGE = `/events/:eventID`;
export const EVENT_PAGE_BUILDER = (eventID: string) => `/events/${eventID}`;

export const  ADD_EVENT_PAGE = `/add-event`

export const GATEWAY_REGISTER_URL = "http://localhost:8080/api/register";
export const GATEWAY_LOGIN_URL = "http://localhost:8080/api/login";
export const GATEWAY_SEARCH_EVENTS = "http://localhost:8080/api/events";
export const GATEWAY_PATCH_EVENT = (eventID: string) => `http://localhost:8080/api/events/${eventID}`;

export const GATEWAY_GET_FAVORITE_EVENTS = `http://localhost:8080/api/profile/favorites`;
export const GATEWAY_ADD_EVENT_TO_FAVORITES = (eventID: string) => `http://localhost:8080/api/events/${eventID}/favorites`;
export const GATEWAY_DELETE_EVENT_FROM_FAVORITES = (eventID: string) => `http://localhost:8080/api/events/${eventID}/favorites`;

export const GATEWAY_CONFIRMATION_MAIL = "http://localhost:8080/api/confirm-email";

export const GATEWAY_GET_PROFILE_URL=`http://localhost:8080/api/profile`;
export const GATEWAY_GET_PURCHASED_TICKETS_URL=`http://localhost:8080/api/profiles/tickets`
export const GATEWAY_EDIT_PROFILE_URL =`http://localhost:8080/api/profiles`;
export const EVENT_FORUM = `/forum/:eventID`;
export const EVENT_FORUM_BUILDER = (eventID: string) => `/forum/${eventID}`;

export const GATEWAY_ADD_MESSAGE = (eventID: string) => `http://localhost:8080/api/messages/${eventID}`;
export const GATEWAY_GET_EVENT_MESSAGES = (eventID: string) => `http://localhost:8080/api/messages/events/${eventID}`;

export const GATEWAY_ADD_USER = `http://localhost:8080/api/add-user`;
export const USERS_ENDPOINT = "/users";

export const GATEWAY_GET_ALL_PROFILES = `http://localhost:8080/api/profiles`;
export const GATEWAY_DELETE_PROFILE = (id: number) => `http://localhost:8080/api/profiles/${id}`;
export const GATEWAY_DELETE_USER = (userUUID: String) => `http://localhost:8080/api/users/${userUUID}`;

export const GATEWAY_DELETE_EVENT = (id: number) => `http://localhost:8080/api/events/${id}`; 

