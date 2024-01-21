import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ForumComponent } from './forum.component';
import { ForumService } from 'src/app/core/forumService';
import { AuthService } from 'src/app/core/authService';
import { ToastService } from 'src/app/core/toastService';
import { CookieService } from 'ngx-cookie-service';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { CustomResponse } from 'src/app/models/responses/CustomResponse';

describe('ForumComponent', () => {
  let component: ForumComponent;
  let fixture: ComponentFixture<ForumComponent>;
  let forumServiceSpy: jasmine.SpyObj<ForumService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let toastServiceSpy: jasmine.SpyObj<ToastService>;
  let cookieServiceSpy: jasmine.SpyObj<CookieService>;

  beforeEach(() => {
    forumServiceSpy = jasmine.createSpyObj('ForumService', ['getMessagesForEvent', 'addMessageForEvent']);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['']);
    toastServiceSpy = jasmine.createSpyObj('ToastService', ['']);
    cookieServiceSpy = jasmine.createSpyObj('CookieService', ['get']);

    TestBed.configureTestingModule({
      declarations: [ForumComponent],
      imports: [RouterTestingModule],
      providers: [
        { provide: ForumService, useValue: forumServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: ToastService, useValue: toastServiceSpy },
        { provide: CookieService, useValue: cookieServiceSpy },
      ],
    });

    fixture = TestBed.createComponent(ForumComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to login if no JWT cookie is found', () => {
    cookieServiceSpy.get.and.returnValue('');
    const router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
    expect(toastServiceSpy.showError).toHaveBeenCalledWith('Please log in to access this feature.');
  });

  it('should load messages on init if JWT cookie is present', () => {
    const mockMessages = [{ messageText: 'Hello, world!' }];
    cookieServiceSpy.get.and.returnValue('mockJWT');
    expect(forumServiceSpy.getMessagesForEvent).toHaveBeenCalledOnceWith(jasmine.any(String));

    component.ngOnInit();

    expect(component.messages).toEqual(jasmine.arrayContaining(mockMessages));
    expect(forumServiceSpy.getMessagesForEvent).toHaveBeenCalledOnceWith(jasmine.any(String));
  });

  it('should add a message and reload messages', () => {
    const mockResponse = { message: 'Message was added successfully' };
    forumServiceSpy.addMessageForEvent.and.returnValue(of({ message: 'Message was added successfully' } as CustomResponse<any>));
    spyOn(component, 'loadMessages' as any);

    component.eventId = 'mockEventId';
    component.newMessage = 'New message';
    component.addMessage();

    expect(forumServiceSpy.addMessageForEvent).toHaveBeenCalledOnceWith('mockEventId', 'New message');
    expect(component.newMessage).toEqual('');
    spyOn(component, 'loadMessages' as any);
  });
});