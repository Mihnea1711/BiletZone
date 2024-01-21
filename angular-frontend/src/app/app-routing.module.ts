import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './features/register/register.component'; // Adjust the path accordingly
import { LoginComponent } from './features/login/login.component';
import { EventComponent } from './features/event/event.component';
import { AddEventComponent } from './features/add-event/add-event.component';
import { EditEventComponent } from './features/edit-event/edit-event.component';

import { MainComponent } from './features/main/main.component';
import { FaqComponent } from './features/faq/faq.component';
import { EventsComponent } from './features/events/events.component';
import { UsersComponent } from './features/users/users.component';
import { ForumComponent } from './features/forum/forum.component';
import { AddUserComponent } from './features/add-user/add-user.component';
import { MyProfileComponent } from './features/my-profile/my-profile.component';
import { SelectTicketsComponent } from './features/select-tickets/select-tickets.component';
import { MyTicketsComponent } from './features/my-tickets/my-tickets.component';
import { EditProfileComponent } from './features/edit-profile/edit-profile.component';
import { ConfirmEmailComponent } from './features/confirm-email/confirm-email.component';
import { FavoritesComponent } from './features/favorites/favorites.component';


const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: MainComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'confirm-email', component: ConfirmEmailComponent },
  { path: 'events/:eventID', component: EventComponent },
  { path: 'favorites', component: FavoritesComponent },
  { path: 'add-event', component: AddEventComponent },
  { path: 'events/:eventID/edit-event', component:  EditEventComponent },
  { path: 'faq', component: FaqComponent },
  { path: 'events-forum', component: EventsComponent },
  { path: 'users', component: UsersComponent },
  { path: 'forum/:eventId', component: ForumComponent },
  { path: 'add-user', component: AddUserComponent },
  { path: 'my-profile', component: MyProfileComponent },
  { path: 'select-tickets', component: SelectTicketsComponent },
  { path: 'my-tickets', component: MyTicketsComponent },
  { path: 'edit-profile', component: EditProfileComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
