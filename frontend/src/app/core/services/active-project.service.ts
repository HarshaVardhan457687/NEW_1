import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { UserRole } from './role-selection.service';

interface ProjectResponse {
  projectId: number;
  projectName: string;
  projectStatus: string;
  objectiveId: number[];
  projectProgress: number;
  active: boolean;
}

export interface ActiveProject {
  projectId: number;
  projectName: string;
  projectStatus: string;
  projectProgress: number;
  objectivesCount: number;
}

@Injectable({
  providedIn: 'root'
})
export class ActiveProjectService {
  private readonly API_URL = 'http://localhost:8060/api/users';

  constructor(private http: HttpClient) {}

  getActiveProjects(userEmail: string, role: UserRole): Observable<ActiveProject[]> {
    const params = new HttpParams()
      .set('userEmail', userEmail)
      .set('userRole', role);

    return this.http.get<ProjectResponse[]>(`${this.API_URL}/project/find-active`, { params })
      .pipe(
        map(projects => projects.map(project => ({
          projectId: project.projectId,
          projectName: project.projectName,
          projectStatus: project.projectStatus,
          projectProgress: Math.floor(project.projectProgress),
          objectivesCount: project.objectiveId?.length || 0
        })))
      );
  }
} 