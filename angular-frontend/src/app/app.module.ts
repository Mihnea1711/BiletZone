import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { ToastrModule } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { MatPaginatorModule } from '@angular/material/paginator';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { DatePipe } from '@angular/common';

import { CookieService } from 'ngx-cookie-service';
import { AuthService } from './core/authService';
import { SearchService } from './core/searchService';
import { FilterService } from './core/filterService';

import { AppComponent } from './app.component';
import { RegisterComponent } from './features/register/register.component';
import { LoginComponent } from './features/login/login.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { FooterComponent } from './shared/footer/footer.component';
import { NavtabsComponent } from './shared/navtabs/navtabs.component';
import { EventComponent } from './features/event/event.component';
import { AddEventComponent } from './features/add-event/add-event.component';
import { EditEventComponent } from './features/edit-event/edit-event.component';
import { SidebarComponent } from './shared/sidebar/sidebar.component';
import { MainComponent } from './features/main/main.component';
import { EventCardComponent } from './shared/event-card/event-card.component';
import { FaqComponent } from './features/faq/faq.component';
import { EventsComponent } from './features/events/events.component';
import { UsersComponent } from './features/users/users.component';
import { ForumComponent } from './features/forum/forum.component';
import { AddUserComponent } from './features/add-user/add-user.component';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MyProfileComponent } from './features/my-profile/my-profile.component';
import { SelectTicketsComponent } from './features/select-tickets/select-tickets.component';
import { MyTicketsComponent } from './features/my-tickets/my-tickets.component';
import { EditProfileComponent } from './features/edit-profile/edit-profile.component';
import { ConfirmEmailComponent } from './features/confirm-email/confirm-email.component';
import { EventCardForumComponent } from './shared/event-card-forum/event-card-forum.component';
import { FavoritesComponent } from './features/favorites/favorites.component';
import { MatTooltipModule } from '@angular/material/tooltip';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    NavbarComponent,
    FooterComponent,
    NavtabsComponent,
    SidebarComponent,
    MainComponent,
    EventCardComponent,
    FaqComponent,
    EventsComponent,
    UsersComponent,
    ForumComponent,
    AddUserComponent,
    MyProfileComponent,
    SelectTicketsComponent,
    MyTicketsComponent,
    EditProfileComponent,
    EventComponent,
    AddEventComponent,
    EditEventComponent,
    ConfirmEmailComponent,
    EventCardForumComponent,
    FavoritesComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule, BrowserAnimationsModule,
    NgbModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    MatIconModule,
    MatButtonModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatPaginatorModule,
    MatTooltipModule,
    HttpClientModule,
    ToastrModule.forRoot({
      positionClass: 'toast-top-right',
      progressBar: true,
      preventDuplicates: true,
    }),
  ],
  providers: [
    CookieService,
    AuthService,
    SearchService,
    FilterService,
    DatePipe,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
