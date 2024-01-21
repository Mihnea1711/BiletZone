import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { DEFAULT_IMAGE_LINK, JWT_COOKIE_NAME } from 'src/app/utils/constants';
import { EVENT_FORUM_BUILDER} from 'src/app/utils/endpoints';

@Component({
  selector: 'app-event-card-forum',
  templateUrl: './event-card-forum.component.html',
  styleUrls: ['./event-card-forum.component.css']
})
export class EventCardForumComponent implements OnInit{

  @Input() id: number = 0;
  @Input() name: string = '';
  @Input() image: string = '';
  @Input() type: string = '';
  @Input() isFavourite: boolean = false;

  // Define a pattern for a valid image link (you can adjust this as needed)
  private imageLinkPattern = /^(http(s?):\/\/)(www\.)?[\w-]+(\.[\w-]+)+([\w.,@?^=%&:/~+#-]*[\w@?^=%&/~+#-])?$/;
  
  constructor(
    private router: Router,
    private cookieService: CookieService,
  ) {}

  getJwtFromCookie(): string | null {
    return this.cookieService.get(JWT_COOKIE_NAME);
  }

  ngOnInit(): void {
    // Set default image link if not provided or is not valid
    if (!this.isValidImageLink(this.image)) {
      this.image = DEFAULT_IMAGE_LINK;
    }    
  }

  handleClick(event: Event) {
    // Get the id attribute of the clicked element
    const clickedElement = event.target as HTMLElement;
    const iconId = clickedElement.getAttribute('id');

    if (this.id !== 0) {
      // You can construct the route or path dynamically based on your requirements
      const eventForumRoute = EVENT_FORUM_BUILDER(encodeURIComponent(this.id.toString()));
      // Navigate to the specified route
      this.router.navigate([eventForumRoute]);
    }
  }
  // Custom validator function to check if the image link is valid
  private isValidImageLink(link: string): boolean {
    return this.imageLinkPattern.test(link);
  }
}

