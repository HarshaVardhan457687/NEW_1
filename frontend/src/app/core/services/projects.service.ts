import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, forkJoin, switchMap } from 'rxjs';
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
}

export interface ProjectWithManager extends Omit<Project, 'projectManagerId'> {
  projectManager: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = 'http://localhost:3000/projects';

  constructor(
    private http: HttpClient,
    private userService: UserService
  ) {}

  getProjects(): Observable<ProjectWithManager[]> {
    return this.http.get<Project[]>(this.apiUrl).pipe(
      switchMap(projects => {
        const projectManagerRequests = projects.map(project => 
          this.userService.getUserById(project.projectManagerId)
        );
        
        return forkJoin(projectManagerRequests).pipe(
          map(managers => projects.map((project, index) => ({
            ...project,
            projectManager: managers[index].name
          })))
        );
      })
    );
  }

  getActiveProjects(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:3000/activeProjects');
  }
}