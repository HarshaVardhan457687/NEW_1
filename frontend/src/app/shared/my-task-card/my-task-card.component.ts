import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Task } from '../../core/services/tasks.service';

@Component({
  selector: 'app-my-task-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-task-card.component.html',
  styleUrl: './my-task-card.component.scss'
})
export class MyTaskCardComponent {
  @Input() task!: Task;
  isSubmitted = false;

  ngOnInit() {
    if (!this.task.status) {
      this.task.status = 'pending';
    }
    if (!this.task.description) {
      this.task.description = 'No description available';
    }
  }

  onSubmit() {
    this.isSubmitted = true;
    this.task.status = 'waiting_approval';
    // Here you would typically call a service to update the task status
  }
}
