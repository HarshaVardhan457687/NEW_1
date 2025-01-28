import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressBarLinearComponent } from '../../../../../shared/progress-bar-linear/progress-bar-linear.component';
import { ProjectWithManager } from '../../../../../core/services/projects.service';

@Component({
  selector: 'app-project-title-card',
  standalone: true,
  imports: [CommonModule, ProgressBarLinearComponent],
  templateUrl: './project-title-card.component.html',
  styleUrl: './project-title-card.component.scss'
})
export class ProjectTitleCardComponent {
  @Input() project!: ProjectWithManager;

  getStatusColor(): string {
    if (this.project.progress >= 70) return 'status-green';
    if (this.project.progress < 20) return 'status-blue';
    return 'status-red';
  }

  getStatus(): string {
    if (this.project.progress >= 70) return 'On Track';
    if (this.project.progress < 20) return 'New';
    return 'At Risk';
  }
}
