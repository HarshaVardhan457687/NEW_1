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
      priority: 'Low Priority'
    },
    {
      title: 'Team Performance Analysis',
      dueDate: 'Mar 25, 2025',
      tag: 'Analysis',
      project: 'Project Gamma',
      priority: 'High Priority'
    },
    {
      title: 'Update Project Timeline',
      dueDate: 'Mar 18, 2025',
      tag: 'Planning',
      project: 'Project Delta',
      priority: 'Medium Priority'
    },
    {
      title: 'Stakeholder Meeting Prep',
      dueDate: 'Mar 22, 2025',
      tag: 'Meeting',
      project: 'Project Alpha',
      priority: 'Low Priority'
    },
    {
      title: 'Resource Allocation Review',
      dueDate: 'Mar 28, 2025',
      tag: 'Review',
      project: 'Project Beta',
      priority: 'Low Priority'
    }
  ];
}
