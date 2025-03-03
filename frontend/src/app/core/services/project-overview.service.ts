import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, switchMap, of, forkJoin, tap } from 'rxjs';

export type ProjectStatus = 'ON_TRACK' | 'AT_RISK' | 'COMPLETED';
export type ProjectStatusDisplay = 'On Track' | 'At Risk' | 'Completed';

export interface Project {
  projectId: number;
  projectName: string;
  projectDueDate: string;
  projectProgress: number;
  projectStatus: ProjectStatusDisplay;
  projectPriority: string;
  projectManagerName: string;
  objectivesCount: number;
}

export interface ObjectiveStats {
  completed: number;
  inProgress: number;
  notStarted: number;
}

export interface TaskStats {
  total: number;
  completed: number;
  inProgress: number;
  yourTasks: number;
}

interface ProjectTasksResponse {
  totalCompletedTask: number;
  totalActiveTask: number;
  totalTask: number;
}

export interface TimelineEvent {
  timeLineId: number;
  timeLineHeading: string;
  timeLineAssociatedProject: number;
  timeLineStatus: 'UPCOMING' | 'IN_PROGRESS' | 'COMPLETED';
}

export interface TeamDetails {
  teamName: string;
  teamLeaderName: string;
  teamLeaderProfile: string;  
}

@Injectable({
  providedIn: 'root'
})
export class ProjectOverviewService {
  private readonly API_URL = 'http://localhost:8060/api/projects';
  private readonly USER_API_URL = 'http://localhost:8060/api/users';
  private readonly TIMELINE_API_URL = 'http://localhost:8060/api/timelines';

  constructor(private http: HttpClient) {}

  getProjectById(id: number): Observable<Project> {
    return this.http.get<any>(`${this.API_URL}/${id}`).pipe(
      switchMap(response => {
        console.log(response);
        return this.getManagerName(response.projectManagerId).pipe(
          map(managerName => ({
            projectId: response.projectId,
            projectName: response.projectName || 'Untitled Project',
            projectDueDate: response.projectDueDate || 'No Due Date',
            projectProgress: response.projectProgress,
            projectStatus: this.mapStatus(response.projectStatus),
            projectPriority: this.mapPriority(response.projectPriority),
            projectManagerName: managerName,
            objectivesCount: response.objectiveId?.length || 0
          }))
        );
      }),
      catchError(error => {
        console.error('Error fetching project:', error);
        return of<Project>({
          projectId: 0,
          projectName: 'Error loading project',
          projectDueDate: '',
          projectProgress: 0,
          projectStatus: 'On Track',
          projectPriority: 'Medium',
          projectManagerName: 'Unknown Manager',
          objectivesCount: 0
        });
      })
    );
  }

  private getManagerName(managerId: number): Observable<string> {
    return this.http.get<any>(`${this.USER_API_URL}/${managerId}`).pipe(
      map(user => user.userName || 'Unknown Manager'),
      catchError(() => of('Unknown Manager'))
    );
  }

  private mapStatus(status: ProjectStatus): ProjectStatusDisplay {
    switch (status) {
      case 'AT_RISK': return 'At Risk';
      case 'COMPLETED': return 'Completed';
      default: return 'On Track';
    }
  }

  private mapPriority(priority: string): string {
    switch (priority?.toUpperCase()) {
      case 'HIGH': return 'High';
      case 'LOW': return 'Low';
      default: return 'Medium';
    }
  }

  getObjectiveStats(projectId: number): Observable<ObjectiveStats> {
    return this.http.post<any>(`${this.API_URL}/objectives-info/${projectId}`, {}).pipe(
      map(response => ({
        completed: response.completedObjectives || 0,
        inProgress: response.inProgressObjectives || 0,
        notStarted: response.notStartedObjectives || 0
      })),
      catchError(error => {
        console.error('Error fetching objective stats:', error);
        return of({
          completed: 0,
          inProgress: 0,
          notStarted: 0
        });
      })
    );
  }

  getTaskStats(projectId: number): Observable<TaskStats> {
    const userEmail = localStorage.getItem('username') || '';
    
    return forkJoin({
      projectTasks: this.http.get<ProjectTasksResponse>(`${this.API_URL}/task-info/${projectId}`),
      userTasks: this.http.get<number>(`${this.USER_API_URL}/project/${projectId}/user/active-tasks`, { params: { userEmail } })
    }).pipe(
      tap(({ projectTasks }) => console.log(projectTasks)),
      map(({ projectTasks, userTasks }) => ({
        total: projectTasks.totalTask || 0,
        completed: projectTasks.totalCompletedTask || 0,
        inProgress: projectTasks.totalActiveTask || 0,
        yourTasks: userTasks || 0
      })),
      catchError(error => {
        console.error('Error fetching task stats:', error);
        return of({
          total: 0,
          completed: 0,
          inProgress: 0,
          yourTasks: 0
        });
      })
    );
  }

  getProjectTimeline(projectId: number): Observable<TimelineEvent[]> {
    return this.http.get<TimelineEvent[]>(`${this.TIMELINE_API_URL}/project/${projectId}`).pipe(
      map(events => events.sort((a, b) => {
        // Sort by status: COMPLETED -> IN_PROGRESS -> UPCOMING
        const statusOrder = {
          'COMPLETED': 0,
          'IN_PROGRESS': 1,
          'UPCOMING': 2
        };
        return statusOrder[a.timeLineStatus] - statusOrder[b.timeLineStatus];
      })),
      catchError(error => {
        console.error('Error fetching timeline:', error);
        return of([]);
      })
    );
  }

  getProjectTeamDetails(projectId: number): Observable<TeamDetails[]> {
    return this.http.get<TeamDetails[]>(`${this.API_URL}/${projectId}/teams-details`).pipe(
      catchError(error => {
        console.error('Error fetching team details:', error);
        return of([]);
      })
    );
  }
} 