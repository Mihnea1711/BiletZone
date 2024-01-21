import { HttpErrorResponse } from "@angular/common/http";
import { CustomResponse } from "../models/responses/CustomResponse";

export function buildQueryString(params: Record<string, any>): string {
    const queryString = Object.entries(params)
      .filter(([key, value]) => value !== undefined && value !== null)
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&');
  
    return queryString.length > 0 ? `?${queryString}` : '';
}

export function buildCompleteURL(endpoint: string, query: string): string {
    return `${endpoint}${query}`;
}

export function parseErrorResponse<T>(error: any): CustomResponse<T> {
  if (error instanceof HttpErrorResponse && error.error) {
    // Assuming the error structure follows the CustomResponse interface
    const customResponse: CustomResponse<T> = {
      timestamp: error.error.timestamp || '',
      message: error.error.message || 'Unknown error',
      payload: error.error.payload || null,
    };

    return customResponse;
  } else {
    // Fallback for unexpected error structures
    return {
      timestamp: '',
      message: 'Unknown error',
      payload: null,
    };
  }
}

export function parseJwt (token: string) {
  var base64Url = token.split('.')[1];
  var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));

  return JSON.parse(jsonPayload);
}