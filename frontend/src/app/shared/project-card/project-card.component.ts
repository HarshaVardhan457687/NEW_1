import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ProgressBarLinearComponent } from '../progress-bar-linear/progress-bar-linear.component';
import { ProjectsPageService, ProjectStatusDisplay } from '../../core/services/projects-page.service';
import { ProjectSelectionService } from '../../core/services/project-selection.service';

@Component({
  selector: 'app-project-card',
  standalone: true,
  imports: [CommonModule, ProgressBarLinearComponent],
  templateUrl: './project-card.component.html',
  styleUrl: './project-card.component.scss'
})
export class ProjectCardComponent implements OnInit {
  @Input() title: string = '';
  @Input() dueDate: string = '';
  @Input() teamSize: number = 0;
  @Input() objectivesCount: number = 0;
  @Input() progress: number = 0;
  @Input() projectManager: { name: string; profilePic: string } = { name: '', profilePic: '' };
  @Input() status: ProjectStatusDisplay = 'On Track';
  @Input() priority: 'High' | 'Medium' | 'Low' = 'Medium';
  @Input() id: number = 0;
  @Input() dashboardType: 'team-manager' | 'team-leader' | 'team-member' = 'team-member';

  constructor(
    private router: Router,
    private projectsPageService: ProjectsPageService,
    private projectSelectionService: ProjectSelectionService
  ) {}

  ngOnInit() {
    // Verify service injection
    if (!this.projectsPageService || !this.projectSelectionService) {
      console.error('Services not properly injected');
      return;
    }
  }

  get statusClass(): string {
    switch(this.status) {
      case 'Completed': return 'status-green';
      case 'On Track': return 'status-blue';
      case 'At Risk': return 'status-yellow';
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

  navigateToProject() {
    try {
      // Set the selected project in both services
      if (this.projectsPageService && this.projectSelectionService) {
        this.projectsPageService.setSelectedProject(this.id);
        this.projectSelectionService.setSelectedProject(this.id);
      } else {
        console.error('Services not available');
        return;
      }
      
      // navigate to the project page
      let basePath = '';
      if (this.dashboardType === 'team-manager') {
        basePath = '/team-manager-dashboard';
      } else if (this.dashboardType === 'team-leader') {
        basePath = '/team-leader-dashboard';
      } else {
        basePath = '/team-member-dashboard';
      }

      this.router.navigate([`${basePath}/projects/${this.id}`]);
    } catch (error) {
      console.error('Error in navigateToProject:', error);
    }
  }
}
