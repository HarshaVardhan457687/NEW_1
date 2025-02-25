import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../shared/navbar/navbar.component';
import { StatsCardComponent } from '../../shared/stats-card/stats-card.component';
import { ActiveProjectSectionComponent } from '../../shared/active-project-section/active-project-section.component';
import { CurrentTasksSectionComponent } from '../../shared/current-tasks-section/current-tasks-section.component';
import { DashboardPageService } from '../../core/services/dashboard.page.service';

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
export class TeamManagerDashboardComponent implements OnInit {
  stats: StatCard[] = [
    { 
      title: 'Projects', 
      activeCount: 0,
      totalCount: 0,
      percentage: 0,
      icon: 'project_icon.png'
    },
    { 
      title: 'Objectives', 
      activeCount: 0,
      totalCount: 0,
      percentage: 0,
      icon: 'objectives_icon.png'
    },
    { 
      title: 'Key Results', 
      activeCount: 0,
      totalCount: 0,
      percentage: 0,
      icon: 'key_result_icon.png'
    },
    { 
      title: 'Tasks', 
      activeCount: 0,
      totalCount: 0,
      percentage: 0,
      icon: 'tasks_icon.png'
    }
  ];

  constructor(private dashboardService: DashboardPageService) {}

  ngOnInit() {
    this.dashboardService.getProjectProgress('project_manager').subscribe({
      next: (data) => {
        this.stats[0] = {
          ...this.stats[0],
          activeCount: data.activeProjects,
          totalCount: data.totalProjects,
          percentage: (data.activeProjects / data.totalProjects) * 100
        };
      },
      error: (error) => {
        console.error('Error loading projects data:', error);
      }
    });

    this.dashboardService.getObjectiveProgress('project_manager').subscribe({
      next: (data) => {
        this.stats[1] = {
          ...this.stats[1],
          activeCount: data.activeObjectives,
          totalCount: data.totalObjectives,
          percentage: (data.activeObjectives / data.totalObjectives) * 100
        };
      },
      error: (error) => {
        console.error('Error loading objectives data:', error);
      }
    });

    this.dashboardService.getKeyResultProgress('project_manager').subscribe({
      next: (data) => {
        this.stats[2] = {
          ...this.stats[2],
          activeCount: data.activeKeyResults,
          totalCount: data.totalKeyResults,
          percentage: (data.activeKeyResults / data.totalKeyResults) * 100
        };
      },
      error: (error) => {
        console.error('Error loading key results data:', error);
      }
    });
  }
}



