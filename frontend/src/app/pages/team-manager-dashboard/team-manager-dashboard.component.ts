import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../shared/navbar/navbar.component';
import { StatsCardComponent } from '../../shared/stats-card/stats-card.component';
import { ActiveProjectSectionComponent } from '../../shared/active-project-section/active-project-section.component';
import { CurrentTasksSectionComponent } from '../../shared/current-tasks-section/current-tasks-section.component';

interface StatCard {
  title: string;
  activeCount: number;
  totalCount: number;
  percentage: number;
  icon?: string;
}

@Component({
  selector: 'app-team-manager-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    NavbarComponent,
    StatsCardComponent,
    ActiveProjectSectionComponent,
    CurrentTasksSectionComponent
  ],
  templateUrl: './team-manager-dashboard.component.html',
  styleUrl: './team-manager-dashboard.component.scss',
  host: { 'id': 'team-manager-dashboard-component' }
})
export class TeamManagerDashboardComponent {
  stats: StatCard[] = [
    { 
      title: 'Projects', 
      activeCount: 52, 
      totalCount: 100, 
      percentage: 52,
      icon: 'project_icon.png'
    },
    { 
      title: 'Objectives', 
      activeCount: 32, 
      totalCount: 80, 
      percentage: 40,
      icon: 'objectives_icon.png'
    },
    { 
      title: 'Key Results', 
      activeCount: 21, 
      totalCount: 35, 
      percentage: 60,
      icon: 'key_result_icon.png' 
    },
    { 
      title: 'Tasks', 
      activeCount: 18, 
      totalCount: 20, 
      percentage: 90,
      icon: 'tasks_icon.png' 
    }
  ];
}



