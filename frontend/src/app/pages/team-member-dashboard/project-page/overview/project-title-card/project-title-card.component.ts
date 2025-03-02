import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressBarLinearComponent } from '../../../../../shared/progress-bar-linear/progress-bar-linear.component';
import { Project } from '../../../../../core/services/project-overview.service';

@Component({
  selector: 'app-project-title-card',
  standalone: true,
  imports: [CommonModule, ProgressBarLinearComponent],
  templateUrl: './project-title-card.component.html',
  styleUrl: './project-title-card.component.scss'
})
export class ProjectTitleCardComponent {
  @Input() project!: Project;

  getStatusColor(): string {
    if (this.project.projectProgress >= 70) return 'status-green';
    if (this.project.projectProgress < 20) return 'status-blue';
    return 'status-red';
  }

  getStatus(): string {
    return this.project.projectStatus;
  }
}
