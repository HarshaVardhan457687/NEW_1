import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

export interface KeyResultUnitDTO {
  keyResultId: number;
  keyResultName: string;
  keyResultCurrentVal: number;
  keyResultTargetVal: number;
  keyResultUnit: string;
}

@Injectable({
  providedIn: 'root'
})
export class KeyResultIncrementService {
  private readonly API_URL = 'http://localhost:8060/api/keyresults'; 

  constructor(private http: HttpClient) {}

  getKeyResultProgress(taskId: number): Observable<KeyResultUnitDTO> {
    if (!taskId) {
      return throwError(() => new Error('Task ID is required'));
    }

    console.log(`[KeyResultIncrementService] Fetching progress for task ${taskId}`);

    return this.http.get<KeyResultUnitDTO>(`${this.API_URL}/get/progress`, {
      params: { taskId: taskId.toString() }
    }).pipe(
      tap(result => console.log('[KeyResultIncrementService] Key result progress:', result)),
      catchError(error => {
        console.error('[KeyResultIncrementService] Error fetching key result progress:', error);
        if (error.status === 404) {
          return throwError(() => new Error('No key result found for this task'));
        }
        return throwError(() => new Error('Failed to fetch key result progress'));
      })
    );
  }
}