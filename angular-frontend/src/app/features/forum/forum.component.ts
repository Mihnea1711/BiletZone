import { Component, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ForumService, Message } from 'src/app/core/forumService';
import { AuthService } from 'src/app/core/authService';
import {Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { LOGIN_ENDPOINT } from 'src/app/utils/endpoints';
import { ToastService } from 'src/app/core/toastService';
import { CookieService } from 'ngx-cookie-service';
import { JWT_COOKIE_NAME } from 'src/app/utils/constants';

@Component({
  selector: 'app-forum',
  templateUrl: './forum.component.html',
  styleUrls: ['./forum.component.css'],
})
export class ForumComponent implements OnDestroy {
  eventId?: string;
  messages: Message[] = [];
  newMessage: string = '';

  private unsubscribe$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private messageService: ForumService,
    private authService: AuthService,
    private toastr: ToastService,
    private cookieService: CookieService
  ) {}

  private getJwtFromCookies(): string | null {
    return this.cookieService.get(JWT_COOKIE_NAME);
  }

  ngOnInit(): void {
    const jwt = this.getJwtFromCookies();
    if(!jwt)
    {
      this.router.navigate([LOGIN_ENDPOINT]);
      this.toastr.showError('Please log in to access this feature.');
    }
    else{
      this.eventId = this.route.snapshot.paramMap.get('eventId') || '';
      this.loadMessages();
    }
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  private loadMessages(): void {
    if(this.eventId)
    {
      this.messageService.getMessagesForEvent(this.eventId)
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((response: any) => {
        if (Array.isArray(response) && response.length > 0) {
          this.messages = response;
        } else {
          this.messages = [];
        }
      });
    }
  }

  addMessage(): void {
    if(this.eventId)
    {
      this.messageService.addMessageForEvent(this.eventId, this.newMessage)
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe((response: any) => {
          if (response.message === 'Message was added successfully') {
            this.newMessage = '';
            this.loadMessages();
          } else {
            // Handle errors or show appropriate messages
          }
        });
    }
  }
}