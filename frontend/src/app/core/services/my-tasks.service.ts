import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap, switchMap, catchError } from 'rxjs/operators';
import { UserRole } from './role-selection.service';


export interface TaskStatusCountDTO {
  totalTasks: number;
  completedTasks: number;
  waitingForApprovalTasks: number;
  pendingTasks: number;
}

export interface TaskDetailsDTO {
  taskId: number;
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

export interface TaskApprovalRequestDTO {
  taskId: number;
  role: string;
  id: number;
}

@Injectable({
  providedIn: 'root'
})
export class MyTasksService {
  private readonly API_URL = 'http://localhost:8060/api/users';
  private readonly APPROVAL_API_URL = 'http://localhost:8060/api/approvals';

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

  /**
   * Gets the mapped team ID for the current user and project
   * @param projectId The ID of the project
   * @returns Observable of team ID
   */
  private getMappedTeamId(projectId: number): Observable<number> {
    if (!projectId) {
      return throwError(() => new Error('Project ID is required'));
    }

    const email = localStorage.getItem('username');
    if (!email) {
      return throwError(() => new Error('User email not found'));
    }
    
    const params = new HttpParams()
      .set('userEmail', email)
      .set('projectId', projectId.toString());

    return this.http.get<number>(`${this.API_URL}/team-mapped-to-project`, { params }).pipe(
      tap(teamId => console.log('[MyTasksService] Mapped teamId:', teamId))
    );
  }

  /**
   * Submits a task for approval
   * @param taskId The ID of the task to submit
   * @param projectId The ID of the project
   * @returns Observable of the task approval response
   */
  submitTaskForApproval(taskId: number, projectId: number): Observable<any> {
    console.log(`[MyTasksService] Submitting task ${taskId} for approval`);
    
    const userRole = sessionStorage.getItem('role_selected') as UserRole;
    if (!userRole) {
      console.error('[MyTasksService] No role selected for task submission');
      return new Observable(observer => {
        observer.error('No role selected');
      });
    }

    if (userRole === 'team_leader') {
      // For team leader, use project ID directly
      const requestBody: TaskApprovalRequestDTO = {
        taskId: taskId,
        role: userRole,
        id: projectId
      };
      console.log('[MyTasksService] Submitting task as team leader:', requestBody);
      return this.http.post(`${this.APPROVAL_API_URL}/request`, requestBody).pipe(
        tap({
          next: (response) => console.log('[MyTasksService] Task submission successful:', response),
          error: (error) => console.error('[MyTasksService] Error submitting task:', error)
        }),
        catchError(error => {
          const errorMessage = error?.message || 'An error occurred while submitting the task';
          console.error('[MyTasksService] Submission error details:', error);
          return throwError(() => new Error(errorMessage));
        })
      );
    } else {
      // For team member, first get the mapped team ID
      console.log('[MyTasksService] Submitting task as team member, fetching team ID...');
      return this.getMappedTeamId(projectId).pipe(
        tap(teamId => console.log(`[MyTasksService] Got teamId: ${teamId} for submission`)),
        switchMap(teamId => {
          const requestBody: TaskApprovalRequestDTO = {
            taskId: taskId,
            role: userRole,
            id: teamId
          };
          console.log('[MyTasksService] Submitting task with team ID:', requestBody);
          return this.http.post(`${this.APPROVAL_API_URL}/request`, requestBody);
        }),
        tap({
          next: (response) => console.log('[MyTasksService] Task submission successful:', response),
          error: (error) => console.error('[MyTasksService] Error submitting task:', error)
        }),
        catchError(error => {
          const errorMessage = error?.message || 'An error occurred while submitting the task';
          console.error('[MyTasksService] Submission error details:', error);
          return throwError(() => new Error(errorMessage));
        })
      );
    }
  }
} 