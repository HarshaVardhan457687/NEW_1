import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';

export interface KeyResult {
  id: number;
  objectiveId: number;
  title: string;
  owner: string;
  priority: string;
  status: string;
  progress: number;
  timeline: string;
}

export interface Objective {
  id: number;
  projectId: number;
  title: string;
  description: string;
  progress: number;
  status: string;
  keyResults: number[];
}

export interface ObjectiveStats {
  totalObjectives: number;
  inProgress: number;
  completed: number;
  completedKeys: number;
}

@Injectable({
  providedIn: 'root'
})
export class ObjectivesService {
  private apiUrl = 'assets/mock_data/db.json';

  constructor(private http: HttpClient) { }

  getProjectObjectives(projectId: number): Observable<Objective[]> {
    return this.http.get<{ objectives: Objective[] }>(this.apiUrl).pipe(
      map(response => response.objectives.filter(obj => obj.projectId === projectId))
    );
  }

  getKeyResults(): Observable<KeyResult[]> {
    return this.http.get<{ keyResults: KeyResult[] }>(this.apiUrl).pipe(
      map(response => response.keyResults)
    );
  }

  getObjectiveStats(projectId: number): Observable<ObjectiveStats> {
    return this.http.get<{ objectives: Objective[], keyResults: KeyResult[] }>(this.apiUrl).pipe(
      map(response => {
        const projectObjectives = response.objectives.filter(obj => obj.projectId === projectId);
        const allKeyResults = response.keyResults;
        
        const totalObjectives = projectObjectives.length;
        const inProgress = projectObjectives.filter(obj => obj.status === 'in_progress').length;
        const completed = projectObjectives.filter(obj => obj.status === 'completed').length;
        
        const projectKeyResultIds = projectObjectives.flatMap(obj => obj.keyResults);
        const completedKeys = allKeyResults.filter(
          kr => projectKeyResultIds.includes(kr.id) && kr.status === 'completed'
        ).length;

        return {
          totalObjectives,
          inProgress,
          completed,
          completedKeys
        };
      })
    );
  }
}
