import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ObjectiveStatCardComponent } from '../../../../shared/objective-stat-card/objective-stat-card.component';
import { MyTasksSectionComponent } from '../../../../shared/my-tasks-section/my-tasks-section.component';
import { TaskService, Task } from '../../../../core/services/tasks.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-my-tasks',
  standalone: true,
  imports: [CommonModule, ObjectiveStatCardComponent, MyTasksSectionComponent],
  templateUrl: './my-tasks.component.html',
  styleUrl: './my-tasks.component.scss'
})
export class MyTasksComponent implements OnInit {
  tasks: Task[] = [];
  isLoading = true;
  error?: string;
  
  taskStats = {
    pending: 0,
    waitingApproval: 0,
    denied: 0,
    completed: 0
  };

  constructor(
    private taskService: TaskService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.taskService.getTasks().subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.calculateTaskStats();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading tasks:', err);
        this.error = 'Failed to load tasks';
        this.isLoading = false;
      }
    });
  }

  private calculateTaskStats() {
    // Reset stats
    this.taskStats = {
      pending: 10, // Hardcoded for now, you can implement actual logic
      waitingApproval: 5,
      denied: 3,
      completed: 10
    };
  }
}
