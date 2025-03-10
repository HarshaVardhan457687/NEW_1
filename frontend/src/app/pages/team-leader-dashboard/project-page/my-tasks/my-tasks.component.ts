import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ObjectiveStatCardComponent } from '../../../../shared/objective-stat-card/objective-stat-card.component';
import { MyTasksSectionComponent } from '../../../../shared/my-tasks-section/my-tasks-section.component';
import { ActivatedRoute } from '@angular/router';
import { MyTasksService, TaskDetailsDTO } from '../../../../core/services/my-tasks.service';

@Component({
  selector: 'app-my-tasks',
  standalone: true,
  imports: [CommonModule, ObjectiveStatCardComponent, MyTasksSectionComponent],
  templateUrl: './my-tasks.component.html',
  styleUrl: './my-tasks.component.scss'
})
export class MyTasksComponent implements OnInit {
  tasks: TaskDetailsDTO[] = [];
  isLoading = true;
  error?: string;
  
  taskStats = {
    pending: 0,
    waitingApproval: 0,
    totalTasks: 0,
    completed: 0
  };

  constructor(
    private myTasksService: MyTasksService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    console.log("MyTasksComponent initialized");
    const userEmail = localStorage.getItem('username');
    this.route.parent?.params.subscribe(params => {
      const projectId = Number(params['id']);
      
      if (!userEmail || !projectId) {
        this.error = 'Missing required parameters';
        console.error('Missing required parameters');
        this.isLoading = false;
        return;
      }

      // Fetch task statistics
      this.myTasksService.getTaskStatusCounts(userEmail, projectId).subscribe({
        next: (stats) => {
          this.taskStats = {
            pending: stats.pendingTasks,
            waitingApproval: stats.waitingForApprovalTasks,
            totalTasks: stats.totalTasks, 
            completed: stats.completedTasks
          };
          console.log(this.taskStats);
        },
        error: (err) => {
          console.error('Error loading task statistics:', err);
          this.error = 'Failed to load task statistics';
        }
      });

      // Fetch task details
      this.myTasksService.getTasksForUser(userEmail, projectId).subscribe({
        next: (tasks) => {
          this.tasks = tasks;
          console.log(this.tasks);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error loading tasks:', err);
          this.error = 'Failed to load tasks';
          this.isLoading = false;
        }
      });
    });
  }
}
