import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-tasks-overview-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tasks-overview-card.component.html',
  styleUrl: './tasks-overview-card.component.scss'
})
export class TasksOverviewCardComponent {
  @Input() projectId!: number;
}
