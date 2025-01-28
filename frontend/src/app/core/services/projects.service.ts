import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, switchMap, of } from 'rxjs';
import { UserService, User } from './user.service';

export interface Project {
  id: number;
  title: string;
  dueDate: string;
  teamSize: number;
  role: string;
  progress: number;
  projectManagerId: number;
  status: 'In Progress' | 'Completed';
  priority: 'High' | 'Medium' | 'Low';
  objectives: number;
  active: boolean;
}

export interface ProjectWithManager extends Omit<Project, 'projectManagerId'> {
  projectManager: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = 'http://localhost:3000/projects';
  private users: User[] = [];

  constructor(
    private http: HttpClient,
    private userService: UserService
  ) {
    // Cache users on service initialization
    this.userService.getUsers().subscribe(users => this.users = users);
  }

  private getManagerName(managerId: number): string {
    const manager = this.users.find(user => user.id === managerId);
    return manager ? manager.name : 'Unknown Manager';
  }

  getProjects(): Observable<ProjectWithManager[]> {
    return this.http.get<Project[]>(this.apiUrl).pipe(
      map(projects => projects.map(project => ({
        ...project,
        projectManager: this.getManagerName(project.projectManagerId)
      })))
    );
  }

  getActiveProjects(): Observable<ProjectWithManager[]> {
    return this.http.get<Project[]>(`${this.apiUrl}?active=true`).pipe(
      map(projects => projects.map(project => ({
        ...project,
        projectManager: this.getManagerName(project.projectManagerId)
      })))
    );
  }
}