import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserRole } from './role-selection.service';
import { ActiveProjectService, ActiveProject } from './active-project.service';

export interface ProjectProgress {
  activeProjects: number;
  totalProjects: number;
}

export interface ObjectiveProgress {
  activeObjectives: number;
  totalObjectives: number;
}

export interface KeyResultProgress {
  activeKeyResults: number;
  totalKeyResults: number;
}

export interface TaskProgress {
  activeTasks: number;
  totalTasks: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardPageService {
  private readonly API_URL = 'http://localhost:8060/api/users';  // Through API Gateway

  constructor(
    private http: HttpClient,
    private activeProjectService: ActiveProjectService
  ) {}

  private getUserEmail(): string {
    return localStorage.getItem('username') || '';
  }

  getProjectProgress(role: UserRole): Observable<ProjectProgress> {
    const params = new HttpParams()
      .set('userEmail', this.getUserEmail())
      .set('userRole', role);

    return this.http.get<ProjectProgress>(`${this.API_URL}/projects/active/count`, { params });
  }

  getObjectiveProgress(role: UserRole): Observable<ObjectiveProgress> {
    const params = new HttpParams()
      .set('userEmail', this.getUserEmail())
      .set('userRole', role);

    return this.http.get<ObjectiveProgress>(`${this.API_URL}/objectives/count`, { params });
  }

  getKeyResultProgress(role: UserRole): Observable<KeyResultProgress> {
    const params = new HttpParams()
      .set('userEmail', this.getUserEmail())
      .set('userRole', role);

    return this.http.get<KeyResultProgress>(`${this.API_URL}/key-results/count`, { params });
  }

  getTaskProgress(role: UserRole): Observable<TaskProgress> {
    const params = new HttpParams()
      .set('userEmail', this.getUserEmail())
      .set('userRole', role);

    return this.http.get<TaskProgress>(`${this.API_URL}/tasks/tasks-count`, { params });
  }

  getActiveProjects(role: UserRole): Observable<ActiveProject[]> {
    return this.activeProjectService.getActiveProjects(this.getUserEmail(), role);
  }
}
