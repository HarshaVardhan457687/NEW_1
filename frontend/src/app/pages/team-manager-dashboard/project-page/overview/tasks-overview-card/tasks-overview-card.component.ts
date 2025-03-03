import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectOverviewService, TaskStats } from '../../../../../core/services/project-overview.service';

@Component({
  selector: 'app-tasks-overview-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tasks-overview-card.component.html',
  styleUrl: './tasks-overview-card.component.scss'
})
export class TasksOverviewCardComponent implements OnInit {
  @Input() projectId!: number;
  taskStats: TaskStats = {
    total: 0,
    completed: 0,
    inProgress: 0,
    yourTasks: 0
  };

  constructor(private projectService: ProjectOverviewService) {}

  ngOnInit() {
    if (this.projectId) {
      this.projectService.getTaskStats(this.projectId).subscribe({
        next: (stats) => {
          this.taskStats = stats;
        },
        error: (err) => {
          console.error('Error loading task stats:', err);
        }
      });
    }
  }
}
