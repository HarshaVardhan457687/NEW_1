import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ProgressBarLinearComponent } from '../../../../shared/progress-bar-linear/progress-bar-linear.component';

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
  @Input() teamLead: string = '';

  constructor(private router: Router) {}

  get statusClass(): string {
    if (this.progress >= 70) return 'status-green';
    if (this.progress < 20) return 'status-blue';
    return 'status-red';
  }

  navigateToConstruction() {
    this.router.navigate(['/construction']);
  }
}
