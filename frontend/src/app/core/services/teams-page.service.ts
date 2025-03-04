import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface TeamResponse {
  teamId: number;
  teamName: string;
  totalKeyResults: { [key: string]: number };
  totalMembers: number;
  teamProgress: number;
  teamTasksCount: { [key: string]: number };
  teamLeaderName: string;
  teamLeaderProfile: string;
}

@Injectable({
  providedIn: 'root'
})
export class TeamsPageService {
  private readonly API_URL = 'http://localhost:8060/api/projects';

  constructor(private http: HttpClient) {}

  getTeamsForProject(projectId: number): Observable<TeamResponse[]> {
    console.log('[TeamsPageService] Getting teams for project:', projectId);
    
    const params = new HttpParams()
      .set('projectId', projectId.toString());

    return this.http.get<TeamResponse[]>(`${this.API_URL}/teams`, { params }).pipe(
      tap(teams => console.log('[TeamsPageService] Teams retrieved:', teams))
    );
  }

  getTeamById(projectId: number, teamId: number): Observable<TeamResponse> {
    console.log('[TeamsPageService] Getting team details for project:', projectId, 'team:', teamId);
    
    const params = new HttpParams()
      .set('projectId', projectId.toString())
      .set('teamId', teamId.toString());

    return this.http.get<TeamResponse>(`${this.API_URL}/team`, { params }).pipe(
      tap(team => console.log('[TeamsPageService] Team details retrieved:', team))
    );
  }
} 