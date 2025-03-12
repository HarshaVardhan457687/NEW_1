import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { switchMap, tap, catchError } from 'rxjs/operators';
import { TaskPriority } from './my-tasks.service';

export enum ApprovalStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}

export interface TaskApproval {
  id: number;
  taskId: number;
  status: ApprovalStatus;
  role: string;
  approverId: number;
}

export interface TaskApprovalResponseDTO {
  taskApprovalId: number;
  taskName: string;
  taskTag: string;
  taskId: number;
  ownerName: string;
  taskPriority: TaskPriority;
  taskDescription: string;
  taskDueDate: Date;
  approvalStatus: ApprovalStatus;
  approvalDate: Date;
  submittedDate: Date;
}

@Injectable({
  providedIn: 'root'
})
export class ApprovalsService {
  private readonly APPROVAL_API_URL = 'http://localhost:8060/api/approvals';
  private readonly USER_API_URL = 'http://localhost:8060/api/users';

  constructor(private http: HttpClient) {}

  /**
   * Gets approvals based on user role and project context
   * @param projectId The ID of the project
   * @returns Observable of TaskApprovalResponseDTO array
   */
  getApprovals(projectId: number): Observable<TaskApprovalResponseDTO[]> {
    const userRole = sessionStorage.getItem('role_selected');
    
    if (!userRole) {
      return throwError(() => new Error('No role selected'));
    }

    if (userRole === 'project_manager') {
      return this.getApprovalsForManager(projectId);
    } else if (userRole === 'team_leader') {
      return this.getMappedTeamId(projectId).pipe(
        switchMap(teamId => this.getApprovalsForLeader(teamId))
      );
    } else {
      return throwError(() => new Error('Invalid role for approvals'));
    }
  }

  /**
   * Gets approvals for project manager
   * @param projectId The ID of the project
   * @returns Observable of TaskApprovalResponseDTO array
   */
  private getApprovalsForManager(projectId: number): Observable<TaskApprovalResponseDTO[]> {
    if (!projectId) {
      return throwError(() => new Error('Project ID is required'));
    }

    // Log the request details
    console.log(`[ApprovalsService] Fetching approvals for project ${projectId}`);

    return this.http.get<TaskApprovalResponseDTO[]>(`${this.APPROVAL_API_URL}/project`, {
      params: new HttpParams().set('projectId', projectId.toString())
    }).pipe(
      tap(approvals => console.log('[ApprovalsService] Manager approvals:', approvals)),
      catchError(error => {
        console.error('[ApprovalsService] Error fetching manager approvals:', error);
        if (error.status === 404) {
          return throwError(() => new Error('No approvals found for this project'));
        }
        return throwError(() => new Error('Failed to fetch approvals'));
      })
    );
  }

  /**
   * Gets approvals for team leader
   * @param teamId The ID of the team
   * @returns Observable of TaskApprovalResponseDTO array
   */
  private getApprovalsForLeader(teamId: number): Observable<TaskApprovalResponseDTO[]> {
    if (!teamId) {
      return throwError(() => new Error('Team ID is required'));
    }

    // Log the request details
    console.log(`[ApprovalsService] Fetching approvals for team ${teamId}`);

    return this.http.get<TaskApprovalResponseDTO[]>(`${this.APPROVAL_API_URL}/team`, {
      params: new HttpParams().set('teamId', teamId.toString())
    }).pipe(
      tap(approvals => console.log('[ApprovalsService] Leader approvals:', approvals)),
      catchError(error => {
        console.error('[ApprovalsService] Error fetching leader approvals:', error);
        if (error.status === 404) {
          return throwError(() => new Error('No approvals found for this team'));
        }
        return throwError(() => new Error('Failed to fetch approvals'));
      })
    );
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

    return this.http.get<number>(`${this.USER_API_URL}/team-mapped-to-project`, { params }).pipe(
      tap(teamId => console.log('[ApprovalsService] Mapped teamId:', teamId))
    );
  }

  /**
   * Approves a task approval request
   * @param approvalId The ID of the approval to approve
   * @param increment The increment value
   * @returns Observable of the updated TaskApproval
   */
  approveTask(approvalId: number, increment: number): Observable<TaskApproval> {
    if (!approvalId) {
      return throwError(() => new Error('Approval ID is required'));
    }

    console.log(`[ApprovalsService] Approving task approval ${approvalId} with increment ${increment}`);
    
    return this.http.post<TaskApproval>(`${this.APPROVAL_API_URL}/approve`, null, {
      params: new HttpParams()
        .set('approvalId', approvalId.toString())
        .set('increment', increment.toString())
    }).pipe(
      tap(response => console.log('[ApprovalsService] Task approval successful:', response)),
      catchError(error => {
        console.error('[ApprovalsService] Error approving task:', error);
        return throwError(() => new Error('Failed to approve task'));
      })
    );
  }

  /**
   * Rejects a task approval request
   * @param approvalId The ID of the approval to reject
   * @returns Observable of the updated TaskApproval
   */
  rejectTask(approvalId: number): Observable<TaskApproval> {
    if (!approvalId) {
      return throwError(() => new Error('Approval ID is required'));
    }

    console.log(`[ApprovalsService] Rejecting task approval ${approvalId}`);
    
    return this.http.post<TaskApproval>(`${this.APPROVAL_API_URL}/reject`, null, {
      params: new HttpParams().set('approvalId', approvalId.toString())
    }).pipe(
      tap(response => console.log('[ApprovalsService] Task rejection successful:', response)),
      catchError(error => {
        console.error('[ApprovalsService] Error rejecting task:', error);
        return throwError(() => new Error('Failed to reject task'));
      })
    );
  }
} 