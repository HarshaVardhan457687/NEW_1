import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-current-task-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './current-task-card.component.html',
  styleUrl: './current-task-card.component.scss'
})
export class CurrentTaskCardComponent {
  @Input() title: string = '';
  @Input() dueDate: string = '';
  @Input() tag: string = '';
  @Input() project: string = '';
  @Input() priority: 'High Priority' | 'Medium Priority' | 'Low Priority' = 'Medium Priority';

  get priorityClass(): string {
    switch(this.priority) {
      case 'High Priority': return 'priority-high';
      case 'Medium Priority': return 'priority-medium';
      default: return 'priority-low';
    }
  }
}
