import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskDetailsDTO, TaskStatus, TaskPriority } from '../../core/services/my-tasks.service';

@Component({
  selector: 'app-my-task-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-task-card.component.html',
  styleUrl: './my-task-card.component.scss'
})
export class MyTaskCardComponent {
  @Input() task!: TaskDetailsDTO;
  isSubmitted = false;

  ngOnInit() {
    if (!this.task.taskStatus) {
      this.task.taskStatus = TaskStatus.PENDING;
    }
    if (!this.task.taskDescription) {
      this.task.taskDescription = 'No description available';
    }
    this.isSubmitted = (this.task.taskStatus === TaskStatus.WAITING_FOR_APPROVAL);
  }

  onSubmit() {
    this.isSubmitted = true;
    this.task.taskStatus = TaskStatus.WAITING_FOR_APPROVAL;
    
    // Here you would typically call a service to update the task status
  }

  formatPriority(priority: TaskPriority): string {
    switch (priority) {
      case TaskPriority.HIGH:
        return 'High Priority';
      case TaskPriority.MEDIUM:
        return 'Medium Priority';
      case TaskPriority.LOW:
        return 'Low Priority';
      default:
        return priority;
    }
  }

  formatStatus(status: TaskStatus): string {
    switch (status) {
      case TaskStatus.PENDING:
        return 'Pending';
      case TaskStatus.WAITING_FOR_APPROVAL:
        return 'Waiting For Approval';
      case TaskStatus.COMPLETED:
        return 'Completed';
      default:
        return status;
    }
  }
}
