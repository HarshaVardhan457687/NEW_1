import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Statistics } from '../interfaces/dashboard.interface';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  getStatistics(): Observable<Statistics> {
    // Mock data - replace with actual API call
    return of({
      projects: { active: 5, total: 8 },
      objectives: { active: 12, total: 15 },
      tasks: { active: 25, total: 30 }
    });
  }
} 