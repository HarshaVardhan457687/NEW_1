import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface KeyResult {
  keyResultId: number;
  keyResultName: string;
}

export interface Task {
  taskId?: number;
  taskHeading: string;
  taskDescription: string;
  taskOwner: number;
  taskStartDate?: Date;
  taskDueDate: Date;
  taskTag: string;
  taskIsActive: boolean;
  taskAssociatedProject: number;
  taskAssociatedKeyResult: number;
  taskStatus: 'WAITING_FOR_APPROVAL' | 'PENDING' | 'COMPLETED';
  taskPriority: 'HIGH' | 'MEDIUM' | 'LOW';
}

@Injectable({
  providedIn: 'root'
})
export class AssignTaskService {
  private readonly PROJECT_API_URL = 'http://localhost:8060/api/projects';
  private readonly TASK_API_URL = 'http://localhost:8060/api/tasks';

  constructor(private http: HttpClient) {}

  /**
   * Gets all key results associated with a project
   * @param projectId The ID of the project
   * @returns Observable of KeyResult array
   */
  getKeyResults(projectId: number): Observable<KeyResult[]> {
    const params = new HttpParams().set('projectId', projectId.toString());

    return this.http.get<KeyResult[]>(`${this.PROJECT_API_URL}/keyresults`, { params }).pipe(
      tap(keyResults => console.log('[AssignTaskService] Key results retrieved:', keyResults))
    );
  }

  /**
   * Creates a new task
   * @param task The task to create
   * @returns Observable of the created Task
   */
  createTask(task: Task): Observable<Task> {
    console.log('[AssignTaskService] Creating task:', task);
    return this.http.post<Task>(`${this.TASK_API_URL}`, task).pipe(
      tap(createdTask => console.log('[AssignTaskService] Task created:', createdTask))
    );
  }
} 