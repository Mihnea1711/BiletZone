import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventCardForumComponent } from './event-card-forum.component';

describe('EventCardForumComponent', () => {
  let component: EventCardForumComponent;
  let fixture: ComponentFixture<EventCardForumComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventCardForumComponent]
    });
    fixture = TestBed.createComponent(EventCardForumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
