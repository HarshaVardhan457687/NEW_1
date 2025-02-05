import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

export interface Task {
  id: number;
  title: string;
  dueDate: string;
  tag: string;
  projectId: number;
  priority: 'High Priority' | 'Medium Priority' | 'Low Priority';
  project?: string;
  status?: 'pending' | 'waiting_approval' | 'denied' | 'completed';
  description?: string;
}

export interface TaskStats {
  pending: number;
  waitingApproval: number;
  denied: number;
  completed: number;
}

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = 'http://localhost:3000/tasks';

  constructor(private http: HttpClient) {}

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.apiUrl);
  }

  getTasksByProjectId(projectId: number): Observable<Task[]> {
    return this.getTasks().pipe(
      map(tasks => tasks.filter(task => task.projectId === projectId))
    );
  }

  getTaskStats(projectId?: number): Observable<TaskStats> {
    return this.getTasks().pipe(
      map(tasks => {
        const filteredTasks = projectId 
          ? tasks.filter(task => task.projectId === projectId)
          : tasks;

        return {
          pending: filteredTasks.filter(t => t.status === 'pending').length,
          waitingApproval: filteredTasks.filter(t => t.status === 'waiting_approval').length,
          denied: filteredTasks.filter(t => t.status === 'denied').length,
          completed: filteredTasks.filter(t => t.status === 'completed').length
        };
      })
    );
  }
}