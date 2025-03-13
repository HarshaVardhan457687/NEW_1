import { Component, OnInit, Output, EventEmitter, ElementRef, HostListener } from '@angular/core';
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
  private isInitialized = false;

  constructor(
    private notificationService: NotificationService,
    private elementRef: ElementRef
  ) {}

  ngOnInit(): void {
    this.notificationService.getNotifications().subscribe(notifications => {
      this.notifications = notifications;
    });
    
    this.notificationService.getUnreadCountObservable().subscribe(count => {
      this.unreadCount = count;
    });
  }

  ngAfterViewInit() {
    // Delay setting the initialized flag to prevent immediate closing
    setTimeout(() => {
      this.isInitialized = true;
    }, 100);
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
