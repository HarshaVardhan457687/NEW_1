import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TeamMember {
  name: string;
  role: string;
  image: string;
  progress: number;
}

export interface TeamLeader {
  name: string;
  role: string;
  image: string;
  progress: number;
}

export interface TeamOverview {
  teamMembers: number;
  objectives: string;
  tasks: string;
}

export interface Team {
  id: number;
  name: string;
  progress: number;
  teamLeader: TeamLeader;
  overview: TeamOverview;
  members: TeamMember[];
}

@Injectable({
  providedIn: 'root'
})
export class TeamService {
  private apiUrl = 'http://localhost:3000';

  constructor(private http: HttpClient) {}

  getTeamById(id: number): Observable<Team> {
    return this.http.get<Team>(`${this.apiUrl}/teams/${id}`);
  }

  getTeams(): Observable<Team[]> {
    return this.http.get<Team[]>(`${this.apiUrl}/teams`);
  }
} 