import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CurrentTaskCardComponent } from '../current-task-card/current-task-card.component';

interface Task {
  title: string;
  dueDate: string;
  tag: string;
  project: string;
  priority: 'High Priority' | 'Medium Priority' | 'Low Priority';
}

@Component({
  selector: 'app-current-tasks-section',
  standalone: true,
  imports: [CommonModule, CurrentTaskCardComponent],
  templateUrl: './current-tasks-section.component.html',
  styleUrl: './current-tasks-section.component.scss'
})
export class CurrentTasksSectionComponent {
  tasks: Task[] = [
    {
      title: 'Update OKR documentation',
      dueDate: 'Mar 15, 2025',
      tag: 'Documentation',
      project: 'Project Alpha',
      priority: 'High Priority'
    },
    {
      title: 'Review Q1 Objectives',
      dueDate: 'Mar 20, 2025',
      tag: 'Planning',
      project: 'Project Beta',
      priority: 'Medium Priority'
    }
  ];
}
