// pagination.service.ts
import { Injectable } from '@angular/core';
import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from '../utils/constants';

@Injectable({
  providedIn: 'root',
})
export class PaginationService {
  private pageIndex: number = DEFAULT_PAGE_NUMBER;
  private pageSize: number = DEFAULT_PAGE_SIZE;

  setPageSize(pageSize: number): void {
    this.pageSize = pageSize;
  }

  setPageIndex(pageIndex: number): void {
    this.pageIndex = pageIndex;
  }

  getPaginationParams(): { pageIndex: number; pageSize: number } {
    return { pageIndex: this.pageIndex, pageSize: this.pageSize };
  }
}
