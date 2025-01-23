import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectCardComponent } from './project-card/project-card.component';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, ProjectCardComponent],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss'
})
export class ProjectsComponent {
  projects = [
    {
      title: 'Website Redesign',
      dueDate: 'Mar 15, 2025',
      teamSize: 8,
      role: 'UI/UX Lead',
      progress: 75,
      teamLead: 'Sarah Johnson'
    },
    {
      title: 'Mobile App Development',
      dueDate: 'Apr 20, 2025',
      teamSize: 12,
      role: 'Frontend Developer',
      progress: 45,
      teamLead: 'Michael Chen'
    },
    {
      title: 'Database Migration',
      dueDate: 'May 10, 2025',
      teamSize: 6,
      role: 'Developer',
      progress: 15,
      teamLead: 'Emily Brown'
    },
    // Add more projects as needed
  ];
}
