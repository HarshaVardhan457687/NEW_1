import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map, forkJoin, mergeMap } from 'rxjs';
import { UserRole } from './role-selection.service';

type TaskPriority = 'HIGH' | 'MEDIUM' | 'LOW';

interface TaskResponse {
  taskId: number;
  taskHeading: string;
  taskDueDate: string;
  taskTag: string;
  taskAssociatedProject: number | null | undefined;
  taskPriority: TaskPriority | null | undefined;
  taskStatus: string;
}

export interface ActiveTask {
  id: number;
  title: string;
  dueDate: string;
  tag: string;
  projectId: number | null | undefined;
  projectTitle: string;
  priority: 'High Priority' | 'Medium Priority' | 'Low Priority';
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class CurrentTasksSectionService {
  private readonly USER_API_URL = 'http://localhost:8060/api/users';
  private readonly PROJECT_API_URL = 'http://localhost:8060/api/projects';

  constructor(private http: HttpClient) {}

  private getProjectName(projectId: number): Observable<string> {
    return this.http.get(`${this.PROJECT_API_URL}/getname`, {
      params: new HttpParams().set('projectId', projectId),
      responseType: 'text'  // Specify text response type
    });
  }

  private formatPriority(priority: TaskPriority | null | undefined): 'High Priority' | 'Medium Priority' | 'Low Priority' {
    if (!priority) {
      return 'Medium Priority'; // default value
    }
    const formatted = priority.charAt(0) + priority.slice(1).toLowerCase() + ' Priority';
    return formatted as 'High Priority' | 'Medium Priority' | 'Low Priority';
  }

  getActiveTasks(userEmail: string, role: UserRole): Observable<ActiveTask[]> {
    const params = new HttpParams()
      .set('userEmail', userEmail)
      .set('userRole', role);

    return this.http.get<TaskResponse[]>(`${this.USER_API_URL}/tasks/active`, { params })
      .pipe(
        map(tasks => tasks.map(task => ({
          id: task.taskId,
          title: task.taskHeading,
          dueDate: task.taskDueDate,
          tag: task.taskTag,
          projectId: task.taskAssociatedProject,
          priority: this.formatPriority(task.taskPriority),
          status: task.taskStatus
        }))),
        mergeMap(tasks => {
          const projectRequests = tasks.map(task => 
            task.projectId ? 
            this.getProjectName(task.projectId).pipe(
              map(projectTitle => ({
                ...task,
                projectTitle
              }))
            ) :
            // Return observable with default value for null/undefined projectId
            new Observable<ActiveTask>(subscriber => {
              subscriber.next({
                ...task,
                projectTitle: 'No project found'
              });
              subscriber.complete();
            })
          );
          return forkJoin(projectRequests);
        })
      );
  }
} 