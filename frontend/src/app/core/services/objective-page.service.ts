import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';

export interface ObjectivePageStats {
  totalObjectives: number;
  completedObjectives: number;
  totalKeys: number;
  completedKeys: number;
}

interface KeyResult {
  keyResultId: number;
  name: string;
  priority: string;
  currKeyResultVal: number;
  targetKeyResultVal: number;
  unit: string;
  dueDate: string;
  progress: number;
  teamName: string;
  teamLeaderProfilePic: string | null;
}

export interface ObjectiveSummary {
  objectiveId: number;
  objectiveName: string;
  objectiveStatus: string;
  objectiveProgress: number;
  keyResults: KeyResult[];
}

@Injectable({
  providedIn: 'root'
})
export class ObjectivePageService {
  private readonly API_URL = 'http://localhost:8060/api/projects';
  private readonly OBJECTIVE_API_URL = 'http://localhost:8060/api/objective';

  constructor(private http: HttpClient) {}

  getObjectiveStats(projectId: number): Observable<ObjectivePageStats> {
    const objectivesInfo = this.http.post<Record<string, number>>(`${this.API_URL}/objectives-info/${projectId}`, {});
    const keyResultsCount = this.http.get<Record<string, number>>(`${this.API_URL}/keyresults/count`, {
      params: new HttpParams().set('projectId', projectId.toString())
    });

    return forkJoin({
      objectives: objectivesInfo,
      keyResults: keyResultsCount
    }).pipe(
      map(response => ({
        totalObjectives: (response.objectives['completedObjectives'] || 0) + 
                        (response.objectives['inProgressObjectives'] || 0) + 
                        (response.objectives['notStartedObjectives'] || 0),
        completedObjectives: response.objectives['completedObjectives'] || 0,
        totalKeys: response.keyResults['totalKeyResults'] || 0,
        completedKeys: response.keyResults['completedKeyResults'] || 0
      }))
    );
  }

  getObjectivesWithKeyResults(projectId: number): Observable<ObjectiveSummary[]> {
    return this.http.get<ObjectiveSummary[]>(`${this.OBJECTIVE_API_URL}/project/${projectId}`);
  }
}