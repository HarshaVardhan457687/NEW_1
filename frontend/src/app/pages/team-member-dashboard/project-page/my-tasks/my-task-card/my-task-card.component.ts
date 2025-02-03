import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Task {
  id: number;
  title: string;
  dueDate: string;
  tag: string;
  priority: string;
  projectId: number;
}

@Component({
  selector: 'app-my-task-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-task-card.component.html',
  styleUrl: './my-task-card.component.scss'
})
export class MyTaskCardComponent {
  @Input() task!: Task;
}
