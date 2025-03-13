import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';

export interface Notification {
  id: number;
  notificationId?: number;
  message: string;
  timestamp: Date;
  createdAt?: Date;
  read: boolean;
  isRead?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService implements OnDestroy {
  private notifications: Notification[] = [];

  private notificationsSubject = new BehaviorSubject<Notification[]>(this.notifications);
  private unreadCountSubject = new BehaviorSubject<number>(this.getUnreadCount());
  
  private stompClient: any;
  private serverUrl = 'http://localhost:8087/ws'; // Adjust to your notification service URL
  private userServiceUrl = 'http://localhost:8060/api/users'; // Adjust to your user service URL

  constructor(private http: HttpClient) {
    this.initializeWebSocketConnection();
  }

  private initializeWebSocketConnection(): void {
    // Get the user email from localStorage
    const userEmail = localStorage.getItem('username');
    if (!userEmail) {
      console.error('User email not found in localStorage');
      return;
    }

    // Get the userId from the user service only once
    this.getUserIdFromEmail(userEmail).subscribe({
      next: (userId) => {
        if (userId) {
          this.connectWebSocket(userId);
        } else {
          console.error('Received null or undefined user ID');
        }
      },
      error: (error) => {
        console.error('Error getting user ID:', error);
      }
    });
  }

  private getUserIdFromEmail(email: string): Observable<number> {
    const url = `${this.userServiceUrl}/getUserId/${email}`;
    return this.http.get<number>(url);
  }

  private connectWebSocket(userId: number): void {
    const socket = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(socket);
    
    // Disable STOMP debug logging
    this.stompClient.debug = null;
    
    this.stompClient.connect({}, () => {
      // Subscribe to user-specific notification queue for new notifications
      this.stompClient.subscribe(`/queue/notifications/${userId}`, (message: any) => {
        if (message.body) {
          const notification = JSON.parse(message.body);
          this.addNotification(this.mapBackendNotification(notification));
        }
      });
      
      // Subscribe to notification updates (e.g., read status changes)
      this.stompClient.subscribe(`/queue/notifications/${userId}/updates`, (message: any) => {
        if (message.body) {
          const updatedNotification = JSON.parse(message.body);
          this.updateNotification(this.mapBackendNotification(updatedNotification));
        }
      });
      
      // Subscribe to initial notifications load
      this.stompClient.subscribe(`/queue/notifications/${userId}/initial`, (message: any) => {
        if (message.body) {
          const response = JSON.parse(message.body);
          
          if (response.type === 'INITIAL_LOAD' && Array.isArray(response.notifications)) {
            // Replace all notifications with the initial set
            this.notifications = response.notifications.map((notification: any) => 
              this.mapBackendNotification(notification)
            );
            
            // Sort notifications by timestamp (newest first)
            this.notifications.sort((a, b) => 
              new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()
            );
            
            this.notificationsSubject.next([...this.notifications]);
            this.unreadCountSubject.next(this.getUnreadCount());
          }
        }
      });

      // Request initial notifications via WebSocket
      const userIdStr = userId.toString();
      const headers = { 'content-type': 'text/plain' };
      this.stompClient.send('/app/notifications/user', headers, userIdStr);
    }, (error: any) => {
      console.error('Error connecting to WebSocket:', error);
      // Retry connection after a delay
      setTimeout(() => this.connectWebSocket(userId), 5000);
    });
  }

  private mapBackendNotification(backendNotification: any): Notification {
    // Check if the notification has a read status field
    const isRead = backendNotification.isRead !== undefined ? backendNotification.isRead : backendNotification.read !== undefined ? backendNotification.read : false;
    
    return {
      id: backendNotification.notificationId,
      notificationId: backendNotification.notificationId,
      message: backendNotification.message,
      timestamp: backendNotification.createdAt ? new Date(backendNotification.createdAt) : new Date(),
      createdAt: backendNotification.createdAt ? new Date(backendNotification.createdAt) : new Date(),
      read: isRead,
      isRead: isRead
    };
  }

  private addNotification(notification: Notification): void {
    // Check if notification already exists
    const existingIndex = this.notifications.findIndex(n => n.id === notification.id);
    
    if (existingIndex !== -1) {
      // Update existing notification
      this.notifications[existingIndex] = notification;
    } else {
      // Add new notification at the beginning
      this.notifications.unshift(notification);
    }
    
    this.notificationsSubject.next([...this.notifications]);
    this.unreadCountSubject.next(this.getUnreadCount());
  }

  private updateNotification(updatedNotification: Notification): void {
    const index = this.notifications.findIndex(n => n.id === updatedNotification.id);
    if (index !== -1) {
      this.notifications[index] = updatedNotification;
      this.notificationsSubject.next([...this.notifications]);
      this.unreadCountSubject.next(this.getUnreadCount());
    } else {
      // If notification doesn't exist, add it
      this.addNotification(updatedNotification);
    }
  }

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
    if (notification) {
      // Check if notification is already read
      if (notification.read) {
        return;
      }
      
      // Update the read status locally
      notification.read = true;
      
      // Send read status update to server via WebSocket
      if (this.stompClient && this.stompClient.connected) {
        // Use proper content type and send as text
        const headers = { 'content-type': 'text/plain' };
        
        // Make sure we're sending the correct ID (notificationId from backend)
        const notificationId = notification.notificationId || notification.id;
        this.stompClient.send('/app/notification/read', headers, notificationId.toString());
      } else {
        console.error('WebSocket not connected, cannot mark notification as read');
      }
      
      // Update the UI immediately
      this.notificationsSubject.next([...this.notifications]);
      this.unreadCountSubject.next(this.getUnreadCount());
    }
  }

  markAllAsRead(): void {
    let updated = false;
    const notificationsToUpdate: Notification[] = [];
    
    this.notifications.forEach(notification => {
      if (!notification.read) {
        notification.read = true;
        notificationsToUpdate.push(notification);
        updated = true;
      }
    });
    
    if (updated && this.stompClient && this.stompClient.connected) {
      // Send read status updates to server via WebSocket
      const headers = { 'content-type': 'text/plain' };
      
      notificationsToUpdate.forEach(notification => {
        // Make sure we're sending the correct ID (notificationId from backend)
        const notificationId = notification.notificationId || notification.id;
        this.stompClient.send('/app/notification/read', headers, notificationId.toString());
      });
      
      // Update the UI immediately
      this.notificationsSubject.next([...this.notifications]);
      this.unreadCountSubject.next(0);
    } else if (updated) {
      console.error('WebSocket not connected, cannot mark all notifications as read');
    }
  }
  
  // Clean up resources when service is destroyed
  ngOnDestroy(): void {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  }

  hasNotifications(): boolean {
    return this.notifications.length > 0;
  }
} 