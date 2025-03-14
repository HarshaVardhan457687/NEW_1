import { Component, OnInit, OnDestroy, Output, EventEmitter, ElementRef, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService, Notification } from '../../core/services/notification.service';
import { NotificationItemComponent } from '../notification-item/notification-item.component';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-notification-component',
  standalone: true,
  imports: [CommonModule, NotificationItemComponent],
  templateUrl: './notification-component.component.html',
  styleUrl: './notification-component.component.scss'
})
export class NotificationComponentComponent implements OnInit, OnDestroy {
  @Output() close = new EventEmitter<void>();
  
  notifications: Notification[] = [];
  showAllNotifications: boolean = false;
  unreadCount: number = 0;
  private isInitialized = false;
  private subscriptions: Subscription[] = [];

  constructor(
    private notificationService: NotificationService,
    private elementRef: ElementRef
  ) {}

  ngOnInit(): void {
    
    
    const notificationsSub = this.notificationService.getNotifications().subscribe(notifications => {
      console.log('Notifications received in component:', notifications);
      this.notifications = notifications;
    });
    
    const unreadCountSub = this.notificationService.getUnreadCountObservable().subscribe(count => {
      console.log('Unread count updated:', count);
      this.unreadCount = count;
    });

    this.subscriptions.push(notificationsSub, unreadCountSub);
  }

  ngAfterViewInit() {
    // Delay setting the initialized flag to prevent immediate closing
    setTimeout(() => {
      this.isInitialized = true;
    }, 100);
  }

  ngOnDestroy(): void {
    // Unsubscribe from all subscriptions
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: Event) {
    // Only process click outside events after initialization
    if (this.isInitialized && !this.elementRef.nativeElement.contains(event.target)) {
      this.close.emit();
    }
  }

  @HostListener('click', ['$event'])
  onClick(event: Event) {
    // Prevent clicks within the component from bubbling up
    event.stopPropagation();
  }

  toggleShowAll(): void {
    this.showAllNotifications = !this.showAllNotifications;
  }

  markAsRead(id: number): void {
    console.log('Marking notification as read:', id);
    this.notificationService.markAsRead(id);
  }

  markAsReadIfUnread(notification: Notification): void {
    if (!notification.read) {
      this.markAsRead(notification.id);
    }
  }

  markAllAsRead(): void {
    console.log('Marking all notifications as read');
    this.notificationService.markAllAsRead();
  }

  closeNotifications(): void {
    this.close.emit();
  }

  getDisplayedNotifications(): Notification[] {
    const notifications = this.showAllNotifications 
      ? this.notifications 
      : this.notifications.slice(0, 3);
    console.log('Displaying notifications:', notifications);
    return notifications;
  }

  hasNotifications(): boolean {
    return this.notifications.length > 0;
  }
}
