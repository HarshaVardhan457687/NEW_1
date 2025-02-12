import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TeamsService {
  private apiUrl = 'assets/mock_data/db.json';

  constructor(private http: HttpClient) {}

  getTeams(): Observable<any[]> {
    return this.http.get<any>(this.apiUrl).pipe(
      map(data => {
        return data.teams.map((team: any) => ({
          ...team,
          active: true // Setting all teams as active for now
        }));
      })
    );
  }
} 