import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard/dashboard.component';

@Component({
  selector: 'app-analytics-component',
  standalone: true,
  imports: [CommonModule, DashboardComponent],
  templateUrl: './analytics-component.component.html',
  styleUrl: './analytics-component.component.scss'
})
export class AnalyticsComponent {
  // Analytics component logic goes here
}
