export interface CustomResponse<T> {
    timestamp: string;
    message: string;
    payload: T | null;
}