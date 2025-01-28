import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, switchMap, BehaviorSubject, combineLatest } from 'rxjs';
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

export interface ProjectWithManager {
  id: number;
  title: string;
  projectManager: string;
  teamSize: number;
  status: 'In Progress' | 'Completed';
  priority: 'High' | 'Medium' | 'Low';
  progress: number;
  dueDate: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = 'http://localhost:3000';
  private users$ = new BehaviorSubject<User[]>([]);

  constructor(
    private http: HttpClient,
    private userService: UserService
  ) {
    // Load users immediately and store them
    this.userService.getUsers().subscribe({
      next: (users) => {
        console.log('Loaded users:', users); // Debug log
        this.users$.next(users);
      },
      error: (err) => console.error('Error loading users:', err)
    });
  }

  private getManagerName(managerId: number): Observable<string> {
    return this.userService.getUserById(managerId).pipe(
      map(user => user.name)
    );
  }

  getProjects(): Observable<ProjectWithManager[]> {
    return this.http.get<Project[]>(`${this.apiUrl}/projects`).pipe(
      switchMap(projects => {
        const projectsWithManagers = projects.map(project => 
          this.getManagerName(project.projectManagerId).pipe(
            map(managerName => ({
              id: project.id,
              title: project.title,
              projectManager: managerName,
              teamSize: project.teamSize,
              status: project.status,
              priority: project.priority,
              progress: project.progress,
              dueDate: project.dueDate,
              role: project.role
            }))
          )
        );
        return combineLatest(projectsWithManagers);
      })
    );
  }

  getProjectById(id: number): Observable<ProjectWithManager> {
    return this.http.get<Project>(`${this.apiUrl}/projects/${id}`).pipe(
      switchMap(project => 
        this.getManagerName(project.projectManagerId).pipe(
          map(managerName => ({
            id: project.id,
            title: project.title,
            projectManager: managerName,
            teamSize: project.teamSize,
            status: project.status,
            priority: project.priority,
            progress: project.progress,
            dueDate: project.dueDate,
            role: project.role
          }))
        )
      )
    );
  }

  getActiveProjects(): Observable<ProjectWithManager[]> {
    return this.http.get<Project[]>(`${this.apiUrl}/projects?active=true`).pipe(
      switchMap(projects => {
        const projectsWithManagers = projects.map(project => 
          this.getManagerName(project.projectManagerId).pipe(
            map(managerName => ({
              id: project.id,
              title: project.title,
              projectManager: managerName,
              teamSize: project.teamSize,
              status: project.status,
              priority: project.priority,
              progress: project.progress,
              dueDate: project.dueDate,
              role: project.role
            }))
          )
        );
        return combineLatest(projectsWithManagers);
      })
    );
  }
}