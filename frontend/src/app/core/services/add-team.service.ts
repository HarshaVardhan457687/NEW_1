import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserSummary {
  userId: number;
  userName: string;
  userProfilePhoto: string;
}

export interface CreateTeamRequest {
  teamName: string;
  teamLead: number;
  teamMembers: number[];
  assignedProject: number;
  assignedKeyResult: number[];
}

export interface Team {
  teamId: number;
  teamName: string;
  teamLead: number;
  teamMembers: number[];
  assignedProject: number;
  assignedKeyResult: number[];
}

@Injectable({
  providedIn: 'root'
})
export class AddTeamService {
  private readonly USER_API_URL = 'http://localhost:8060/api/users';
  private readonly TEAM_API_URL = 'http://localhost:8060/api/teams';

  constructor(private http: HttpClient) {}

  /**
   * Get all users summary for team formation
   * Returns users with their basic info and profile pictures
   */
  getAllUsersSummary(): Observable<UserSummary[]> {
    return this.http.get<UserSummary[]>(`${this.USER_API_URL}/summary`);
  }

  /**
   * Create a new team
   * @param teamData Team creation data including name, leader, members and project
   */
  createTeam(teamData: CreateTeamRequest): Observable<Team> {
    return this.http.post<Team>(`${this.TEAM_API_URL}`, teamData);
  }
} 