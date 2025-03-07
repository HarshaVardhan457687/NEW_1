import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ObjectiveRequest {
  objectiveName: string;
  objectiveDueDate: string;
  objectivePriority: 'HIGH' | 'MEDIUM' | 'LOW';
  mappedProject: number;
}

@Injectable({
  providedIn: 'root'
})
export class AddObjectiveService {
  private readonly OBJECTIVE_API = 'http://localhost:8060/api/objective';  // Through API Gateway

  constructor(private http: HttpClient) {}

  createObjective(objective: ObjectiveRequest): Observable<any> {
    return this.http.post(this.OBJECTIVE_API, objective);
  }
}