import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface KeyResultRequest {
  keyResultName: string;
  keyResultTargetVal: number;
  keyResultUnit: string;
  keyResultDueDate: string;
  keyResultPriority: 'HIGH' | 'MEDIUM' | 'LOW';
  associatedObjectiveId: number;
  teamId: number;
}

export interface TeamSelectDTO {
  teamId: number;
  teamName: string;
}

@Injectable({
  providedIn: 'root'
})
export class AddKeyResultService {
  private readonly KEY_RESULT_API = 'http://localhost:8060/api/keyresults';  // Through API Gateway
  private readonly PROJECT_API = 'http://localhost:8060/api/projects';  // Through API Gateway

  constructor(private http: HttpClient) {}

  createKeyResult(keyResult: KeyResultRequest): Observable<any> {
    return this.http.post(this.KEY_RESULT_API, keyResult);
  }

  getTeamsForProject(projectId: number): Observable<TeamSelectDTO[]> {
    return this.http.get<TeamSelectDTO[]>(`${this.PROJECT_API}/teams-select?projectId=${projectId}`);
  }
} 