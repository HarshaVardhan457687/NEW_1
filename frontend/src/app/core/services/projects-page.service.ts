import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, forkJoin, map, switchMap, of, catchError } from 'rxjs';

export type ProjectStatus = 'ON_TRACK' | 'AT_RISK' | 'COMPLETED';

export type ProjectStatusDisplay = 'On Track' | 'At Risk' | 'Completed';

export interface ProjectData {
  id: number;
  title: string;
  dueDate: string;
  teamSize: number;
  objectivesCount: number;
  progress: number;
  projectManager: {
    name: string;
    profilePic: string;
  };
  status: ProjectStatusDisplay;
  priority: 'High' | 'Medium' | 'Low';
}

@Injectable({
  providedIn: 'root'
})
export class ProjectsPageService {
  private readonly API_URL = 'http://localhost:8060/api/users';

  constructor(private http: HttpClient) {}

  private getUserCredentials(): { email: string; role: string } {
    return {
      email: localStorage.getItem('username') || '',
      role: sessionStorage.getItem('role_selected') || ''
    };
  }

  private mapStatusToDisplay(status: ProjectStatus): ProjectStatusDisplay {
    switch (status) {
      case 'ON_TRACK': return 'On Track';
      case 'AT_RISK': return 'At Risk';
      case 'COMPLETED': return 'Completed';
      default: return 'On Track';
    }
  }

  getProjects(): Observable<ProjectData[]> {
    const { email, role } = this.getUserCredentials();
    
    if (!email || !role) {
      console.error('Missing credentials:', { email, role });
      return of([]); 
    }

    const params = new HttpParams()
      .set('userEmail', email)
      .set('userRole', role);

    return this.http.get<any[]>(`${this.API_URL}/all/projects`, { params })
      .pipe(
        catchError(error => {
          console.error('Error fetching projects:', error);
          return of([]);
        }),
        switchMap(projects => {
          if (!projects || !projects.length) return of([]);

          const managerRequests = projects.map(project => 
            this.getManagerData(project.projectManagerId)
          );

          return forkJoin(managerRequests).pipe(
            map(managers => {
              return projects.map((project, index) => {
                const status = (project.projectStatus as ProjectStatus) || 'ON_TRACK';
                return {
                  id: project.projectId || 0,
                  title: project.projectName || 'Untitled Project',
                  dueDate: project.projectDueDate || 'No Due Date',
                  teamSize: project.teamsInvolvedId?.length || 0,
                  objectivesCount: project.objectiveId?.length || 0,
                  progress: project.projectProgress || 0,
                  projectManager: managers[index],
                  status: this.mapStatusToDisplay(status),
                  priority: this.mapPriority(project.projectPriority)
                };
              });
            })
          );
        })
      );
  }

  private getManagerData(managerId: number): Observable<{ name: string; profilePic: string }> {
    if (!managerId) {
      return of({ name: 'Not Found', profilePic: 'assets/default_profile.png' });
    }

    return this.http.get<any>(`${this.API_URL}/${managerId}`).pipe(
      catchError(error => {
        console.error('Error fetching manager data:', error);
        return of({ userName: 'Not Found', userEmail: '' });
      }),
      switchMap(manager => {
        if (!manager || !manager.userEmail) {
          return of({ name: 'Not Found', profilePic: 'assets/default_profile.png' });
        }

        return this.http.get<string>(`${this.API_URL}/get/profile-pic?userEmail=${manager.userEmail}`)
          .pipe(
            catchError(error => {
              console.error('Error fetching profile picture:', error);
              return of('assets/default_profile.png');
            }),
            map(profilePic => ({
              name: manager.userName || 'Not Found',
              profilePic: profilePic || 'assets/default_profile.png'
            }))
          );
      })
    );
  }

  private mapPriority(priority: string): 'High' | 'Medium' | 'Low' {
    switch (priority?.toUpperCase()) {
      case 'HIGH': return 'High';
      case 'LOW': return 'Low';
      default: return 'Medium';
    }
  }

  setSelectedProject(projectId: number): void {
    sessionStorage.setItem('project_selected', projectId.toString());
  }

  getSelectedProject(): number | null {
    const projectId = sessionStorage.getItem('project_selected');
    return projectId ? parseInt(projectId) : null;
  }

  clearSelectedProject(): void {
    sessionStorage.removeItem('project_selected');
  }
} 