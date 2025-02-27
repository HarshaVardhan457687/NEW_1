import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ProgressBarLinearComponent } from '../progress-bar-linear/progress-bar-linear.component';
import { ProjectStatus, ProjectStatusDisplay } from '../../core/services/projects-page.service';

@Component({
  selector: 'app-project-card',
  standalone: true,
  imports: [CommonModule, ProgressBarLinearComponent],
  templateUrl: './project-card.component.html',
  styleUrl: './project-card.component.scss'
})
export class ProjectCardComponent {
  @Input() title: string = '';
  @Input() dueDate: string = '';
  @Input() teamSize: number = 0;
  @Input() objectivesCount: number = 0;
  @Input() progress: number = 0;
  @Input() projectManager: { name: string; profilePic: string } = { name: '', profilePic: '' };
  @Input() status: ProjectStatus = 'ON_TRACK';
  @Input() statusDisplay: ProjectStatusDisplay = 'On Track';
  @Input() priority: 'High' | 'Medium' | 'Low' = 'Medium';
  @Input() id: number = 0;
  @Input() dashboardType: 'team-manager' | 'team-leader' | 'team-member' = 'team-member';

  constructor(private router: Router) {}

  get statusClass(): string {
    switch(this.status) {
      case 'COMPLETED': return 'status-green';
      case 'ON_TRACK': return 'status-blue';
      case 'AT_RISK': return 'status-yellow';
      default: return 'status-blue';
    }
  }

  get priorityClass(): string {
    switch(this.priority) {
      case 'High': return 'priority-high';
      case 'Low': return 'priority-low';
      default: return 'priority-medium';
    }
  }

  navigateToConstruction() {
    let basePath = '';
    if (this.dashboardType === 'team-manager') {
      basePath = '/team-manager-dashboard';
    } else if (this.dashboardType === 'team-leader') {
      basePath = '/team-leader-dashboard';
    } else {
      basePath = '/team-member-dashboard';
    }
  
    this.router.navigate([`${basePath}/projects/${this.id}`]);
  }
  

}
