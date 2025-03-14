import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, catchError } from 'rxjs';
import { map } from 'rxjs/operators';

interface TeamProgress {
  progress: number;
}

interface TeamMemberProgressDto {
  userId: number;
  userName: string;
  userProfile: string;
  role: string;
  totalTasks: number;
  completedTasks: number;
  progress: number;
}

interface Team {
  id: number;
  name: string;
}

interface ObjectivePerformance {
  AT_RISK: number;
  ON_TRACK: number;
  COMPLETED: number;
}

interface MonthlyTaskApprovalDTO {
  year: number;
  month: number;
  totalTasks: number;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8060/api';

  constructor(private http: HttpClient) { }

  getObjectivePerformance(projectId: number): Observable<{name: string, value: number}[]> {
    return this.http.get<ObjectivePerformance>(`${this.baseUrl}/objective/objective-performance/888290452986`)
      .pipe(
        map(data => [
          { name: 'On Track', value: data.ON_TRACK || 0 },
          { name: 'At Risk', value: data.AT_RISK || 0 },
          { name: 'Completed', value: data.COMPLETED || 0 }
        ]),
        catchError(() => of([
          { name: 'On Track', value: 0 },
          { name: 'At Risk', value: 0 },
          { name: 'Completed', value: 0 }
        ]))
      );
  }

  getTeamProgress(projectId: number, teamId: number): Observable<TeamProgress> {
    return this.http.get<TeamProgress>(`${this.baseUrl}/teams/progress?projectId=${projectId}&teamId=${teamId}`)
      .pipe(
        catchError(() => of({ progress: 0 }))
      );
  }

  getTeams(projectId: number): Observable<Team[]> {
    return this.http.get<Team[]>(`${this.baseUrl}/teams/project/${projectId}`)
      .pipe(
        catchError(() => of([]))
      );
  }

  getTeamMembersProgress(teamId: number, projectId: number): Observable<TeamMemberProgressDto[]> {
    return this.http.get<TeamMemberProgressDto[]>(`${this.baseUrl}/teams/project/members-progress?teamId=${teamId}&projectId=${projectId}`)
      .pipe(
        catchError(() => of([]))
      );
  }

  getApprovedTasksByMonth(projectId: number): Observable<MonthlyTaskApprovalDTO[]> {
    return this.http.get<MonthlyTaskApprovalDTO[]>(`${this.baseUrl}/approvals/approvedTasksByMonth/${projectId}`)
      .pipe(
        catchError(() => of([]))
      );
  }
}
