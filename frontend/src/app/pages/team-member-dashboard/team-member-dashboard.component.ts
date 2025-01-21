import { Component } from '@angular/core';
import { NavbarComponent } from '../../shared/navbar/navbar.component';
import { StatsCardComponent } from './stats-card/stats-card.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-team-member-dashboard',
  standalone: true,
  imports: [CommonModule, NavbarComponent, StatsCardComponent],
  templateUrl: './team-member-dashboard.component.html',
  styleUrl: './team-member-dashboard.component.scss',
  host: { 'id': 'team-member-dashboard-component' }
})
export class TeamMemberDashboardComponent {
  stats = [
    { title: 'All Project Progress', activeCount: 52, totalCount: 100, percentage: 52 },
    { title: 'Projects', activeCount: 32, totalCount: 80, percentage: 40 },
    { title: 'Objectives', activeCount: 21, totalCount: 35, percentage: 60 },
    { title: 'Tasks', activeCount: 18, totalCount: 20, percentage: 90 }
  ];
}