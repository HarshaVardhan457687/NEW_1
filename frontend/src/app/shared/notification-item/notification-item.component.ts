import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-notification-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification-item.component.html',
  styleUrl: './notification-item.component.scss'
})
export class NotificationItemComponent implements OnChanges {
  @Input() message: string = '';
  @Input() timestamp: Date = new Date();
  @Input() read: boolean = false;
  
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['read']) {
      console.log('Notification read status changed:', this.read);
    }
  }
  
  getTimeAgo(): string {
    const now = new Date();
    const diff = now.getTime() - this.timestamp.getTime();
    
    // Convert to minutes
    const minutes = Math.floor(diff / 60000);
    
    if (minutes < 1) {
      return 'Just now';
    } else if (minutes < 60) {
      return `${minutes} minute${minutes > 1 ? 's' : ''} ago`;
    } else if (minutes < 1440) { // Less than a day
      const hours = Math.floor(minutes / 60);
      return `${hours} hour${hours > 1 ? 's' : ''} ago`;
    } else {
      const days = Math.floor(minutes / 1440);
      return `${days} day${days > 1 ? 's' : ''} ago`;
    }
  }
}
