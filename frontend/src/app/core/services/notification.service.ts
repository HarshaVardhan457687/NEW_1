import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface Notification {
  id: number;
  message: string;
  timestamp: Date;
  read: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notifications: Notification[] = [
    {
      id: 1,
      message: 'Your OKR "Increase customer satisfaction" has been approved',
      timestamp: new Date(Date.now() - 15 * 60000), // 15 minutes ago
      read: false
    },
    {
      id: 2,
      message: 'Team meeting scheduled for tomorrow at 10:00 AM',
      timestamp: new Date(Date.now() - 3 * 3600000), // 3 hours ago
      read: false
    },
    {
      id: 3,
      message: 'You have completed 75% of your quarterly objectives',
      timestamp: new Date(Date.now() - 24 * 3600000), // 1 day ago
      read: true
    },
    {
      id: 4,
      message: 'New comment on your key result "Launch mobile app"',
      timestamp: new Date(Date.now() - 2 * 24 * 3600000), // 2 days ago
      read: true
    },
    {
      id: 5,
      message: 'Your manager has assigned you a new objective',
      timestamp: new Date(Date.now() - 3 * 24 * 3600000), // 3 days ago
      read: true
    }
  ];

  private notificationsSubject = new BehaviorSubject<Notification[]>(this.notifications);
  private unreadCountSubject = new BehaviorSubject<number>(this.getUnreadCount());

  constructor() {}

  getNotifications(): Observable<Notification[]> {
    return this.notificationsSubject.asObservable();
  }

  getUnreadCount(): number {
    return this.notifications.filter(notification => !notification.read).length;
  }

  getUnreadCountObservable(): Observable<number> {
    return this.unreadCountSubject.asObservable();
  }

  markAsRead(id: number): void {
    const notification = this.notifications.find(n => n.id === id);
    if (notification && !notification.read) {
      notification.read = true;
      this.notificationsSubject.next([...this.notifications]);
      this.unreadCountSubject.next(this.getUnreadCount());
    }
  }

  markAllAsRead(): void {
    let updated = false;
    this.notifications.forEach(notification => {
      if (!notification.read) {
        notification.read = true;
        updated = true;
      }
    });

    if (updated) {
      this.notificationsSubject.next([...this.notifications]);
      this.unreadCountSubject.next(0);
    }
  }
} 