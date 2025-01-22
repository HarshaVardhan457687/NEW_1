import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActiveProjectsCardComponent } from '../active-projects-card/active-projects-card.component';

interface Project {
  name: string;
  objectives: number;
  progress: number;
}

@Component({
  selector: 'app-active-project-section',
  standalone: true,
  imports: [CommonModule, ActiveProjectsCardComponent],
  templateUrl: './active-project-section.component.html',
  styleUrl: './active-project-section.component.scss'
})
export class ActiveProjectSectionComponent {
  projects: Project[] = [
    { name: 'Project Alpha', objectives: 4, progress: 75 },
    { name: 'Project Beta', objectives: 6, progress: 45 },
    { name: 'Project Gamma', objectives: 3, progress: 15 }
  ];
}
