import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressBarLinearComponent } from '../progress-bar-linear/progress-bar-linear.component';

@Component({
  selector: 'app-active-projects-card',
  standalone: true,
  imports: [CommonModule, ProgressBarLinearComponent],
  templateUrl: './active-projects-card.component.html',
  styleUrl: './active-projects-card.component.scss'
})
export class ActiveProjectsCardComponent {
  @Input() name: string = '';
  @Input() objectives: number = 0;
  @Input() progress: number = 0;

  get status(): string {
    if (this.progress >= 70) return 'On Track';
    if (this.progress < 20) return 'New';
    return 'At Risk';
  }

  get statusClass(): string {
    if (this.progress >= 70) return 'status-green';
    if (this.progress < 20) return 'status-blue';
    return 'status-red';
  }
}
