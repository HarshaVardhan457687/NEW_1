# Notification Service with WebSocket Support

This service provides real-time notifications to users through WebSockets.

## Architecture

The notification system uses a combination of REST APIs and WebSockets:

1. **REST APIs** - For CRUD operations on notifications
2. **WebSockets** - For real-time delivery of notifications to users

## WebSocket Configuration

The WebSocket configuration is defined in `WebSocketConfig.java`:

- Endpoint: `/ws` - Clients connect to this endpoint with SockJS
- Message broker prefixes:
  - `/topic` - For broadcast messages
  - `/queue` - For user-specific messages
- Application destination prefix: `/app`
- User destination prefix: `/user`

## User-Specific Notifications

Each user subscribes to their own notification queue:

```
/queue/notifications/{userId}
```

Updates to notification status (e.g., marking as read) are sent to:

```
/queue/notifications/{userId}/updates
```

## Frontend Integration

The frontend connects to the WebSocket server and subscribes to the user-specific queue:

1. Get the user's email from localStorage (`username`)
2. Call the User Service to get the userId from the email
3. Connect to the WebSocket server
4. Subscribe to the user-specific notification queue
5. Handle incoming notifications in real-time

## Message Flow

1. **Creating a notification**:
   - REST API: `POST /api/notifications`
   - WebSocket: Send to `/app/notification`

2. **Marking a notification as read**:
   - REST API: `POST /api/notifications/{notificationId}/read`
   - WebSocket: Send to `/app/notification/read`

3. **Getting notifications for a user**:
   - REST API: `GET /api/notifications/user?userId={userId}`

## Security Considerations

- WebSocket connections are restricted to the Angular frontend origin (`http://localhost:4200`)
- User authentication should be implemented to secure WebSocket connections
- Consider adding CSRF protection for WebSocket connections 