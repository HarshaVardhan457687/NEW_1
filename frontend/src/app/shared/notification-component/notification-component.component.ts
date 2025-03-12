import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService, Notification } from '../../core/services/notification.service';
import { NotificationItemComponent } from '../notification-item/notification-item.component';

@Component({
  selector: 'app-notification-component',
  standalone: true,
  imports: [CommonModule, NotificationItemComponent],
  templateUrl: './notification-component.component.html',
  styleUrl: './notification-component.component.scss'
})
export class NotificationComponentComponent implements OnInit {
  @Output() close = new EventEmitter<void>();
  
  notifications: Notification[] = [];
  showAllNotifications: boolean = false;
  unreadCount: number = 0;

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.notificationService.getNotifications().subscribe(notifications => {
      this.notifications = notifications;
    });
    
    this.notificationService.getUnreadCountObservable().subscribe(count => {
      this.unreadCount = count;
    });
  }

  toggleShowAll(): void {
    this.showAllNotifications = !this.showAllNotifications;
  }

  markAsRead(id: number): void {
    this.notificationService.markAsRead(id);
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead();
  }

  closeNotifications(): void {
    this.close.emit();
  }

  getDisplayedNotifications(): Notification[] {
    return this.showAllNotifications 
      ? this.notifications 
      : this.notifications.slice(0, 3);
  }
}
