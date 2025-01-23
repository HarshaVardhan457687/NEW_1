import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../shared/navbar/navbar.component';
import { StatsCardComponent } from './stats-card/stats-card.component';
import { ActiveProjectSectionComponent } from './active-project-section/active-project-section.component';
import { CurrentTasksSectionComponent } from './current-tasks-section/current-tasks-section.component';


interface StatCard {
  title: string;
  activeCount: number;
  totalCount: number;
  percentage: number;
  icon?: string;
  showGraph: boolean;
}

@Component({
  selector: 'app-team-member-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    NavbarComponent,
    StatsCardComponent,
    ActiveProjectSectionComponent,
    CurrentTasksSectionComponent
  ],
  templateUrl: './team-member-dashboard.component.html',
  styleUrl: './team-member-dashboard.component.scss',
  host: { 'id': 'team-member-dashboard-component' }
})
export class TeamMemberDashboardComponent {
  stats: StatCard[] = [
    { 
      title: 'Projects', 
      activeCount: 52, 
      totalCount: 100, 
      percentage: 52,
      showGraph: false,
      icon: 'project_icon.png'
    },
    { 
      title: 'Objectives', 
      activeCount: 32, 
      totalCount: 80, 
      percentage: 40,
      showGraph: false,
      icon: 'objectives_icon.png' 
    },
    { 
      title: 'Key Results', 
      activeCount: 21, 
      totalCount: 35, 
      percentage: 60,
      showGraph: false,
      icon: 'key_result_icon.png' 
    },
    { 
      title: 'Tasks', 
      activeCount: 18, 
      totalCount: 20, 
      percentage: 90,
      showGraph: false,
      icon: 'tasks_icon.png' 
    }
  ];
}