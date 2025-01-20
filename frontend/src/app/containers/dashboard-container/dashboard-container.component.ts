import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Statistics } from '../../interfaces/dashboard.interface';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-dashboard-container',
  template: `
    <app-dashboard
      [statistics]="statistics$ | async"
      [userName]="userName">
    </app-dashboard>
  `,
  host: { class: 'dashboard-container' }
})
export class DashboardContainerComponent {
  statistics$: Observable<Statistics>;
  userName = 'John Doe';

  constructor(private readonly dashboardService: DashboardService) {
    this.statistics$ = this.dashboardService.getStatistics();
  }
} 