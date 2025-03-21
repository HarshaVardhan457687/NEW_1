import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskDetailsDTO, TaskStatus, TaskPriority, MyTasksService } from '../../core/services/my-tasks.service';

@Component({
  selector: 'app-my-task-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-task-card.component.html',
  styleUrl: './my-task-card.component.scss'
})
export class MyTaskCardComponent {
  @Input() task!: TaskDetailsDTO;
  @Input() projectId!: number;
  isSubmitted = false;
  errorMessage: string | null = null;

  constructor(private myTasksService: MyTasksService) {}

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
    this.errorMessage = null; // Reset error message before new submission

    this.myTasksService.submitTaskForApproval(this.task.taskId, this.projectId)
      .subscribe({
        next: (response) => {
          console.log('Task submitted successfully:', response);
          this.isSubmitted = true;
          this.task.taskStatus = TaskStatus.WAITING_FOR_APPROVAL;
        },
        error: (error) => {
          console.error('Error submitting task:', error);
          this.errorMessage =  'An error occurred while submitting the task';
        }
      });
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

  get getButtonText(): string {
    switch (this.task.taskStatus) {
      case TaskStatus.COMPLETED:
        return 'Completed';
      case TaskStatus.WAITING_FOR_APPROVAL:
        return 'Submitted';
      default:
        return 'Submit';
    }
  }

  get isDisabled(): boolean {
    return this.task.taskStatus === TaskStatus.COMPLETED || 
    this.task.taskStatus === TaskStatus.WAITING_FOR_APPROVAL;
  }

}
