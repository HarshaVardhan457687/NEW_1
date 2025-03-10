import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface TaskStatusCountDTO {
  totalTasks: number;
  completedTasks: number;
  waitingForApprovalTasks: number;
  pendingTasks: number;
}

export interface TaskDetailsDTO {
  taskName: string;
  dueDate: Date;
  taskTag: string;
  taskPriority: TaskPriority;
  taskStatus: TaskStatus;
  taskDescription: string;
}

export enum TaskPriority {
  HIGH = 'HIGH',
  MEDIUM = 'MEDIUM',
  LOW = 'LOW'
}

export enum TaskStatus {
  PENDING = 'PENDING',
  COMPLETED = 'COMPLETED',
  WAITING_FOR_APPROVAL = 'WAITING_FOR_APPROVAL',
}

@Injectable({
  providedIn: 'root'
})
export class MyTasksService {
  private readonly API_URL = 'http://localhost:8060/api/users';

  constructor(private http: HttpClient) {}

  /**
   * Fetches task status counts for the current user in a specific project
   * @param userEmail The email of the current user
   * @param projectId The ID of the project
   * @returns Observable of TaskStatusCountDTO
   */
  getTaskStatusCounts(userEmail: string, projectId: number): Observable<TaskStatusCountDTO> {
    return this.http.get<TaskStatusCountDTO>(`${this.API_URL}/user-task-status-counts`, {
      params: {
        userEmail,
        projectId: projectId.toString()
      }
    });
  }

  /**
   * Fetches task details for the current user in a specific project
   * @param userEmail The email of the current user
   * @param projectId The ID of the project
   * @returns Observable of TaskDetailsDTO array
   */
  getTasksForUser(userEmail: string, projectId: number): Observable<TaskDetailsDTO[]> {
    console.log("getTasksForUser called");
    return this.http.get<TaskDetailsDTO[]>(`${this.API_URL}/user-tasks`, {
      params: {
        userEmail,
        projectId: projectId.toString()
      }
    });
  }
} 