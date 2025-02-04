import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ProgressBarLinearComponent } from '../progress-bar-linear/progress-bar-linear.component';

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
  @Input() role: string = '';
  @Input() progress: number = 0;
  @Input() projectManager: string = '';
  @Input() status: 'In Progress' | 'Completed' = 'In Progress';
  @Input() priority: 'High' | 'Medium' | 'Low' = 'Medium';
  @Input() id: number = 0;
  @Input() dashboardType: 'team-manager' | 'team-leader' | 'team-member' = 'team-member';

  constructor(private router: Router) {}

  get statusClass(): string {
    return this.status === 'Completed' ? 'status-green' : 'status-blue';
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
