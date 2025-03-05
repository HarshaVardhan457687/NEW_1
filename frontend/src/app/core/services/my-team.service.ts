import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface TeamResponse {
  teamName: string;
  totalKeyResults: { [key: string]: number };
  totalMembers: number;
  teamProgress: number;
  teamTasksCount: { [key: string]: number };
  teamLeaderName: string;
  teamLeaderProfile: string;
}

export interface TeamMemberProgress {
  userId: number;
  userName: string;
  userProfile: string;
  totalTasks: number;
  completedTasks: number;
  progress: number;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class MyTeamService {
  private readonly API_URL = 'http://localhost:8060/api/projects';
  private readonly USER_API_URL = 'http://localhost:8060/api/users';
  private readonly TEAM_API_URL = 'http://localhost:8060/api/teams';

  constructor(private http: HttpClient) {}

  /**
   * Get team details by project and team ID
   * @param projectId The ID of the project
   * @param teamId The ID of the team
   * @returns Observable of team details including progress, tasks, and leader info
   */
  getTeamById(projectId: number, teamId: number): Observable<TeamResponse> {
    console.log(`[MyTeamService] Getting team details for projectId: ${projectId}, teamId: ${teamId}`);
    const params = new HttpParams()
      .set('projectId', projectId.toString())
      .set('teamId', teamId.toString());

    return this.http.get<TeamResponse>(`${this.API_URL}/team`, { params }).pipe(
      tap(response => console.log('[MyTeamService] Team details response:', response))
    );
  }

  getMappedTeamId(projectId: number): Observable<number> {
    const email = localStorage.getItem('username');
    console.log(`[MyTeamService] Getting mapped teamId for projectId: ${projectId}, email: ${email}`);
    
    const params = new HttpParams()
      .set('userEmail', email || '')
      .set('projectId', projectId.toString());

    return this.http.get<number>(`${this.USER_API_URL}/team-mapped-to-project`, { params }).pipe(
      tap(teamId => console.log('[MyTeamService] Mapped teamId:', teamId))
    );
  }

  getTeamMembersProgress(teamId: number, projectId: number): Observable<TeamMemberProgress[]> {
    console.log(`[MyTeamService] Getting team members progress for teamId: ${teamId}, projectId: ${projectId}`);
    
    const params = new HttpParams()
      .set('teamId', teamId.toString())
      .set('projectId', projectId.toString());

    return this.http.get<TeamMemberProgress[]>(`${this.TEAM_API_URL}/project/members-progress`, { params }).pipe(
      tap(members => console.log('[MyTeamService] Team members progress:', members))
    );
  }
} 