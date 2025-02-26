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
export class TeamMemberDashboardComponent implements OnInit {
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
    // Handle each request independently
    this.dashboardService.getProjectProgress('team_member').subscribe({
      next: (data) => {
        this.stats[0] = {
          ...this.stats[0],
          activeCount: data.activeProjects,
          totalCount: data.totalProjects,
          percentage: Math.floor(((data.totalProjects - data.activeProjects) / data.totalProjects) * 100)
        };
      },
      error: (error) => {
        console.error('Error loading projects data:', error);
        // Keep default values for this card
      }
    });

    this.dashboardService.getObjectiveProgress('team_member').subscribe({
      next: (data) => {
        this.stats[1] = {
          ...this.stats[1],
          activeCount: data.activeObjectives,
          totalCount: data.totalObjectives,
          percentage: Math.floor(((data.totalObjectives - data.activeObjectives) / data.totalObjectives) * 100)
        };
      },
      error: (error) => {
        console.error('Error loading objectives data:', error);
      }
    });

    this.dashboardService.getKeyResultProgress('team_member').subscribe({
      next: (data) => {
        this.stats[2] = {
          ...this.stats[2],
          activeCount: data.activeKeyResults,
          totalCount: data.totalKeyResults,
          percentage: Math.floor(((data.totalKeyResults - data.activeKeyResults) / data.totalKeyResults) * 100)
        };
      },
      error: (error) => {
        console.error('Error loading key results data:', error);
      }
    });

    this.dashboardService.getTaskProgress('team_member').subscribe({
      next: (data) => {
        this.stats[3] = {
          ...this.stats[3],
          activeCount: data.activeTasks,
          totalCount: data.totalTasks,
          percentage: Math.floor(((data.totalTasks - data.activeTasks) / data.totalTasks) * 100)
        };
      },
      error: (error) => {
        console.error('Error loading tasks data:', error);
      }
    });
  }
}





